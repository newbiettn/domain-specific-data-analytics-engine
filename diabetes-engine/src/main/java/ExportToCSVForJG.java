import clustering.DoClustering;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExportToCSVForJG {
    public static void main(String[] args) throws Exception {
        DbUtils dbUtils = new DbUtils();
        MetafeatureGenerator mfGen = new MetafeatureGenerator();

        dbUtils.dropDatasetMetafeature();

        /*Generate meta-features for a set of datasets*/
        File folder = new File(ProjectPropertiesGetter.getSingleton().getProperty("evaluation.dataset.collection"));
        Set<Map<String, Object>> batchResult = mfGen.batchGenerate(folder);

        /*Store in db*/
        for (Map<String, Object> record : batchResult){
            dbUtils.insertDatasetMetafeature(record);
        }
        dbUtils.viewDatasetMetafeature();

        ResultSet rs = dbUtils.getAllDatasetMetafeature();
        ResultSetMetaData rsmd = rs.getMetaData();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            String name = rsmd.getColumnName(i);
            if (i < rsmd.getColumnCount())
                stringBuilder.append(name).append(",");
            else
                stringBuilder.append(name).append("\n");
        }
        while (rs.next()){
            for (int i = 1; i <= rsmd.getColumnCount(); i++ ) {
                String value = rs.getString(i);
                if (i < rsmd.getColumnCount())
                    stringBuilder.append(value).append(",");
                else
                    stringBuilder.append(value).append("\n");
            }
        }


//        while (rs.next()) {
//
//            System.out.println(rs.);
//        }

//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < mf.length; i++){
//            for (int j = 0; j < mf[0].length; j++){
//                if (j == mf[0].length - 1)
//                    stringBuilder.append(mf[i][j]);
//                else
//                    stringBuilder.append(mf[i][j]).append(",");
//            }
//            stringBuilder.append("\n");
//        }
//
        FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "mf.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();
    }
}
