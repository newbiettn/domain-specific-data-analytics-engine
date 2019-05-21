package service;

import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import mf.generator.MetafeatureGenerator;
import model.DMOperator;
import model.DMPlan;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import planner.translator.PlanTranslator;
import smile.clustering.GMeans;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.WekaException;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;
import weka.knowledgeflow.*;
import weka.knowledgeflow.steps.ClassifierPerformanceEvaluator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Services for MainController.
 *
 * @author Ngoc Tran
 * @since 2019-05-13
 */
public class MainControllerService {
    private static Logger logger = LoggerFactory.getLogger(MainControllerService.class);
    ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    String filePath = propGetter.getProperty("sparqlml.tmp.data.filepath");
    String trainingCsv = filePath + "sparql_data_tmp.csv";
    String sparqlEndpoint = "http://localhost:3030/austin/query";

    public boolean execQuery(String q, String type){
        if (type == "Select")
            return execSelectQuery(q);
        else if (type == "Ask")
            return execAskQuery(q);
        else if (type == "Create Prediction Model")
            return execCreatePredictionModelQuery(q);

        return false;
    }

    public boolean execCreatePredictionModelQuery(String queryString) {
        try {
            String filePath = propGetter.getProperty("sparqlml.tmp.data.filepath");
            int seed = 1;
            MetafeatureGenerator mfGen = new MetafeatureGenerator();
            DbUtils dbUtils = new DbUtils();
            int clockTimeout = 30000;
            String trainingCsv = filePath + "sparql_data_tmp.csv";
            String trainingArff = filePath + "sparql_data_tmp.arff";

            // Create the data.
            MLQuery q = MLQueryFactory.create(queryString);
            ArrayList<Var> featureVars = q.getFeatureVars();
            Var tVar = q.setTargetName();
            Var mVar = q.getModelName();
            String modelName = mVar.getVarName();

            Query selectQuery = QueryFactory.make() ;
            selectQuery.setQuerySelectType() ;
            selectQuery.setQueryPattern(q.getQueryPattern());
            selectQuery.getPrefixMapping().setNsPrefix("diab" , "http://localhost:2020/resource/") ;
            selectQuery.getPrefixMapping().setNsPrefix("foaf" , "http://xmlns.com/foaf/0.1/") ;
            selectQuery.getPrefixMapping().setNsPrefix("rdf" , "http://www.w3.org/1999/02/22-rdf-syntax-ns") ;
            selectQuery.addResultVar(Var.alloc(tVar.getVarName()) );
            for (Var v: featureVars){
                Var var = Var.alloc(v.getVarName()) ;
                selectQuery.addResultVar(var);
            }


            selectQuery.serialize(new IndentedWriter(System.out,true)) ;
            System.out.println() ;

            try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", selectQuery) ) {
                // Set the DBpedia specific timeout.
                ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

                // Execute.
                ResultSet rs = qexec.execSelect();
                ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(rs);
                int numCols = resultSetRewindable.getResultVars().size();

                StringBuilder header = new StringBuilder();
                StringBuilder row = new StringBuilder();
                for (int r = 0; resultSetRewindable.hasNext(); r++) {
                    QuerySolution rBind = resultSetRewindable.nextSolution();
                    for ( int col = 0 ; col < numCols ; col++ ) {
                        String rVar = rs.getResultVars().get(col);
                        // Print col headers
                        if (r == 0){
                            if (col < numCols-1)
                                header.append(rVar).append(",");
                            else
                                header.append(rVar).append("\n");
                        }
                        // Print row
                        RDFNode obj = rBind.get(rVar);
                        Literal l = obj.asLiteral();
                        String v = l.getString();
                        if (col < numCols-1)
                            row.append(v).append(",");
                        else
                            row.append(v).append("\n");
                    }
                }
                // Save to CSV
                FileWriter fw = new FileWriter(
                        trainingCsv,
                        false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw);
                out.print(header.toString() + row.toString());
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            /* Convert CSV file to ARFF */
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(new File(trainingCsv));
            Instances data = csvLoader.getDataSet();

            // Put the target attribute to the last position to manage easier
            Attribute tAttr = data.attribute(tVar.getVarName());
            int tIndex = tAttr.index() + 1;
            StringBuilder attrIds = new StringBuilder();
            for (int i = 1; i <= data.numAttributes(); i++){
                if (i != tIndex){
                    attrIds.append(i).append(",");
                }
            }
            attrIds.append(tIndex);
            Reorder reorder = new Reorder();
            reorder.setAttributeIndices(attrIds.toString());
            reorder.setInputFormat(data);
            data = Filter.useFilter(data, reorder);
            data.setClassIndex(data.numAttributes() - 1);

            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(trainingArff));
            saver.writeBatch();

            /* Learn ML from the new generated dataset */
            FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            GMeans gm = (GMeans) oi.readObject();
            oi.close();

            File trainingset = new File(trainingArff);
            Map<String, Double> mf = mfGen.generate(trainingset);
            mf.remove("NumberOfInstancesWithMissingValues");
            mf.remove("NumberOfMissingValues");
            mf.remove("PercentageOfMissingValues");
            mf.remove("PercentageOfInstancesWithMissingValues");
            double[] x = new double[mf.size()];
            int i = 0;
            for (String k : mf.keySet()){
                x[i] = mf.get(k);
                i++;
            }

            double[] normalizedX = normalize(x);
            System.out.println(normalizedX.length);
            int cluster = gm.predict(normalizedX);
            logger.info( "The dataset belongs to the cluster: " + cluster);

            /* Data mining */
//        String trainingFileUrl = filePath + trainingArff;
            List<Map<String, Object>> workflows = dbUtils.getWorkflowOfDatasetByCluster(cluster);
            logger.info( "The size of workflows: " + workflows);
            String[] classifiers = new String[workflows.size()];
            String[] attributeSelections = new String[workflows.size()];

            int flowIndex = 0;
            int lastIndex = (workflows.size())-1;
            List<Map<String, Object>> multiFlowResult = new ArrayList();
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Runnable clockRun = () -> {
                try {
                    Thread.sleep(clockTimeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Future<?> clockFuture = executor.submit(clockRun);

            List<Future<Boolean>> futures = new ArrayList<>();
            for (Map<String, Object> wk : workflows){
                int finalFlowIndex = flowIndex;

                Map<String, Object> results = new HashMap<>();
                multiFlowResult.add(results);

                /*Extract the workflow details*/
                String c = (String) wk.get("classifier");
                String a = (String) wk.get("attributeSelection");
                //-- Execution
                Trainer trainer = Trainer.getSingleton();
                boolean compiledResult = trainer.generateProblemFile(trainingset, c, a);
                if (compiledResult) {
                    //-- Generate the plan
                    DMPlan plan = trainer.generatePlan();
                    File f = new File("diabetes-engine/out.single." + finalFlowIndex + ".kf");
                    PlanTranslator planTranslator = new PlanTranslator(seed, filePath);
                    planTranslator.setTheComponent(plan);
                    String json = planTranslator.translate();
                    FileWriter fw = new FileWriter(f, false);
                    fw.write(json);
                    fw.close();

                    Callable flowCallable = () -> {
                        JSONFlowLoader loader = new JSONFlowLoader();
                        Flow flow = null;
                        try {
                            flow = loader.readFlow(f);
                        } catch (WekaException e) {
                            e.printStackTrace();
                        }
                        BaseExecutionEnvironment execE = new BaseExecutionEnvironment();
                        FlowExecutor flowExecutor = execE.getDefaultFlowExecutor();
                        flowExecutor.setFlow(flow);
                        try {
                            flowExecutor.runSequentially();
                        } catch (WekaException e) {
                            e.printStackTrace();
                        }
                        flowExecutor.waitUntilFinished();

                        int lastIndx = plan.getOperators().size() - 2; //-- the index of ClassifierEvaluation component
                        DMOperator lastOp = plan.getOperators().get(lastIndx);
                        StepManager stepManager = flow.findStep(lastOp.getName());
                        ClassifierPerformanceEvaluator eval = (ClassifierPerformanceEvaluator)stepManager.getManagedStep();

                        results.put("classifier", c);
                        results.put("attributeSelection", a);
                        results.put("seed", seed);
                        if (eval.getM_eval() != null) {
                            results.put("weightedAreaUnderROC", eval.getM_eval().weightedAreaUnderROC());
                            results.put("weightedFMeasure", eval.getM_eval().weightedFMeasure());
                            results.put("weightedPrecision", eval.getM_eval().weightedPrecision());
                            results.put("weightedRecall", eval.getM_eval().weightedRecall());
                            results.put("errorRate", eval.getM_eval().errorRate());
                            logger.info(eval.getM_eval().errorRate() + "==================");
                        }
                        return true;
                    };
                    Future<Boolean> future = executor.submit(flowCallable);
                    futures.add(future);
                    Trainer.reset();
                } else {
                    Log.warn("ExML1", "Compilation of problem.java failed!");
                }
                flowIndex++;
            }

            boolean isOneDone = false;
            while(!isOneDone){
                for(Future<Boolean> future : futures){
                    if (!isOneDone){
                        isOneDone = future.isDone();
                    }
                }
            }
            boolean clockDone = false;
            if (isOneDone) {
                while (!clockDone){
                    boolean allDone = true;
                    clockDone = clockFuture.isDone();
                    for(Future<Boolean> future : futures){
                        allDone &= future.isDone();
                    }
                    if (allDone)
                        clockDone = true;
                }
            }
            logger.info( "Shutdown now!!");
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.NANOSECONDS);

            //-- Get the best flow based on AUC/ErrorRate
            double bestErrorRate= 1;
            int bestFlowIndex = -1;
            int j = 0;
            for (Map<String, Object> r : multiFlowResult){
                if (r.get("errorRate") != null) {
                    double errorRate = (Double)r.get("errorRate");
                    if (bestErrorRate > errorRate){
                        bestErrorRate = errorRate;
                        bestFlowIndex = j;
                    }
                }
                j++;
            }

            Map<String, Object> bestFlowResult = multiFlowResult.get(bestFlowIndex);

            //-- Optimize the best wf and retrieve the result
            String c = (String)bestFlowResult.get("classifier");
            String a = (String)bestFlowResult.get("attributeSelection");
            logger.info(a + "/" + c);
            logger.info(filePath);

            Trainer.getSingleton().trainModelForSPARQL(
                    trainingArff,
                    modelName,
                    c,
                    a,
                    seed);

            boolean everythingIsDone = false;
            while (!everythingIsDone){
                everythingIsDone = true;
                for(Future<Boolean> future : futures){
                    everythingIsDone &= future.get();
                }
            }
            logger.info("=========================================================================================");
        } catch (Exception qpe){
            logger.error(qpe.getMessage());
        }
        return false;
    }
    /**
     * Execute ASK query, retrieve data and write to file.
     *
     * @param q
     * @return
     */
    public boolean execAskQuery(String q){
        try {
            Query query = QueryFactory.create(q);
            logger.info("Print the query: ");
            query.serialize(new IndentedWriter(System.out,true)) ;
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

            // Execute and write result to file
            boolean rs = qexec.execAsk();
            String result = prepareResult(rs);
            return writeResult(result);
        } catch (QueryParseException qpe){
            logger.error(qpe.getMessage());
        }
        return false;
    }

    /**
     * Execute SELECT query, retrieve data and write to file.
     *
     * @param q
     */
    public boolean execSelectQuery(String q){
        try {
            Query query = QueryFactory.create(q);
            logger.info("Print the query: ");
            query.serialize(new IndentedWriter(System.out,true)) ;
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

            // Execute and write result to file
            ResultSet rs = qexec.execSelect();
            String result = prepareResult(rs);
            return writeResult(result);
        } catch (QueryParseException qpe){
            logger.error(qpe.getMessage());
        }
        return false;
    }

    /**
     * Prepare result for boolean type.
     * @param result
     * @return
     */
    private String prepareResult(boolean result){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Result").append("\n");
        stringBuilder.append(result);
        return stringBuilder.toString();
    }

    /**
     * Prepare result for ResultSet type.
     *
     * @param rs ResultSet
     */
    private String prepareResult(ResultSet rs) {
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(rs);
        int numCols = resultSetRewindable.getResultVars().size();
        StringBuilder header = new StringBuilder();
        StringBuilder row = new StringBuilder();
        for (int r = 0; resultSetRewindable.hasNext(); r++) {
            QuerySolution rBind = resultSetRewindable.nextSolution();
            for (int col = 0; col < numCols; col++) {
                String rVar = rs.getResultVars().get(col);
                // Print col headers
                if (r == 0) {
                    if (col < numCols - 1)
                        header.append(rVar).append("\t");
                    else
                        header.append(rVar).append("\n");
                }
                // Print row
                RDFNode obj = rBind.get(rVar);
                String v = FmtUtils.stringForRDFNode(obj);
                if (col < numCols - 1)
                    row.append(v).append("\t");
                else
                    row.append(v).append("\n");
            }
        }
        return header.toString() + row.toString();
    }

    /**
     * Write result to csv file.
     *
     * @param result
     * @return
     */
    private boolean writeResult(String result){
        if (!result.isEmpty()) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(
                        trainingCsv,
                        false);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(result);
            out.close();
            return true;
        }
        return false;

    }

    /**
     * Populate the table with data retrieve from urSpec.
     *
     * @param table
     * @param urlSpec
     * @param hasHeader
     */
    public void populateTable(
            final TableView<ObservableList<StringProperty>> table,
            final String urlSpec, final boolean hasHeader) {
        table.getItems().clear();
        table.getColumns().clear();
        table.setPlaceholder(new Label("Loading..."));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                BufferedReader in = new BufferedReader(new FileReader(trainingCsv));
                // Header line
                if (hasHeader) {
                    final String headerLine = in.readLine();
                    final String[] headerValues = headerLine.split("\t");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int column = 0; column < headerValues.length; column++) {
                                String colName = headerValues[column];
                                colName = colName.substring(0, 1).toUpperCase() + colName.substring(1);
                                table.getColumns().add(
                                        createColumn(column, colName));
                            }
                        }
                    });
                }

                // Data:
                String dataLine;
                while ((dataLine = in.readLine()) != null) {
                    final String[] dataValues = dataLine.split("\t");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Add additional columns if necessary:
                            for (int columnIndex = table.getColumns().size(); columnIndex < dataValues.length; columnIndex++) {
                                table.getColumns().add(createColumn(columnIndex, ""));
                            }
                            // Add data to table:
                            ObservableList<StringProperty> data = FXCollections
                                    .observableArrayList();
                            for (String value : dataValues) {
                                data.add(new SimpleStringProperty(value));
                            }
                            table.getItems().add(data);
                        }
                    });
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private TableColumn<ObservableList<StringProperty>, String> createColumn(
            final int columnIndex, String columnTitle) {
        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
        String title;
        if (columnTitle == null || columnTitle.trim().length() == 0) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);
        column
                .setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
                        ObservableList<StringProperty> values = cellDataFeatures.getValue();
                        if (columnIndex >= values.size()) {
                            return new SimpleStringProperty("");
                        } else {
                            return cellDataFeatures.getValue().get(columnIndex);
                        }
                    }
                });
        return column;
    }

    public static double[] normalize(double[] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < mf.length; j++){
            if (j == mf.length - 1)
                stringBuilder.append(mf[j]);
            else
                stringBuilder.append(mf[j]).append(",");
        }
        stringBuilder.append("\n");

        FileWriter fw = new FileWriter(
                "resources/R-utilities/new.item.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();

        /*Normalize by R*/
        logger.info( "Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.test.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.new.item.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[] normalizedMf = new double[mf.length];
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf.length; col++){
                normalizedMf[col] = Double.parseDouble(line[col]);
            }
        }
        return normalizedMf;
    }
    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime()
                .exec(command, null, new File("resources/R-utilities/"));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info( command + " exitValue() " + pro.exitValue());
    }
    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info( name + " " + line);
        }
    }
}