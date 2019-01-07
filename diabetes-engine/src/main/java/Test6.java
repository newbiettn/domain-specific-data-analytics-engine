import clustering.DoClustering;
import db.DbUtils;
import smile.clustering.GMeans;

import java.io.*;
import java.sql.SQLException;

/**
 * @author newbiettn on 23/7/18
 * @project DiabetesDiscoveryV2
 */

public class Test6 {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        DbUtils dbUtils = new DbUtils();
        DoClustering dc = new DoClustering();

//        ResultSet rs = dbUtils.getAllDatasetMetafeature();
//        Map<String, Object> m = dbUtils.transformDatasetMetafeature(rs);
//        double[][] mf = (double[][])m.get("metafeatures");
//        GMeans km = dc.performGmean(mf);
//        System.out.println(km.toString());
//
//        /*Save model to file */
//        FileOutputStream f = new FileOutputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
//        ObjectOutputStream o = new ObjectOutputStream(f);
//        o.writeObject(km);
//        o.flush();
//        o.close();

        /*Load model from file*/
        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        GMeans km1 = (GMeans) oi.readObject();
        oi.close();
        System.out.println(km1.toString());
    }
}
