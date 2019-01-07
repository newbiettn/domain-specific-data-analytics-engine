package evaluation;

import com.google.common.base.Stopwatch;
import common.ProjectPropertiesGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;

import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Evaluate AutoWeka
 */
public class EvaluateAutoWeka {
    private static Logger logger = LoggerFactory.getLogger(EvaluateAutoWeka.class);

    public static void main (String[] args) throws FileNotFoundException {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        int[] seeds = new int[]{500, 550, 600, 650, 700, 750, 800, 850, 900, 950};
        String[] datasetNames = new String[]{"anneal", "audiology", "autos", "balance-scale",
                "breast-cancer", "breast-w", "colic", "credit-a", "credit-g",
                "diabetes", "glass", "heart-c", "heart-h",
                "heart-statlog", "hepatitis", "hypothyroid",
                "ionosphere", "iris", "kr-vs-kp", "labor",
                "letter", "lymph","mushroom", "segment",
                "sick", "sonar", "soybean", "splice", "vehicle",
                "vote", "vowel", "waveform-5000", "zoo", "primary-tumor"};
        String filePath = propGetter.getProperty("benchmark.again.autoweka");
        String outputFileName = propGetter.getProperty("autoweka.result.file");
        File output = new File(outputFileName);
        createResultFile(outputFileName);
        for(String datasetName : datasetNames) {
            String trainingFileUrl = filePath + "training_" + datasetName + "-Randomize-S800.arff";
            String testFileUrl = filePath + "test_" + datasetName + "-Randomize-S800.arff";

            try {
                for (int seed : seeds) {
                    Stopwatch stopwatch = new Stopwatch();
                    stopwatch.start();

                    Trainer trainer = Trainer.getSingleton();
                    Map<String, Object> r = trainer.executeAutoWeka(
                            datasetName,
                            trainingFileUrl,
                            testFileUrl,
                            seed);

                    stopwatch.stop();
                    long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                    r.put("timeElapsed", timeElapsed);

                    //-- Write to file
                    try (FileWriter fw = new FileWriter(output, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.print(r.get("classifier"));
                        out.print(",");
                        out.print(r.get("attributeSelection"));
                        out.print(",");
                        out.print(seed);
                        out.print(",");
                        out.print(r.get("dataset"));
                        out.print(",");
                        out.print(r.get("weightedAreaUnderROC"));
                        out.print(",");
                        out.print(r.get("weightedFMeasure"));
                        out.print(",");
                        out.print(r.get("weightedPrecision"));
                        out.print(",");
                        out.print(r.get("weightedRecall"));
                        out.print(",");
                        out.print(r.get("errorRate"));
                        out.print(",");
                        out.print(r.get("timeElapsed"));
                        out.print("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
            }

        }
    }
    static void createResultFile(String fName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fName);
        StringBuilder sb = new StringBuilder();
        sb.append("classifier");
        sb.append(",");
        sb.append("attributeSelection");
        sb.append(',');
        sb.append("seed");
        sb.append(',');
        sb.append("dataset");
        sb.append(',');
        sb.append("weightedAreaUnderROC");
        sb.append(',');
        sb.append("weightedFMeasure");
        sb.append(',');
        sb.append("weightedPrecision");
        sb.append(',');
        sb.append("weightedRecall");
        sb.append(',');
        sb.append("errorRate");
        sb.append(',');
        sb.append("timeElapsed");
        sb.append('\n');

        pw.write(sb.toString());
        pw.close();
        logger.info("Output file created successfully!");
    }
}
