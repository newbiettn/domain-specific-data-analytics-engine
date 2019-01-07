import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Test10 {
    public static void main (String[] args) throws SQLException, IOException {
        DbUtils dbUtils = new DbUtils();

        ResultSet rs = dbUtils.getAllDatasetMetafeature();
        Map<String, Object> m = dbUtils.transformDatasetMetafeature(rs);
        double[][] mf = (double[][])m.get("metafeatures");

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

        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized_mf.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[][] normalizedMf = new double[mf.length][mf[0].length];
        int row = 0;
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf[0].length; col++){
                normalizedMf[row][col] = Double.parseDouble(line[col]);
                System.out.print(normalizedMf[row][col] + ",");
            }
            System.out.println();
        }

    }
}
