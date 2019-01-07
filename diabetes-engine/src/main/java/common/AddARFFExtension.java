package common;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * @author newbiettn on 20/6/18
 * @project DiabetesDiscoveryV2
 */

public class AddARFFExtension {
    public static void main(String[] args){
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        File folder = new File(propGetter.getProperty("openml.repository"));
        File[] files = folder.listFiles();
        for(File f : files) {
            if ((!FilenameUtils.getExtension(f.getName()).equals("arff")) &&
                    (!FilenameUtils.getExtension(f.getName()).equals("DS_Store"))){
                f.renameTo(new File(f.getAbsolutePath() + ".arff"));
            }
        }
    }
}
