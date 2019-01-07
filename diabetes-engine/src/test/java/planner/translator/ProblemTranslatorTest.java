package planner.translator;

//import common.LoggerRule;

import common.ProjectPropertiesGetter;
import model.DMDataset;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Test unit for {@link ProblemTranslator}
 *
 * @project diabetes-engine
 * @author newbiettn on 6/3/18
 *
 */

public class ProblemTranslatorTest {
    @ClassRule
//    public static LoggerRule testLogger = new LoggerRule();

    public ProblemTranslator problemTranslator;
    private static Logger logger;
    private ProjectPropertiesGetter propGetter;

    @Before
    public void setUp(){
        logger = LoggerFactory.getLogger(ProblemTranslatorTest.class);
        propGetter = ProjectPropertiesGetter.getSingleton();
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        DMDataset dmDataset = new DMDataset(fName, targetClassName);
//        problemTranslator = new ProblemTranslator(dmDataset);
    }

    /**
     * Test creating a string of problem description.
     */
    @Test
    public void test1CreateProblem(){
        logger.info("\n" + problemTranslator.create());
    }

    /**
     * Test output the problem description file
     *
     * @throws FileNotFoundException
     */
    @Test
    public void test2OutputProblemDescriptionFile() throws IOException {
        problemTranslator.outputFile();
    }

    @Test
    public void test3ReadArff() throws IOException {
        String fPath = problemTranslator.getDmDataset().getFilepath();
        logger.info(fPath);
        BufferedReader reader = new BufferedReader(new FileReader(fPath));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances data = arff.getData();
        Attribute target = problemTranslator.getDmDataset().getTargetClass();
        data.setClass(target);
        logger.info(data.toSummaryString());
    }
}
