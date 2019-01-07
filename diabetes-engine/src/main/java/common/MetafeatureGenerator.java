package common;

import org.apache.commons.io.FilenameUtils;
import org.openml.webapplication.fantail.dc.Characterizer;
import org.openml.webapplication.features.GlobalMetafeatures;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class MetafeatureGenerator {

	public static void main(String[] args) throws Exception {
		ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
		File folder = new File(propGetter.getProperty("tmp"));
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

			GlobalMetafeatures gm = new GlobalMetafeatures(800);
			List<Characterizer> batchCharacterizers = gm.getCharacterizers();
//			System.out.println(batchCharacterizers.size());
            int i = 0;

			for(Characterizer characterizer : batchCharacterizers) {
				Map<String, Double> c = characterizer.characterize(data);
				for (String k : c.keySet()){
					System.out.println(k + " : " + c.get(k));
					i++;
				}
			}
			System.out.println(i);

		}
	}

}
