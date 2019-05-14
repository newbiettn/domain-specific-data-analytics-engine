package service;

import common.ProjectPropertiesGetter;
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
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Services for MainController.
 *
 * @author Ngoc Tran
 * @since 2019-05-13
 */
public class MainControllerService {
    private static Logger logger = LoggerFactory.getLogger(MainControllerService.class);
    ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    String  filePath = propGetter.getProperty("sparqlml.tmp.data.filepath");
    String trainingCsv = filePath + "sparql_data_tmp.csv";
    String sparqlEndpoint = "http://localhost:3030/austin/query";

    /**
     * Execute query, retrieve data and write to file.
     *
     * @param q
     */
    public boolean executeQuery(String q){
        try {
            String prolog = "PREFIX diab: <http://www.semanticweb.org/newbiettn/ontologies/2017/11/diabetes_inpatient_study#>\n" +
                    "PREFIX : <http://localhost:2020/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
            String fullQuery = prolog + "\n" + q;
            Query query = QueryFactory.create(fullQuery);
            query.serialize(new IndentedWriter(System.out,true)) ;
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute and write result to file
            ResultSet rs = qexec.execSelect();
            return writeResult(rs);
        } catch (QueryParseException qpe){
            logger.error(qpe.getMessage());
        }
        return false;
    }

    /**
     * Write ResultSet to file in form of a csv.
     *
     * @param rs ResultSet
     */
    private boolean writeResult(ResultSet rs){
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
                        header.append(rVar).append("\t");
                    else
                        header.append(rVar).append("\n");
                }
                // Print row
                RDFNode obj = rBind.get(rVar);
                String v = FmtUtils.stringForRDFNode(obj);
                if (col < numCols-1)
                    row.append(v).append("\t");
                else
                    row.append(v).append("\n");
            }
        }

        // Save to CSV
        if (!row.toString().isEmpty()) {
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
            out.print(header.toString() + row.toString());
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
}
