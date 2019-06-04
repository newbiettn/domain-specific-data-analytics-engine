package service;

import common.ProjectPropertiesGetter;
import config.Project;
import config.Prolog;
import controllers.Configuration;
import eu.mihosoft.vrl.workflow.VFlow;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.util.Callback;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.util.FmtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.ParseTree;
import planner.Trainer;
import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Services for MainController.
 *
 * @author Ngoc Tran
 * @since 2019-05-13
 */
public class MainControllerService {
    private static Logger logger = LoggerFactory.getLogger(MainControllerService.class);
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    private String sparqlEndpoint;
    private VFlow  flow;
    private Project projectConfig;

    private String testDataFilepath;
    private String trainingDataFilepath;
    private String processFilepath;
    private String trainingArffFilename;
    private String testArffFilename;
    private String resultFilename;
    private String processFilename;

    public MainControllerService(){
        projectConfig = Configuration.getSingleton().getProject();
        sparqlEndpoint = projectConfig.getEndpoint().getUri();
        resultFilename = propGetter.getProperty("sparqlml.result.filepath") + "result.csv";
        testDataFilepath = propGetter.getProperty("sparqlml.test.data.filepath");
        trainingDataFilepath = propGetter.getProperty("sparqlml.training.data.filepath");
        processFilepath = propGetter.getProperty("sparqlml.dm.process.filepath");
        testArffFilename = testDataFilepath + "test_dataset.arff";
    }

    /**
     * Wrapper method for execute all kinds of query.
     *
     * @return
     */
    public boolean execQuery(){
        ParseTree pt = new ParseTree();
        int type = pt.parse(flow); // Parse the flow to parse tree
        logger.info("Parse tree type: " + type);
        if (type != ParseTree.INVALID_TREE){
            String sparqlQuery = pt.interpret(); // Interpret to appropriate SPARQL query
            logger.info("The parsed query: \n" + sparqlQuery);
            if (pt.interpret() != null){
                boolean execResult = false;
                if (type == ParseTree.SELECT_TREE)
                    execResult = execSelectQuery(sparqlQuery);
                else if (type == ParseTree.PREVALENCE_TREE)
                    execResult = execSelectQuery(sparqlQuery);
                else if (type == ParseTree.ASK_TREE)
                    execResult = execAskQuery(sparqlQuery);
                else if (type == ParseTree.CREATEPREDICTIONMODEL_TREE)
                    execResult = execCreatePredictionModelQuery(sparqlQuery);
                else if (type == ParseTree.PREDICT_TREE)
                    execResult = execPredictQuery(sparqlQuery);
                if (execResult) { // Run query to retrieve data
                    logger.info("Populating the table...");
                    return true;
                } else {
                    logger.info("Retrieved no data");
                    return false;
                }
            }
        } else {
            logger.info("Invalid parsing tree");
            return false;
        }
        return false;
    }

    /**
     * To execute PREDICT query.
     *
     * @param queryString
     * @return
     */
    public boolean execPredictQuery(String queryString) {
        // Create the data.
        MLQuery q = MLQueryFactory.create(queryString);
        ArrayList<Var> featureVars = q.getFeatureVars();
        Var tVar = q.setTargetName();
        String modelName = q.getModelFileName();
        ElementFilter filterEl = (ElementFilter)q.getFilterEle();

        // Create SELECT query
        Query selectQuery = QueryFactory.make() ;
        selectQuery.setQuerySelectType() ;
        selectQuery.setQueryPattern(q.getQueryPattern());
        for (Prolog p : projectConfig.getPrologs()){ // Set PREFIX
            selectQuery.getPrefixMapping().setNsPrefix(p.getPrefix(), p.getUri()) ;
        }
        selectQuery.addResultVar(Var.alloc(tVar.getVarName()) ); // Result variable
        for (Var v: featureVars){
            Var var = Var.alloc(v.getVarName()) ;
            selectQuery.addResultVar(var);
        }
        selectQuery.serialize(new IndentedWriter(System.out,true)) ;

        // Gather as a dataset for further ML execution
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, selectQuery) ) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            ResultSet rs = qexec.execSelect();
            String selectResult = convertResultSet(rs);

            // Create Instances type from string
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(new ByteArrayInputStream(selectResult.getBytes()));
            Instances data = csvLoader.getDataSet();

            // Put the target attribute to the last position to manage easier
            Attribute tAttr = data.attribute(tVar.getVarName());
            int tIndex = tAttr.index() + 1;
            StringBuilder attrIds = new StringBuilder();
            for (int i = 1; i <= data.numAttributes(); i++) {
                if (i != tIndex) {
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
            saver.setFile(new File(testArffFilename));
            saver.writeBatch();

            if (compareTestAndTraining()){ // Verify if training & test sets are compatible
                // Predicting
                processFilename = processFilepath + "process_" + modelName + ".kf";
                Instances instanceResult = Trainer.getSingleton().predictForSPARQL(processFilename, testArffFilename);
                int numCols = instanceResult.numAttributes();
                for (int col = 0; col < numCols; col++){
                    Attribute attribute = instanceResult.attribute(col);
                    String attributeName = attribute.name();
                    String attributeNewName = null;
                    if (col < numCols - 3){
                        attributeNewName = attributeName.substring(0,1).toUpperCase() + attributeName.substring(1).toLowerCase();
                    } else {
                        if (col == numCols - 3) {
                            attributeNewName = attributeName + " (Actual)";
                        } else if (col == numCols - 2) {
                            attributeNewName =  "False Probability (Predicted)";
                        } else if (col == numCols - 1){
                            attributeNewName =  "True Probability (Predicted)";
                        }
                    }
                    instanceResult.renameAttribute(col, attributeNewName);
                }
                CSVSaver csvSaver = new CSVSaver();
                csvSaver.setFieldSeparator(",");
                csvSaver.setFile(new File(resultFilename));
                csvSaver.setInstances(instanceResult);
                csvSaver.writeBatch();
                return true;
            } else {
                logger.warn("Training and test sets are not compatible");
                return false;
            }

        } catch (Exception qpe){
            qpe.printStackTrace();
        }
        return false;
    }

    /**
     * To execute Create ML Prediction query.
     *
     * @param queryString
     * @return
     */
    public boolean execCreatePredictionModelQuery(String queryString) {
        try {
            // Create the data.
            MLQuery q = MLQueryFactory.create(queryString);
            ArrayList<Var> featureVars = q.getFeatureVars();
            Var tVar = q.setTargetName();
            String modelName = q.getModelFileName();

            Query selectQuery = QueryFactory.make() ;
            selectQuery.setQuerySelectType() ;
            selectQuery.setQueryPattern(q.getQueryPattern());
            for (Prolog p : projectConfig.getPrologs()){ // Set PREFIX
                selectQuery.getPrefixMapping().setNsPrefix(p.getPrefix(), p.getUri()) ;
            }
            selectQuery.addResultVar(Var.alloc(tVar.getVarName()) );
            for (Var v: featureVars){
                Var var = Var.alloc(v.getVarName()) ;
                selectQuery.addResultVar(var);
            }
            logger.info("SELECT query is as follow: ");
            selectQuery.serialize(new IndentedWriter(System.out,true)) ;

            try ( QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, selectQuery) ) {
                // Set the DBpedia specific timeout.
                ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

                // Execute select query and store result in string with CSV format
                ResultSet rs = qexec.execSelect();
                String result = convertResultSet(rs);

                // Create Instances type from string
                CSVLoader csvLoader = new CSVLoader();
                csvLoader.setSource(new ByteArrayInputStream(result.getBytes()));
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

                // Store training file for further prediction
                trainingArffFilename = trainingDataFilepath + "training_" + modelName + ".arff";
                ArffSaver saver = new ArffSaver();
                saver.setInstances(data);
                saver.setFile(new File(trainingArffFilename));
                saver.writeBatch();

                Trainer.getSingleton().executeForSPARQLML(trainingArffFilename, modelName);


            } catch (Exception e) {
                e.printStackTrace();
            }
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
        Query query = QueryFactory.create(q);
        logger.info("Print the query: ");
        query.serialize(new IndentedWriter(System.out,false)) ;

        try(QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query)){
            ((QueryEngineHTTP)qexec).addParam("timeout", "1000") ;

            // Execute and write result to file
            logger.info("Executing select...");
            ResultSet rs = qexec.execSelect();
            String result = prepareResult(rs);
            return writeResult(result);
        } catch (Exception e){
            logger.error(e.getMessage());
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
                        resultFilename,
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
                        header.append(rVar).append(",");
                    else
                        header.append(rVar).append("\n");
                }
                // Print row
                RDFNode obj = rBind.get(rVar);
                String v = FmtUtils.stringForRDFNode(obj);
                if (col < numCols - 1)
                    row.append(v).append(",");
                else
                    row.append(v).append("\n");
            }
        }
        return header.toString() + row.toString();
    }

    public void emptyTable(final TableView<ObservableList<StringProperty>> table) {
        table.getItems().clear();
        table.getColumns().clear();
        table.setPlaceholder(new Label("There is no retrieved data for the exected query"));
    }

    /**
     * Populate the table with data retrieve from urSpec.
     *
     * @param table
     * @param hasHeader
     */
    public void populateTable(
            final TableView<ObservableList<StringProperty>> table,
            final boolean hasHeader) {
        table.getItems().clear();
        table.getColumns().clear();
        table.setPlaceholder(new Label("Loading..."));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                BufferedReader in = new BufferedReader(new FileReader(resultFilename));
                // Header line
                if (hasHeader) {
                    final String headerLine = in.readLine();
                    final String[] headerValues = headerLine.split(",");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int column = 0; column < headerValues.length; column++) {
                                String colName = headerValues[column];
                                colName = colName.substring(0, 1).toUpperCase() + colName.substring(1);
                                if (colName.equals("Patient") || colName.equals("Episode") || colName.equals("AdmissionReport")) {
                                    table.getColumns().add(
                                            createColumnForHyperLinkType(column, colName));
                                } else {
                                    table.getColumns().add(
                                            createColumn(column, colName));
                                }
                            }
                        }
                    });
                }

                // Data:
                String dataLine;
                while ((dataLine = in.readLine()) != null) {
                    final String[] dataValues = dataLine.split(",");
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


    /**
     * Create cols for table.
     *
     * @param columnIndex
     * @param columnTitle
     * @return
     */
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

    private TableColumn<ObservableList<StringProperty>, String> createColumnForHyperLinkType(
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
        Callback<TableColumn<ObservableList<StringProperty>, String>, TableCell<ObservableList<StringProperty>, String>> cellFactory0
                = (final TableColumn<ObservableList<StringProperty>, String> entry) -> {
            final TableCell<ObservableList<StringProperty>, String> cell = new TableCell<ObservableList<StringProperty>, String>()
            {

                Hyperlink hyperlink = new Hyperlink();

                @Override
                public void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        System.out.println("set hyperlink");
                        item = item.substring(1, item.length()-1);
                        hyperlink.setText(item);
                        hyperlink.setOnAction((event) -> {
                            System.out.println("Go to URL");
                            try {
                                Desktop.getDesktop().browse(new URL(hyperlink.getText()).toURI());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        });
                        setGraphic(hyperlink);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        column.setCellFactory(cellFactory0);
        return column;
    }

    /**
     * Convert ResultSet to a string of CSV format for further manipulation.
     *
     * @param rs
     * @return
     */
    public String convertResultSet(ResultSet rs){
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
        return (header.toString() + row.toString());
    }

    /**
     * Checking if the structure of test dataset identical to of training set.
     *
     * @return
     */
    private boolean compareTestAndTraining(){
        ArffLoader arffLoader = new ArffLoader();
        try {
            arffLoader.setSource(new File(testArffFilename));
            Instances testInstances = arffLoader.getDataSet();
            testInstances.setClassIndex(testInstances.numAttributes() - 1);
            arffLoader.setSource(new File(trainingArffFilename));
            Instances trainingInstances = arffLoader.getDataSet();
            trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);

            // Checking num attributes
            if (testInstances.numAttributes() != trainingInstances.numAttributes()){
                logger.warn("Training and test sets have different number of attributes");
                return false;
            }

            // Checking target attribute
            if (!testInstances.classAttribute().name().equals(trainingInstances.classAttribute().name())){
                logger.warn("Training and test sets have different target attributes");
                return false;
            }

            // Compare names of attribute of both after sorting them
            int numAttr = trainingInstances.numAttributes();
            List<String> testAttNames = new ArrayList<>();
            List<String> trainingAttName = new ArrayList<>();
            List<String> originalTestAttName = new ArrayList<>();
            for (int i = 0; i < numAttr-1; i++){ // except class attribute
                testAttNames.add(testInstances.attribute(i).name());
                originalTestAttName.add(testInstances.attribute(i).name());
                trainingAttName.add(trainingInstances.attribute(i).name());
            }
            Collections.sort(testAttNames);
            Collections.sort(trainingAttName);
            String[] newIndex = new String[numAttr];
            for (int k = 0; k < numAttr-1; k++) {
                 newIndex[k]= String.valueOf(testAttNames.indexOf(originalTestAttName.get(k)) + 1);
            }
            newIndex[numAttr-1] = "last";

            if (!testAttNames.equals(trainingAttName)) {
                logger.warn("Training and test sets have different attributes");
                return false;
            } else { // ensure both have attributes in the same order
                Reorder reorder = new Reorder();
                String attributeOrder = String.join(",", newIndex);
                reorder.setAttributeIndices(attributeOrder);
                reorder.setInputFormat(testInstances);
                testInstances = Filter.useFilter(testInstances, reorder);

                ArffSaver saver = new ArffSaver();
                saver.setInstances(testInstances);
                saver.setFile(new File(testArffFilename));
                saver.writeBatch();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public VFlow getFlow() {
        return flow;
    }

    public void setFlow(VFlow flow) {
        this.flow = flow;
    }

}