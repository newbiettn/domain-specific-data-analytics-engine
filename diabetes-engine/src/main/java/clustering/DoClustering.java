package clustering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smile.clustering.GMeans;
import smile.clustering.KMeans;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author newbiettn on 2/7/18
 * @project DiabetesDiscoveryV2
 */
public class DoClustering {
    private static Logger logger = LoggerFactory.getLogger(DoClustering.class);
    public static final int MAX_CLUSTER = 100;

    /**
     * Predict the target cluster given a set of metafeatures.
     *
     * @param data a 2-d array of data.
     * @return an object of GMeans
     * @throws IOException
     * @throws ParseException
     */
    public KMeans performKmean(double[][] data) {
        int n = data.length;
        int k = (int) Math.sqrt(n/2);
        KMeans km = new KMeans(data, k);
        return km;
    }
    public GMeans performGmean(double[][] data) {
        GMeans gm = new GMeans(data, MAX_CLUSTER);
        return gm;
    }
}
