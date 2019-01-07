import com.google.common.base.Stopwatch;
import common.ProjectPropertiesGetter;
import model.DMOperator;
import model.DMPlan;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import weka.knowledgeflow.Flow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static planner.Trainer.makeFooterForTranslation;
import static planner.Trainer.makeHeaderForTranslation;

public class Test11 {
    private static Logger logger = LoggerFactory.getLogger(Test11.class);
    public static void main (String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();

        String[] classifiers = new String[]{
                "randomforest",
                "randomsubspace",
                "stacking"};
        String[] attributeSelections = new String[]{
                "cfs-subset-eval",
                "correlation-attribute-eval",
                "gain-ratio-attribute-eval",
                "info-gain-attribute-eval",
                "relieff-attribute-eval",
                "symmetrical-uncert-attribute-eval",
                "wrapper-subset-eval-with-adaboost-operator"};

        //-- Sequentially read all files from the data repository
        File folder = new File(propGetter.getProperty("multithread.testing.dataset.collection"));
        File[] files = folder.listFiles();
        String filePath = propGetter.getProperty("multithread.testing.dataset.collection");
        for(File f : files){
            //-- MacOS always have DS_Store file, have to handle it
            if (FilenameUtils.getExtension(f.getName()).equals("DS_Store"))
                continue;
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();

            String flowFileName = propGetter.getProperty("multiflow.weka.output.file");
            File kfFile = new File(flowFileName);
            makeHeaderForTranslation(flowFileName);
            int flowIndex = 0;
            int lastIndex = (classifiers.length*attributeSelections.length)-1;
            List<Map<String, Object>> multiFlowResult = new ArrayList();
            for (String c : classifiers) {
                Map<String, Object> result = new HashMap<>();
                multiFlowResult.add(result);
                for (String a : attributeSelections){
                    //-- Execution
                    for (int seed = 1; seed<=1; seed++){
                        Trainer trainer = Trainer.getSingleton();
                        boolean compiledResult = trainer.generateProblemFile(f, c, a);
                        if (compiledResult) {
                            //-- Generate the plan
                            DMPlan plan = trainer.generatePlan();

                            //-- Translate the plan to WEKA KNOWLEDGE FLOW file
                            kfFile = trainer.translatePlanToJSONForMultiFlows(
                                    flowFileName,
                                    seed,
                                    filePath,
                                    flowIndex,
                                    plan,
                                    lastIndex);
                            StringBuilder strBuilder = new StringBuilder();
                            for (DMOperator op : plan.getOperators()){
                                strBuilder.append(op.getName());
                                if (op.getNextOp() != null) strBuilder.append("|");
                            }
                            String workflowDesc = strBuilder.toString();

                            result.put("classifier", c);
                            result.put("attributeSelection", a);
                            result.put("seed", seed);
                            result.put("flow", workflowDesc);

                            Trainer.reset();
                            logger.info(strBuilder.toString());
                        } else {
                            logger.warn("Compilation of problem.java failed!");
                        }
                    }
                    flowIndex++;
                }
            }
            makeFooterForTranslation(flowFileName);

            Trainer trainer = Trainer.getSingleton();
            Flow flow = trainer.executeTranslatedPlan(kfFile);

            //-- Collect the flow results
            int i = 0;
            for (Map<String, Object> r : multiFlowResult){
                Map<String, Double> result = trainer.getDataMiningResultFromMultiFlow(flow, i);
                i++;
                r.put("weightedAreaUnderROC", result.get("weightedAreaUnderROC"));
                r.put("weightedFMeasure", result.get("weightedFMeasure"));
                r.put("weightedPrecision", result.get("weightedPrecision"));
                r.put("weightedRecall", result.get("weightedRecall"));
                r.put("errorRate", result.get("errorRate"));
            }

            //-- Get the best flow based on AUC
            double bestAUC = -1.0;
            int bestFlowIndex = -1;
            int j = 0;
            for (Map<String, Object> r : multiFlowResult){
                double auc = (Double)r.get("weightedAreaUnderROC");
                if (bestAUC < auc){
                    bestAUC = auc;
                    bestFlowIndex = j;
                }
                j++;
            }
            Map<String, Object> bestFlowResult = multiFlowResult.get(bestFlowIndex);
            stopwatch.stop();
            long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            bestFlowResult.put("timeElapsed", (double)timeElapsed);

            logger.info((double)bestFlowResult.get("weightedAreaUnderROC")+"");
            logger.info((double)bestFlowResult.get("timeElapsed")+"");
            logger.info(bestAUC+"");
        }
    }
}
