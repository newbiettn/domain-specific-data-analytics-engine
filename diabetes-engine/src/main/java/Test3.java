import mf.generator.MetafeatureGenerator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * @author newbiettn on 2/7/18
 * @project DiabetesDiscoveryV2
 */

public class Test3 {
    private static Logger logger = LoggerFactory.getLogger(Test3.class);

    public static void main(String[] args) throws Exception {
//        -- Clustering the metadata
//        DoClustering dc = new DoClustering();
//        GMeans gm = dc.perform(new FileInputStream("subset.mf.arff"));
//        System.out.println(gm.getNumClusters());
//        for (int i : gm.getClusterLabel()){
//            System.out.print(i + ",");
//        }
//        System.out.println("");
//
//        --- Read a dataset and select the best workflow

        System.out.println("===========================================================================");
        File folder = new File("resources/datasets/tmp/");
        File[] files = folder.listFiles();
        for (File f : files){
            logger.info(f.getName());
            if (!FilenameUtils.getExtension(f.getName()).equals("arff"))
                continue;
            MetafeatureGenerator mfGen = new MetafeatureGenerator();
            Map<String, Double> mf = mfGen.generate(f);
            double[] x = new double[mf.size()];
            int i = 0;
            for (String k : mf.keySet()){
                x[i] = mf.get(k);
                i++;
            }
//            int cl = gm.predict(x);
//            logger.info("Cluster: " + cl);
            System.out.println("===========================================================================");
        }
    }
}
