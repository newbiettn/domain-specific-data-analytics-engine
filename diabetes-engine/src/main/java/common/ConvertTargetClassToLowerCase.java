package common;

import org.apache.commons.io.FilenameUtils;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author newbiettn on 19/6/18
 * @project DiabetesDiscoveryV2
 */

public class ConvertTargetClassToLowerCase {
    public static void main(String[] args) throws IOException {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        File folder = new File(propGetter.getProperty("openml.repository"));
        File[] files = folder.listFiles();
        for(File f : files) {
            System.out.println(f.getName());
            if (!FilenameUtils.getExtension(f.getName()).equals("arff"))
                continue;
            //-- Generate problem file by reading ARFF dataset file
            BufferedReader reader = new BufferedReader(new FileReader(f.getPath()));
            ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
            Instances data = arff.getData();
            data.setClassIndex(data.numAttributes() - 1);
            Attribute target = data.classAttribute();
            ArffSaver saver = new ArffSaver();
            try {
                if (!target.name().equals("class")){
//                String modifiedName = target.name();
//            modifiedName = modifiedName.toLowerCase();
//            modifiedName = modifiedName.replaceAll("[^a-zA-Z0-9_-]", "");
                    String modifiedName = "class";
                    data.renameAttribute(target, modifiedName);
                    saver.setInstances(data);
                    saver.setFile(f);
                    saver.setDestination(f);
                    saver.writeBatch();

                }

            } catch (IllegalArgumentException e) {
                System.out.println("class is not the last attribute");
                f.delete();
            }
        }
    }
}
