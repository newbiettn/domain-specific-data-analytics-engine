package mf.generator;

import org.apache.commons.io.FilenameUtils;
import org.openml.webapplication.fantail.dc.Characterizer;
import org.openml.webapplication.features.GlobalMetafeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * @author newbiettn on 2/7/18
 * @project DiabetesDiscoveryV2
 */

public class MetafeatureGenerator {
    private static Logger logger = LoggerFactory.getLogger(MetafeatureGenerator.class);

    /**
     * Generate metafeatures of a dataset.
     *
     * @param f ARFF file
     * @return a collection of metafeatures of keys and values
     * @throws Exception
     */
    public Map<String, Double> generate (File f) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(f.getPath()));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances data = arff.getData();
        data.setClassIndex(data.numAttributes() - 1);
        if (data.classAttribute().isNominal()) {
            Map<String, Double> result = new HashMap<>();
            GlobalMetafeatures gm = new GlobalMetafeatures(800);
            List<Characterizer> batchCharacterizers = gm.getCharacterizers();
            for (Characterizer characterizer : batchCharacterizers) {
                Map<String, Double> c = characterizer.characterize(data);
                result.putAll(c);
            }
//            logger.info("Length: " + result.size());
            return result;
        }
        return null;
    }

    /**
     * Batch generation of a folder of dataset files.
     *
     * @param folder
     * @return
     * @throws Exception
     */
    public Set<Map<String, Object>> batchGenerate(File folder) throws Exception {
        Set<Map<String, Object>> batchResult = new HashSet<>();
        File[] files = folder.listFiles();
        logger.info(folder.listFiles().length+"");
        for (File f : files){
            if (!FilenameUtils.getExtension(f.getName()).equals("arff")){
                System.out.println(f.getName());
                continue;
            }
            String fileName = f.getName();

            //-- Generating
            logger.info("Generate metafeatures for: " + f.getName());
            Map<String, Double> mf = this.generate(f);

            //-- Prepare a single set of metafeature of a dataset
            Map<String, Object> record = new HashMap<>();
            record.put("Dataset", fileName);
            for (String key : mf.keySet()){
                record.put(key, (Double)mf.get(key));
            }

            //-- Put it in the batch result to return later
            batchResult.add(record);
        }
        return batchResult;
    }

}
