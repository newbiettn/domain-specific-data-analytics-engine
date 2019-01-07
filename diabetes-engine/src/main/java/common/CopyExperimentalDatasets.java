package common;

import com.opencsv.CSVReader;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author newbiettn on 19/6/18
 * @project DiabetesDiscoveryV2
 */

public class CopyExperimentalDatasets {
    public static void main(String[] args) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get("merged.csv"))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                // Reading Records One by One in a String array
                String[] nextRecord;
                while ((nextRecord = csvReader.readNext()) != null) {
                    String fileName = nextRecord[8];
                    File source = new File("/Volumes/Seagate/openml_datasets/openml/" + fileName);
                    if (source.exists()){
                        File dest = new File("/Volumes/Seagate/openml_datasets/extract/" + fileName);
                        copyFileUsingChannel(source, dest);
                    }

                }
            }
        }
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }finally{
            sourceChannel.close();
            destChannel.close();
        }
    }
}
