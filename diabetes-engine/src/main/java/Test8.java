import common.ProjectPropertiesGetter;
import db.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author newbiettn on 23/7/18
 * @project DiabetesDiscoveryV2
 */
public class Test8 {
    private static Logger logger = LoggerFactory.getLogger(Test8.class);
    public static void main(String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        DbUtils dbUtils = new DbUtils();
        Trainer trainer = Trainer.getSingleton();

        File folder = new File(propGetter.getProperty("evaluation.dataset.collection"));
        File[] files = folder.listFiles();
        int cluster = 1;
        List<Map<String, Object>> workflows = dbUtils.getWorkflowOfDatasetByCluster(cluster);
        for (Map<String, Object> wk : workflows){
            /*Extract the workflow details*/
            String classifier = (String) wk.get("classifier");
            String attributeSelection = (String) wk.get("attributeSelection");
            int rank = (int) wk.get("rank");
            File f = files[0];
            int seed = 1;
            boolean insertToDb = false;
            String filePath = propGetter.getProperty("evaluation.dataset.collection");

            /*Call trainer to generate plan for the target dataset*/
            Map<String, Object> r = trainer.execute(f,
                    seed,
                    classifier,
                    attributeSelection,
                    insertToDb,
                    filePath);
            r.put("rank", rank);
//            dbUtils.insertNewEvaluationExperiment(r);
        }
//        dbUtils.viewAllEvaluationExperiments();
//        dbUtils.exportEvaluationExperimentsToCSV();
    }
}
