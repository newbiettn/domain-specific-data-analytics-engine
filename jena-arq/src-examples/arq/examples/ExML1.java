/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arq.examples;

// The ARQ application API.
import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import model.DMOperator;
import model.DMPlan;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.* ;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.graph.Node;
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
import java.util.*;
import java.util.concurrent.*;

/** Example 1 : Execute a simple SELECT query on a model
 *  to find the DC titles contained in a model. */

public class ExML1
{
    static public final String NL = System.getProperty("line.separator") ;
    private static Logger logger = LoggerFactory.getLogger(ExML1.class);
    
    public static void main(String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        File output = new File("output.txt");
        String datasetName = "tmp_sparql_dataset";
        String filePath = propGetter.getProperty("sparqml.resource.filepath");
        int seed = 1;
        MetafeatureGenerator mfGen = new MetafeatureGenerator();
        DbUtils dbUtils = new DbUtils();
        int clockTimeout = 30000;
        String trainingCsv = filePath + "sparql_data_tmp.csv";
        String trainingArff = filePath + "sparql_data_tmp.arff";

        // Create the data.
        // First part or the query string
        String prolog = "PREFIX : <file:///Users/newbiettn/Downloads/d2rq-0.8.1/mapping.nt#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX vocab: <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/>" ;

        // ML query string.
        String queryString = prolog + NL +
                "CREATE PREDICTION MODEL ?m " +
                "TARGET ?publish " +
                "WHERE {" +
                "?publish FEATURE vocab:papers_Publish." +
                "?title FEATURE vocab:papers_Title." +
                "?year FEATURE vocab:papers_Year." +
                "}";
        MLQuery q = MLQueryFactory.create(queryString);
        LinkedHashMap<Var, Node> cpmWhereVars = q.getCPMWhereVars();
        Var tVar = q.getCPMTarget();
        Var mVar = q.getCPMName();
        String modelName = mVar.getVarName();

        StringBuilder whereStr = new StringBuilder();
        StringBuilder selectStr = new StringBuilder();
        for (Map.Entry<Var, Node> e : cpmWhereVars.entrySet()){
            Var v = e.getKey();
            Node n = e.getValue();
            selectStr = selectStr.append("?").append(v.getVarName()).append(" ");
            whereStr = whereStr.append("?s ").append("<").append(n.getURI()).append(">").append(" ").append("?").append(v.getVarName()).append(".").append(NL);
        }

        logger.info("Successfully parse MLQuery");
        // Create SELECT query
        String selectQuery = prolog + NL +
                "SELECT " + selectStr.toString() +
                " WHERE { " + NL +
                whereStr.toString() +
                " }";

        Query query = QueryFactory.create(selectQuery);
        logger.info( "Select query");
//        query.serialize(new IndentedWriter(System.out,true)) ;

        // Gather as a dataset for further ML execution
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/D2/query", query) ) {
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
                    String v = FmtUtils.stringForRDFNode(obj);
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

        File trainingset = new File("resources/sparqml/anneal.arff");
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
        String trainingFileUrl = filePath + "anneal.arff";
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

//                double bestAUC = 0;
//                int bestFlowIndex = -1;
//                int j = 0;
//                for (Map<String, Object> r : multiFlowResult){
//                    if (r.get("weightedAreaUnderROC") != null) {
//                        double weightedAreaUnderROC = (Double)r.get("weightedAreaUnderROC");
//                        if (bestAUC < weightedAreaUnderROC){
//                            bestAUC = weightedAreaUnderROC;
//                            bestFlowIndex = j;
//                        }
//                    }
//                    j++;
//                }
        Map<String, Object> bestFlowResult = multiFlowResult.get(bestFlowIndex);

        //-- Optimize the best wf and retrieve the result
        String c = (String)bestFlowResult.get("classifier");
        String a = (String)bestFlowResult.get("attributeSelection");
        logger.info(a + "/" + c);

        Trainer.getSingleton().trainModelForSPARQL(
                datasetName,
                trainingFileUrl,
                modelName,
                c,
                a,
                seed,
                filePath);

        boolean everythingIsDone = false;
        while (!everythingIsDone){
            everythingIsDone = true;
            for(Future<Boolean> future : futures){
                everythingIsDone &= future.get();
            }
        }
        logger.info("=========================================================================================");
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
