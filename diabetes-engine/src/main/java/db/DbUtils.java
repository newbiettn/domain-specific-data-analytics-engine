package db;

import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Instance;
import weka.core.SparseInstance;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author newbiettn on 5/7/18
 * @project DiabetesDiscoveryV2
 */

public class DbUtils {
    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);
    private String URL = ProjectPropertiesGetter.getSingleton().getProperty("h2.db.url");
    private Connection conn;

    public DbUtils() {
        try {
            this.conn = DriverManager.getConnection(this.URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info(name + " " + line);
        }
    }

    private void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime()
                .exec(command, null, new File(ProjectPropertiesGetter.getSingleton().getProperty("R.utilities")));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info(command + " exitValue() " + pro.exitValue());
    }

    /**
     * export all evaluation experiments to CSV
     */
    public void exportEvaluationExperimentsToCSV() {
        String selectQuery = "SELECT * FROM EVALUATION_EXPERIMENTS";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int rowIndex = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                if (rowIndex == 0) {
                    for (int j = 1; j <= columnsNumber; j++) {
                        if (j == columnsNumber)
                            stringBuilder.append(rsmd.getColumnName(j));
                        else
                            stringBuilder.append(rsmd.getColumnName(j)).append(",");
                    }
                    stringBuilder.append("\n");
                }

                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) stringBuilder.append(",");
                    String columnValue = resultSet.getString(i);
                    stringBuilder.append(columnValue);
                    if (i == columnsNumber) {
                        stringBuilder.append("\n");
                    }
                }
                rowIndex++;
            }

            FileWriter fw = new FileWriter(
                    ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "evaluation_experiment.csv",
                    false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(stringBuilder.toString());
            out.close();
            logger.info("Export evaluation experiments to CSV");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * display SELECT * FROM EVALUATION_EXPERIMENTS
     */
    public void viewAllEvaluationExperiments() {
        String selectQuery = "SELECT * FROM EVALUATION_EXPERIMENTS";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("================================== SELECT * FROM EVALUATION_EXPERIMENTS ================================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END ============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean dropEvaluationExperiment(){
        String dropQuery = "DROP TABLE IF EXISTS EVALUATION_EXPERIMENTS";
        PreparedStatement dropPreparedStatement = null;
        try {
            dropPreparedStatement = conn.prepareStatement(dropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


    /**
     * This table to store evaluation experiments
     *
     * @param record
     * @return
     */
    public boolean insertNewEvaluationExperiment(Map<String, Object> record){
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;

        String CreateQuery = "CREATE TABLE IF NOT EXISTS EVALUATION_EXPERIMENTS" +
                "(id int AUTO_INCREMENT, " +
                "classifier varchar(255)," +
                "attributeSelection varchar(255)," +
                "seed double," +
                "dataset varchar(255)," +
                "flow varchar(255)," +
                "rank int," +
                "cluster int," +
                "weightedAreaUnderROC double," +
                "weightedFMeasure double," +
                "weightedPrecision double," +
                "weightedRecall double," +
                "errorRate double," +
                "timeElapsed double)";
        String InsertQuery = "INSERT INTO EVALUATION_EXPERIMENTS" +
                "(classifier, attributeSelection, seed, dataset, flow, rank," +
                "cluster, weightedAreaUnderROC, weightedFMeasure, weightedPrecision," +
                "weightedRecall, errorRate, timeElapsed) values" +
                "(?, ?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            createPreparedStatement = conn.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = conn.prepareStatement(InsertQuery);
            String classifier = (String) record.get("classifier");
            String attributeSelection = (String) record.get("attributeSelection");
            double seed = (Double) record.get("seed");
            String dataset = (String) record.get("dataset");
            String flow = (String) record.get("flow");
            int rank = (Integer) record.get("rank");
            int cluster = (Integer) record.get("cluster");
            double weightedAreaUnderROC = (Double) record.get("weightedAreaUnderROC");
            double weightedFMeasure = (Double) record.get("weightedFMeasure");
            double weightedPrecision = (Double) record.get("weightedPrecision");
            double weightedRecall = (Double) record.get("weightedRecall");
            double errorRate = (Double) record.get("errorRate");
            double timeElapsed = (Double) record.get("timeElapsed");

            insertPreparedStatement.setString(1, classifier);
            insertPreparedStatement.setString(2, attributeSelection);
            insertPreparedStatement.setDouble(3, seed);
            insertPreparedStatement.setString(4, dataset);
            insertPreparedStatement.setString(5, flow);
            insertPreparedStatement.setInt(6, rank);
            insertPreparedStatement.setInt(7, cluster);
            insertPreparedStatement.setDouble(8, weightedAreaUnderROC);
            insertPreparedStatement.setDouble(9, weightedFMeasure);
            insertPreparedStatement.setDouble(10, weightedPrecision);
            insertPreparedStatement.setDouble(11, weightedRecall);
            insertPreparedStatement.setDouble(12, errorRate);
            insertPreparedStatement.setDouble(13, timeElapsed);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            conn.commit();
            logger.info("Insert record to db successfully!");

            return true;
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the top N workflow (classifier, attributeSelection & rank) by the cluster.
     *
     * @param cluster (starts from 1)
     */
    public List<Map<String, Object>> getWorkflowOfDatasetByCluster(int cluster) {
        String selectQuery = "SELECT TOP 3 classifier, attributeSelection, rank " +
                "FROM BEST_FLOW_BY_CLUSTER " +
                "WHERE cluster = ? " +
                "ORDER BY rank ASC";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setInt(1, cluster);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            List<Map<String, Object>> l = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> r = new HashMap<>();
                r.put("classifier", resultSet.getString(1));
                r.put("attributeSelection", resultSet.getString(2));
                r.put("rank", resultSet.getInt(3));
                l.add(r);
            }
            return l;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void viewAllBestWorkflowByCluster(){
        String selectQuery = "SELECT * FROM BEST_FLOW_BY_CLUSTER";
        PreparedStatement selectPreparedStatement = null;
        try{
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("=========================== SELECT * FROM BEST_FLOW_BY_CLUSTER ===============================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END =============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean dropBestWorkflowByClusterTable(){
        String dropQuery = "DROP TABLE IF EXISTS BEST_FLOW_BY_CLUSTER";
        PreparedStatement dropPreparedStatement = null;
        try {
            dropPreparedStatement = conn.prepareStatement(dropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Insert the workflow (i.e., classifier & attributeSelection algorithms)
     * and its corresponding ranking for clusters.
     *
     * @param classifier
     * @param attributeSelection
     * @param rank
     * @param cluster
     * @return true if insert successfully, otherwise false
     */
    public boolean insertBestWorkflowByCluster(String classifier,
                                               String attributeSelection,
                                               int rank,
                                               int cluster){

        String createQuery = "CREATE TABLE IF NOT EXISTS BEST_FLOW_BY_CLUSTER(" +
                "id int AUTO_INCREMENT,"+
                "classifier varchar(255)," +
                "attributeSelection varchar(255)," +
                "rank int," +
                "cluster int)";
        String insertQuery = "INSERT INTO " +
                "BEST_FLOW_BY_CLUSTER(classifier, attributeSelection, rank, cluster) " +
                "VALUES (?, ?, ?, ?)";


        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;

        try{
            createPreparedStatement = conn.prepareStatement(createQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = conn.prepareStatement(insertQuery);
            insertPreparedStatement.setString(1, classifier);
            insertPreparedStatement.setString(2, attributeSelection);
            insertPreparedStatement.setInt(3, rank);
            insertPreparedStatement.setInt(4, cluster);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * View rows in Experiments by datasetName
     *
     * @param dataset
     */
    public void viewInNonSummarizedExperimentByDatasetName(String dataset) {
        String selectQuery = "SELECT * FROM EXPERIMENTS WHERE dataset = ?";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, dataset);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("=================== SELECT * FROM EXPERIMENTS WHERE dataset = ?  ============================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END =============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove rows in Experiments by datasetName
     *
     * @param dataset
     */
    public void removeRowInNonSummarizedExperimentByDatasetName(String dataset) {
        String deleteQuery = "DELETE FROM EXPERIMENTS WHERE dataset = ?";
        PreparedStatement deletePreparedStatement = null;
        try {
            deletePreparedStatement = conn.prepareStatement(deleteQuery);
            deletePreparedStatement.setString(1, dataset);
            deletePreparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Get a list of datasets by the number of cluster.
     *
     * @param cluster
     * @return a list of datasets
     */
    public List<String> getDatasetByCluster(int cluster) {
        String selectQuery = "SELECT dataset " +
                             "FROM DATASET_CLUSTER " +
                             "WHERE cluster = ? " +
                             "ORDER BY dataset";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setInt(1, cluster);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            List<String> ds = new ArrayList<>();
            while (resultSet.next()) {
                ds.add(resultSet.getString(1));
//                logger.info(resultSet.getString(1)+"");
            }
            return ds;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * Get a list of datasets by the number of cluster.
     *
     * @return a list of datasets
     */
    public List<String> getAllDataset() {
        String selectQuery = "SELECT dataset " +
                "FROM DATASET_CLUSTER " +
                "ORDER BY dataset";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            List<String> ds = new ArrayList<>();
            while (resultSet.next()) {
                ds.add(resultSet.getString(1));
            }
            return ds;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the workflow by the index and dataset.
     *
     * @param datasetName
     * @param index (starts from 0)
     */
    public List<Map<String, String>> getWorkflowOfDataset(String datasetName) {
        String selectQuery = "SELECT ranking, flow, classifier, attributeSelection " +
                "FROM EXPERIMENT_SUMMARY " +
                "WHERE dataset = ? " +
                "ORDER BY flow DESC";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            List<Map<String, String>> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> r = new HashMap<>();
                r.put("classifier", resultSet.getString(3));
                r.put("attributeSelection", resultSet.getString(4));
                list.add(r);
//                System.out.println(resultSet.getString(3) + "/" + resultSet.getString(4));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the workflow by the index and dataset.
     *
     * @param datasetName
     * @param index (starts from 0)
     */
    public Map<String, String> getWorkflowOfDatasetByIndex(String datasetName, int index) {
        String selectQuery = "SELECT ranking, flow, classifier, attributeSelection " +
                "FROM EXPERIMENT_SUMMARY " +
                "WHERE dataset = ? " +
                "ORDER BY flow DESC";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            Map<String, String> r = new HashMap<>();
            int i = 0;
            while (resultSet.next()) {
//                System.out.println(resultSet.getString(3) + "/" + resultSet.getString(4));
                if (i == index) {
                    r.put("classifier", resultSet.getString(3));
                    r.put("attributeSelection", resultSet.getString(4));
                    break;
                }
                i++;
            }
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getWorkflow(String datasetName) {
        String selectQuery = "SELECT ranking, flow, classifier, attributeSelection " +
                "FROM EXPERIMENT_SUMMARY " +
                "WHERE dataset = ? " +
                "ORDER BY flow DESC";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                i++;
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getTotalNumberOfWorkflows(String datasetName) {
        String selectQuery = "SELECT COUNT(*) as flowcount " +
                "FROM EXPERIMENT_SUMMARY " +
                "WHERE dataset = ? ";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("flowcount");
            resultSet.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get a rank array by dataset name.
     *
     * @param datasetName
     */
    public List<Integer> getRankByDataset(String datasetName) {
        String selectQuery = "SELECT ranking, flow FROM " +
                "EXPERIMENT_SUMMARY WHERE dataset = ? ORDER BY flow DESC";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            List<Integer> ranking = new ArrayList<>();
            while (resultSet.next()) {
                ranking.add(resultSet.getInt(1));
            }
            return ranking;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Display all DATASET_CLUSTER table
     */
    public void exportAllDatasetCluster() {
        String selectQuery = "SELECT * FROM DATASET_CLUSTER";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int rowIndex = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                if (rowIndex == 0) {
                    for (int j = 1; j <= columnsNumber; j++) {
                        if (j == columnsNumber)
                            stringBuilder.append(rsmd.getColumnName(j));
                        else
                            stringBuilder.append(rsmd.getColumnName(j)).append(",");
                    }
                    stringBuilder.append("\n");
                }

                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) stringBuilder.append(",");
                    String columnValue = resultSet.getString(i);
                    stringBuilder.append(columnValue);
                    if (i == columnsNumber) {
                        stringBuilder.append("\n");
                    }
                }
                rowIndex++;
            }

            FileWriter fw = new FileWriter(
                    ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "dataset_cluster.csv",
                    false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(stringBuilder.toString());
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display all DATASET_CLUSTER table
     */
    public void viewAllDatasetCluster() {
        String selectQuery = "SELECT * FROM DATASET_CLUSTER";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("=========================== SELECT * FROM DATASET_CLUSTER ===================================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END =============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxClusterNumber() {
        String selectQuery = "SELECT MAX(cluster) FROM DATASET_CLUSTER";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                int max_cluster = resultSet.getInt(1);
                return max_cluster;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Check if the table DATASET_CLUSTER contains a specific dataset.
     *
     * @param datasetName
     * @return
     */
    public boolean isDatasetExistedInDatasetCluster(String datasetName) {
        String selectQuery = "SELECT * " +
                "FROM DATASET_CLUSTER dc " +
                "WHERE dc.dataset = ?";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dropDatasetCluster(){
        String dropQuery = "DROP TABLE IF EXISTS DATASET_CLUSTER";
        PreparedStatement dropPreparedStatement = null;
        try {
            dropPreparedStatement = conn.prepareStatement(dropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    /**
     * Insert record to DATASET_CLUSTER.
     *
     * @param record
     * @return
     */
    public boolean insertDatasetCluster(Map<String, Object> record) {
        String createQuery = "CREATE TABLE IF NOT EXISTS DATASET_CLUSTER " +
                "(id int AUTO_INCREMENT, " +
                "dataset varchar(255), " +
                "cluster int)";
        String insertQuery = "INSERT INTO DATASET_CLUSTER(dataset, cluster) " +
                "VALUES (?, ?)";

        PreparedStatement createPreparedStm = null;
        PreparedStatement insertPreparedStm = null;

        try {

            //-- Create table if not exists
            createPreparedStm = conn.prepareStatement(createQuery);
            createPreparedStm.executeUpdate();
            createPreparedStm.close();

            //-- Insert
            String dataset = (String) record.get("dataset");
            int cluster = (Integer) record.get("cluster");

            if (!isDatasetExistedInDatasetCluster(dataset)) {
                insertPreparedStm = conn.prepareStatement(insertQuery);
                insertPreparedStm.setString(1, dataset);
                insertPreparedStm.setInt(2, cluster);
                insertPreparedStm.executeUpdate();
                insertPreparedStm.close();
                conn.commit();
                return true;
            }
            conn.commit();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Retrieve a SINGLE row in DATASET_METAFEATURE.
     *
     * @return
     */
    public ResultSet getSingleRowDatasetMetafeature(String datasetName) {
        String selectQuery = "SELECT * FROM DATASET_METAFEATURE WHERE dataset = ?";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert it to double[][] to be clustered.
     *
     * @param rs ResultSet
     * @return a 2-d matrix
     * @throws SQLException
     */
    public Map<String, Object> transformDatasetMetafeature(ResultSet rs) throws SQLException {
        if (rs != null) {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, Object> m = new HashMap<>();
            int nCol = rsmd.getColumnCount();
            List<Instance> instances = new ArrayList<>();
            List<String> datasetNames = new ArrayList<>();
            while (rs.next()) {
                double[] r = new double[nCol-1];
                for (int i = 0; i < nCol; i++) {
                    if (i == 0){
                        logger.info(rs.getString(1));
                        datasetNames.add(rs.getString(i + 1));
                    }
                    else
                        r[i-1] = rs.getDouble(i + 1);
                }
                instances.add(new SparseInstance(1, r));
            }
            int nRow = instances.size();
            double[][] mt = new double[nRow][nCol-1];
            for (int k = 0; k < nRow; k++) {
                mt[k] = instances.get(k).toDoubleArray();
            }

            m.put("datasetNames", datasetNames);
            m.put("metafeatures", mt);
            return m;
        }
        return null;
    }

    /**
     * Retrieve ALL rows in DATASET_METAFEATURE.
     *
     * @return
     */
    public ResultSet getAllDatasetMetafeature() {
        String selectQuery = "SELECT Dataset, Dimensionality, NumberOfClasses, MajorityClassSize, " +
                "MinorityClassSize, NumberOfSymbolicFeatures," +
                "AutoCorrelation, NumberOfFeatures, NumberOfInstances," +
                "MinorityClassPercentage, MinNominalAttDistinctValues," +
                "PercentageOfNumericFeatures, PercentageOfBinaryFeatures, NumberOfBinaryFeatures," +
                "MaxNominalAttDistinctValues, MajorityClassPercentage," +
                "StdvNominalAttDistinctValues, PercentageOfSymbolicFeatures, MeanNominalAttDistinctValues," +
                "NumberOfNumericFeatures FROM DATASET_METAFEATURE";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return resultSet;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if a dataset exists in the metafeature db.
     *
     * @param datasetName
     * @return
     */
    public boolean isDatasetExistedInMetafeature(String datasetName) {
        String selectQuery = "SELECT * " +
                "FROM DATASET_METAFEATURE dm " +
                "WHERE dm.dataset = ?";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Display all items in DATASET_METAFEATURE.
     */
    public void viewDatasetMetafeature() {
        String selectQuery = "SELECT * FROM DATASET_METAFEATURE";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("=========================== SELECT * FROM DATASET_METAFEATURE ===============================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END =============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean dropDatasetMetafeature(){
        String dropQuery = "DROP TABLE IF EXISTS DATASET_METAFEATURE";
        PreparedStatement dropPreparedStatement = null;
        try {
            dropPreparedStatement = conn.prepareStatement(dropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Create DATASET_METAFEATURE
     */
    public boolean insertDatasetMetafeature(Map<String, Object> record) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS DATASET_METAFEATURE (id int AUTO_INCREMENT," +
                "Dataset varchar(255) PRIMARY KEY," +
                "NumberOfInstancesWithMissingValues double," +
                "Dimensionality double," +
                "NumberOfClasses double," +
                "MajorityClassSize double," +
                "MinorityClassSize double," +
                "NumberOfSymbolicFeatures double," +
                "NumberOfMissingValues double," +
                "AutoCorrelation double," +
                "NumberOfFeatures double," +
                "NumberOfInstances double," +
                "PercentageOfMissingValues double," +
                "MinorityClassPercentage double," +
                "MinNominalAttDistinctValues double," +
                "PercentageOfNumericFeatures double," +
                "PercentageOfBinaryFeatures double," +
                "NumberOfBinaryFeatures double," +
                "MaxNominalAttDistinctValues double," +
                "MajorityClassPercentage double," +
                "PercentageOfInstancesWithMissingValues double," +
                "StdvNominalAttDistinctValues double," +
                "PercentageOfSymbolicFeatures double," +
                "MeanNominalAttDistinctValues double," +
                "NumberOfNumericFeatures double)";
        String insertQuery = "INSERT INTO DATASET_METAFEATURE " +
                "(Dataset, NumberOfInstancesWithMissingValues, Dimensionality, NumberOfClasses," +
                "MajorityClassSize, MinorityClassSize, NumberOfSymbolicFeatures," +
                "NumberOfMissingValues, AutoCorrelation, NumberOfFeatures, NumberOfInstances," +
                "PercentageOfMissingValues, MinorityClassPercentage, MinNominalAttDistinctValues," +
                "PercentageOfNumericFeatures, PercentageOfBinaryFeatures, NumberOfBinaryFeatures," +
                "MaxNominalAttDistinctValues, MajorityClassPercentage, PercentageOfInstancesWithMissingValues," +
                "StdvNominalAttDistinctValues, PercentageOfSymbolicFeatures, MeanNominalAttDistinctValues," +
                "NumberOfNumericFeatures) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;

        try {
            createPreparedStatement = conn.prepareStatement(createTableQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = conn.prepareStatement(insertQuery);
            String dataset = (String) record.get("Dataset");
            double numberOfInstancesWithMissingValues = (Double) record.get("NumberOfInstancesWithMissingValues");
            double dimensionality = (Double) record.get("Dimensionality");
            double numberOfClasses = (Double) record.get("NumberOfClasses");
            double majorityClassSize = (Double) record.get("MajorityClassSize");
            double minorityClassSize = (Double) record.get("MinorityClassSize");
            double numberOfSymbolicFeatures = (Double) record.get("NumberOfSymbolicFeatures");
            double numberOfMissingValues = (Double) record.get("NumberOfMissingValues");
            double autoCorrelation = (Double) record.get("AutoCorrelation");
            double numberOfFeatures = (Double) record.get("NumberOfFeatures");
            double numberOfInstances = (Double) record.get("NumberOfInstances");
            double percentageOfMissingValues = (Double) record.get("PercentageOfMissingValues");
            double minorityClassPercentage = (Double) record.get("MinorityClassPercentage");
            double minNominalAttDistinctValues = (Double) record.get("MinNominalAttDistinctValues");
            double percentageOfNumericFeatures = (Double) record.get("PercentageOfNumericFeatures");
            double percentageOfBinaryFeatures = (Double) record.get("PercentageOfBinaryFeatures");
            double numberOfBinaryFeatures = (Double) record.get("NumberOfBinaryFeatures");
            double maxNominalAttDistinctValues = (Double) record.get("MaxNominalAttDistinctValues");
            double majorityClassPercentage = (Double) record.get("MajorityClassPercentage");
            double percentageOfInstancesWithMissingValues = (Double) record.get("PercentageOfInstancesWithMissingValues");
            double stdvNominalAttDistinctValues = (Double) record.get("StdvNominalAttDistinctValues");
            double percentageOfSymbolicFeatures = (Double) record.get("PercentageOfSymbolicFeatures");
            double meanNominalAttDistinctValues = (Double) record.get("MeanNominalAttDistinctValues");
            double numberOfNumericFeatures = (Double) record.get("NumberOfNumericFeatures");

            if (!isDatasetExistedInExperiment(dataset)) {
//                logger.info("Good news! Dataset - " + dataset + " - has been experimented before. ");
                if (!isDatasetExistedInMetafeature(dataset)) {
//                    logger.info("Dataset " + dataset + " does not exist. Begin to insert to database.");
                    insertPreparedStatement.setString(1, dataset);
                    insertPreparedStatement.setDouble(2, numberOfInstancesWithMissingValues);
                    insertPreparedStatement.setDouble(3, dimensionality);
                    insertPreparedStatement.setDouble(4, numberOfClasses);
                    insertPreparedStatement.setDouble(5, majorityClassSize);
                    insertPreparedStatement.setDouble(6, minorityClassSize);
                    insertPreparedStatement.setDouble(7, numberOfSymbolicFeatures);
                    insertPreparedStatement.setDouble(8, numberOfMissingValues);
                    insertPreparedStatement.setDouble(9, autoCorrelation);
                    insertPreparedStatement.setDouble(10, numberOfFeatures);
                    insertPreparedStatement.setDouble(11, numberOfInstances);
                    insertPreparedStatement.setDouble(12, percentageOfMissingValues);
                    insertPreparedStatement.setDouble(13, minorityClassPercentage);
                    insertPreparedStatement.setDouble(14, minNominalAttDistinctValues);
                    insertPreparedStatement.setDouble(15, percentageOfNumericFeatures);
                    insertPreparedStatement.setDouble(16, percentageOfBinaryFeatures);
                    insertPreparedStatement.setDouble(17, numberOfBinaryFeatures);
                    insertPreparedStatement.setDouble(18, maxNominalAttDistinctValues);
                    insertPreparedStatement.setDouble(19, majorityClassPercentage);
                    insertPreparedStatement.setDouble(20, percentageOfInstancesWithMissingValues);
                    insertPreparedStatement.setDouble(21, stdvNominalAttDistinctValues);
                    insertPreparedStatement.setDouble(22, percentageOfSymbolicFeatures);
                    insertPreparedStatement.setDouble(23, meanNominalAttDistinctValues);
                    insertPreparedStatement.setDouble(24, numberOfNumericFeatures);
                    insertPreparedStatement.executeUpdate();
                    insertPreparedStatement.close();
                    conn.commit();
                    return true;
                }
                logger.warn("Dataset " + dataset + "is already existed in the database metafeatures. " +
                        "No insertion is performed!");
                return false;
            } else {
                logger.warn("Dataset " + dataset + " is not existed in any experiment. " +
                        "Need to run some workflows on this dataset first!");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.warn("Something wrong!");
        return false;
    }

    public boolean isDatasetExistedInExperiment(String datasetName) {
        String selectQuery = "SELECT * FROM EXPERIMENT_SUMMARY WHERE dataset = ?";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            selectPreparedStatement.setString(1, datasetName);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Select * FROM Experiment_Summary
     */
    public void viewAllSummarizedExperimentHistoryTable() {
        String selectQuery = "SELECT * FROM EXPERIMENT_SUMMARY";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("=========================== SELECT * FROM SUMMARIZED EXPERIMENTS ============================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END =============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportSummarizedExperimentWithoutRankingTableToCSV() {
        String selectQuery =
                "SELECT classifier, attributeSelection, EXPERIMENTS.dataset, flow, " +
                        "AVG(weightedAreaUnderROC) as meanAUC, " +
                        "AVG(weightedFMeasure) as meanFMeasure, " +
                        "AVG(weightedPrecision) as meanPrecision, " +
                        "AVG(weightedRecall) as meanRecall, " +
                        "AVG(errorRate) as meanErrorRate, " +
                        "AVG(timeElapsed) as meanTimeElapsed, " +
                        "cluster " +
                        "FROM EXPERIMENTS " +
                        "INNER JOIN DATASET_CLUSTER ON DATASET_CLUSTER.dataset = EXPERIMENTS.dataset " +
                        "GROUP BY EXPERIMENTS.dataset, classifier, attributeSelection, flow";
        PreparedStatement selectPreparedStatement = null;

        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int rowIndex = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                if (rowIndex == 0) {
                    for (int j = 1; j <= columnsNumber; j++) {
                        if (j == columnsNumber)
                            stringBuilder.append(rsmd.getColumnName(j));
                        else
                            stringBuilder.append(rsmd.getColumnName(j)).append(",");
                    }
                    stringBuilder.append("\n");
                }

                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) stringBuilder.append(",");
                    String columnValue = resultSet.getString(i);
                    stringBuilder.append(columnValue);
                    if (i == columnsNumber) {
                        stringBuilder.append("\n");
                    }
                }
                rowIndex++;
            }

            FileWriter fw = new FileWriter(
                    ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "temp.csv",
                    false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(stringBuilder.toString());
            out.close();
            logger.info("Export summarized experiment without ranking table to CSV to be processed by RScript");

            runProcess("R CMD BATCH ranking_generation_all.R");
            logger.info("Running RScript to generate ranking per dataset to insert to table");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void createSummarizedExperimentHistoryTable() {
        String dropQuery = "DROP TABLE EXPERIMENT_SUMMARY";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS EXPERIMENT_SUMMARY(id int AUTO_INCREMENT," +
                "classifier varchar(255)," +
                "attributeSelection varchar(255)," +
                "dataset varchar(255)," +
                "flow varchar(255)," +
                "meanAUC double," +
                "meanFMeasure double," +
                "meanPrecision double," +
                "meanRecall double," +
                "meanErrorRate double," +
                "meanTimeElapsed double," +
                "ranking int)";
        String insertQuery = "INSERT INTO EXPERIMENT_SUMMARY" +
                "(classifier, attributeSelection, dataset, flow," +
                "meanAUC, meanFMeasure, meanPrecision," +
                "meanRecall, meanErrorRate, meanTimeElapsed, ranking) values" + "(?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement dropPreparedStatement = null;
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;

        exportSummarizedExperimentWithoutRankingTableToCSV();

        try {
            dropPreparedStatement = conn.prepareStatement(dropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();

            createPreparedStatement = conn.prepareStatement(createTableQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();
            logger.info("createPreparedStatement");

            CSVReader reader = null;
            try {
                reader = new CSVReader(
                        new FileReader(
                                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "experiment_workflow_ranking_by_dataset.csv"),
                        ',', '\'', 1);
                String[] line;
                while ((line = reader.readNext()) != null) {
                    insertPreparedStatement = conn.prepareStatement(insertQuery);
                    String classifier = line[0];
                    String attributeSelection = line[1];
                    String dataset = line[2];
                    String flow = line[3];
                    double meanAUC = (line[4] != null && line[4].length() > 0) ? Double.parseDouble(line[4]) : -1;
                    double meanFMeasure = (line[5] != null && line[5].length() > 0) ? Double.parseDouble(line[5]) : -1;
                    double meanPrecision = (line[6] != null && line[6].length() > 0) ? Double.parseDouble(line[6]) : -1;
                    double meanRecall = (line[7] != null && line[7].length() > 0) ? Double.parseDouble(line[7]) : -1;
                    double meanErrorRate = (line[8] != null && line[8].length() > 0) ? Double.parseDouble(line[8]) : -1;
                    double meanTimeElapsed = (line[9] != null && line[9].length() > 0) ? Double.parseDouble(line[9]) : -1;
                    double cluster = (line[10] != null && line[10].length() > 0) ? Double.parseDouble(line[10]) : -1;
                    double ranking = (line[13] != null && line[13].length() > 0) ? Double.parseDouble(line[13]) : -1;

                    insertPreparedStatement.setString(1, classifier);
                    insertPreparedStatement.setString(2, attributeSelection);
                    insertPreparedStatement.setString(3, dataset);
                    insertPreparedStatement.setString(4, flow);
                    insertPreparedStatement.setDouble(5, meanAUC);
                    insertPreparedStatement.setDouble(6, meanFMeasure);
                    insertPreparedStatement.setDouble(7, meanPrecision);
                    insertPreparedStatement.setDouble(8, meanRecall);
                    insertPreparedStatement.setDouble(9, meanErrorRate);
                    insertPreparedStatement.setDouble(10, meanTimeElapsed);
                    insertPreparedStatement.setDouble(11, ranking);
                    insertPreparedStatement.executeUpdate();
                    insertPreparedStatement.close();
                    logger.info("insertPreparedStatement");
                    conn.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * display SELECT * FROM EXPERIMENTS
     */
    public void viewAllNonSummarizedExperiments() {
        String selectQuery = "SELECT * FROM EXPERIMENTS";
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement = conn.prepareStatement(selectQuery);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            logger.info("================================== SELECT * FROM EXPERIMENT ================================");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) row += ",  ";
                    String columnValue = resultSet.getString(i);
                    row += columnValue + " " + rsmd.getColumnName(i);
                }
                logger.info(row);
            }
            logger.info("=========================================== END ============================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new data mining experiment to the db.
     *
     * @param record a map of description of the data mining experience.
     */
    public void insertNewExperiment(Map<String, Object> record) {
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;

        String CreateQuery = "CREATE TABLE IF NOT EXISTS EXPERIMENTS(id int AUTO_INCREMENT, " +
                "classifier varchar(255)," +
                "attributeSelection varchar(255)," +
                "seed double," +
                "dataset varchar(255)," +
                "flow varchar(255)," +
                "weightedAreaUnderROC double," +
                "weightedFMeasure double," +
                "weightedPrecision double," +
                "weightedRecall double," +
                "errorRate double," +
                "timeElapsed double)";
        String InsertQuery = "INSERT INTO EXPERIMENTS" +
                "(classifier, attributeSelection, seed, dataset, flow," +
                "weightedAreaUnderROC, weightedFMeasure, weightedPrecision," +
                "weightedRecall, errorRate, timeElapsed) values" + "(?,?,?,?,?,?,?,?,?,?,?)";

        try {
            createPreparedStatement = conn.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = conn.prepareStatement(InsertQuery);
            String classifier = (String) record.get("classifier");
            String attributeSelection = (String) record.get("attributeSelection");
            double seed = (Double) record.get("seed");
            String dataset = (String) record.get("dataset");
            String flow = (String) record.get("flow");
            double weightedAreaUnderROC = (Double) record.get("weightedAreaUnderROC");
            double weightedFMeasure = (Double) record.get("weightedFMeasure");
            double weightedPrecision = (Double) record.get("weightedPrecision");
            double weightedRecall = (Double) record.get("weightedRecall");
            double errorRate = (Double) record.get("errorRate");
            double timeElapsed = (Double) record.get("timeElapsed");

            insertPreparedStatement.setString(1, classifier);
            insertPreparedStatement.setString(2, attributeSelection);
            insertPreparedStatement.setDouble(3, seed);
            insertPreparedStatement.setString(4, dataset);
            insertPreparedStatement.setString(5, flow);
            insertPreparedStatement.setDouble(6, weightedAreaUnderROC);
            insertPreparedStatement.setDouble(7, weightedFMeasure);
            insertPreparedStatement.setDouble(8, weightedPrecision);
            insertPreparedStatement.setDouble(9, weightedRecall);
            insertPreparedStatement.setDouble(10, errorRate);
            insertPreparedStatement.setDouble(11, timeElapsed);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            conn.commit();
            logger.info("Insert record to db successfully!");
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
