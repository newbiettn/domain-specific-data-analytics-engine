package org.openml.webapplication.test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.openml.webapplication.fantail.dc.Characterizer;

import org.openml.webapplication.features.GlobalMetafeatures;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class MetafeatureGenerator{

	public static void main(String[] args) throws IOException {

		File folder = new File("/Users/newbiettn/Dropbox/Swinburne/Github/R/optimizationV4/baseTrainingDatasetsARFF");
		File[] files = folder.listFiles();

		Path p = Paths.get("metafeatures.csv");
		if (Files.notExists(p)){
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(files[1].getPath()));
			ArffLoader.ArffReader arff = null;
			arff = new ArffLoader.ArffReader(reader);
			Instances data = arff.getData();
			data.setClassIndex(data.numAttributes() - 1);

			GlobalMetafeatures gm = null;
			try {
				gm = new GlobalMetafeatures(800);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Characterizer> batchCharacterizers = gm.getCharacterizers();

			Map<String, Double> header = new HashMap<>();
			for(Characterizer characterizer : batchCharacterizers) {
				Map<String, Double> c = characterizer.characterize(data);
				header.putAll(c);
			}

			try {
				createResultFile("metafeatures.csv", header);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		for (final File f : files) {
			try {
				if (!FilenameUtils.getExtension(f.getName()).equals("arff")) {
				    System.out.println(f.getName());
					continue;
				}

				MetafeatureGenerator mg = new MetafeatureGenerator();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				System.out.print("[" + dateFormat.format(date) + "] ");
				System.out.println("Start generating metafeatures of the file: " + f.getName());
				File mfFile = new File("metafeatures.csv");

				//-- Generate problem file by reading ARFF dataset file
				BufferedReader reader1 = null;
				try {
					reader1 = new BufferedReader(new FileReader(f.getPath()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				ArffLoader.ArffReader arff1 = null;
				try {
					arff1 = new ArffLoader.ArffReader(reader1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Instances data1 = arff1.getData();
				data1.setClassIndex(data1.numAttributes() - 1);
				if (data1.classAttribute().isNominal()) {
					GlobalMetafeatures gm1 = null;
					try {
						gm1 = new GlobalMetafeatures(800);
					} catch (Exception e) {
						e.printStackTrace();
					}
					List<Characterizer> batchCharacterizers1 = gm1.getCharacterizers();

					Map<String, Double> result = new HashMap<>();
					for (Characterizer characterizer : batchCharacterizers1) {
						System.out.println(characterizer.toString());
						Map<String, Double> c = characterizer.characterize(data1);
						result.putAll(c);
					}
					mg.writeResultToFile(mfFile, f.getName(), result);
					System.out.println("Successful!!!");
				}
				System.out.println("=================================================================================");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	 static void createResultFile(String fName, Map<String, Double> c) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(fName);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("dataset");
		sb.append(",");
		for (String k : c.keySet()){
			sb.append(k);
			if (i < c.size()-1) {
				sb.append(",");
			}
			i++;
		}
		sb.append("\n");
		pw.write(sb.toString());
		pw.close();
		System.out.println("Output file created successfully!");
	}

	void writeResultToFile(File f,
								   String fname,
								   Map<String, Double> c) {
		try(FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)) {
			out.print(fname);
			out.print(",");
			int i = 0;
			for (String k : c.keySet()){
				out.print(c.get(k));
				if (i < c.size()-1) {
					out.print(",");
				}
				i++;
			}
			out.print("\n");
		} catch (IOException e) {
		}
	}

}
