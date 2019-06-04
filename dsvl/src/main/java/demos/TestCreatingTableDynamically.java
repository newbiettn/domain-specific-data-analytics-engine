package demos;

import common.ProjectPropertiesGetter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-13
 */
public class TestCreatingTableDynamically extends Application {
    static ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    static String  filePath = propGetter.getProperty("sparqlml.training.data.filepath");
    static String trainingCsv = filePath + "sparql_data_tmp.csv";

    @Override
    public void start(Stage stage) {
        retrieveData();
        final BorderPane root = new BorderPane();
        final TableView<ObservableList<StringProperty>> table = new TableView<>();
        final TextField urlTextEntry = new TextField();
        urlTextEntry.setPromptText("Enter URL of tab delimited file");
        final CheckBox headerCheckBox = new CheckBox("Data has header line");
        urlTextEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                populateTable(table, urlTextEntry.getText(),
                        true);
            }
        });
        HBox controls = new HBox();
        controls.getChildren().addAll(urlTextEntry, headerCheckBox);
        HBox.setHgrow(urlTextEntry, Priority.ALWAYS);
        HBox.setHgrow(headerCheckBox, Priority.NEVER);
        root.setTop(controls);
        root.setCenter(table);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();

    }

    public static void retrieveData(){
        String selectQuery = "SELECT ?subject ?predicate ?object " +
                "WHERE {" +
                "  ?subject ?predicate ?object" +
                "}" +
                "LIMIT 25";
        Query query = QueryFactory.create(selectQuery);
        query.serialize(new IndentedWriter(System.out,true)) ;

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", query);
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
//        System.out.print(row.toString());
        // Save to CSV
        FileWriter fw = null;
        try {
            fw = new FileWriter(
                    trainingCsv,
                    false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(header.toString() + row.toString());
        out.close();
    }

    private void populateTable(
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
                                String header = headerValues[column];
                                if (header.equals("subject")) {
                                    table.getColumns().add(
                                            createColumn(column, headerValues[column]));

                                } else {
                                    table.getColumns().add(
                                            createColumn(column, headerValues[column]));
                                }
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
                            getHostServices().showDocument(hyperlink.getText());
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

}
