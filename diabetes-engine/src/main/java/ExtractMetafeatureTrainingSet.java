import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author newbiettn on 7/7/18
 * @project DiabetesDiscoveryV2
 */

public class ExtractMetafeatureTrainingSet {
    private static Logger logger = LoggerFactory.getLogger(ExtractMetafeatureTrainingSet.class);
    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info(name + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime()
                .exec(command, null, new File(ProjectPropertiesGetter.getSingleton().getProperty("R.utilities")));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info(command + " exitValue() " + pro.exitValue());
    }

    public static double[][] normalize(double[][] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mf.length; i++){
            for (int j = 0; j < mf[0].length; j++){
                if (j == mf[0].length - 1)
                    stringBuilder.append(mf[i][j]);
                else
                    stringBuilder.append(mf[i][j]).append(",");
            }
            stringBuilder.append("\n");
        }

        FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "mf.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();

        /*Normalize by R*/
        logger.info("Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.training.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.mf.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[][] normalizedMf = new double[mf.length][mf[0].length];
        int row = 0;
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf[0].length; col++){
                normalizedMf[row][col] = Double.parseDouble(line[col]);
            }
            row++;
        }
        return normalizedMf;
    }

    public static void main(String[] args) throws Exception {
        DbUtils dbUtils = new DbUtils();
        ResultSet rs = dbUtils.getAllDatasetMetafeature();
        Map<String, Object> m = dbUtils.transformDatasetMetafeature(rs);
        List<String> datasetNames = (ArrayList)m.get("datasetNames");
        double[][] mf = (double[][])m.get("metafeatures");
        double[][] normalizedMf = normalize(mf);

        StringBuilder strBuilder = new StringBuilder();
        for (int row = 0; row < datasetNames.size(); row++){
            strBuilder.append(datasetNames.get(row)).append(", ");
            double[] instanceMf = normalizedMf[row];
            String instanceMfStr = java.util.Arrays.toString(instanceMf);
            instanceMfStr = instanceMfStr.substring(1, instanceMfStr.length() - 1);
            strBuilder.append(instanceMfStr);
            strBuilder.append("\n");
        }

        try(FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "mf_with_dsnames.csv",
                false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) { out.print(strBuilder.toString());
        } catch (IOException e) {
        }
    }
}
