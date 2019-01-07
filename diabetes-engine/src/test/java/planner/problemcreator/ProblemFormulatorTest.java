package planner.problemcreator;

import common.ProjectPropertiesGetter;
import model.DMDataset;
import org.junit.Before;
import org.junit.ClassRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author newbiettn on 13/6/18
 * @project diabetes-engine
 */

public class ProblemFormulatorTest {
    @ClassRule
//    public static LoggerRule testLogger = new LoggerRule();

    public ProblemFormulator problemFormulator;
    private static Logger logger;
    private ProjectPropertiesGetter propGetter;

    @Before
    public void setUp(){
        logger = LoggerFactory.getLogger(ProblemFormulatorTest.class);
        propGetter = ProjectPropertiesGetter.getSingleton();
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        DMDataset dmDataset = new DMDataset(fName, targetClassName);
        problemFormulator = new ProblemFormulator(dmDataset);
    }

    /**
     * Test creating a string of problem description.
     */

}
