package model;

import common.ProjectPropertiesGetter;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author newbiettn on 6/3/18
 * @project diabetes-engine
 */

public class DMDatasetTest {
    @ClassRule
//    public static LoggerRule testLogger = new LoggerRule();
    private static Logger logger;
    private ProjectPropertiesGetter propGetter;

    public DMDataset dmDataset;

    @Before
    public void setUp(){
        logger = LoggerFactory.getLogger(DMDatasetTest.class);
        propGetter = ProjectPropertiesGetter.getSingleton();
    }

    /**
     * Test extracting all private variables.
     *
     */
    @Test
    public void testConstructor(){
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        logger.info(dmDataset.getFilename());
        logger.info(dmDataset.getDs().toString());
        logger.info(dmDataset.getInstances().toString());
        logger.info(dmDataset.getTargetClass().toString());
        for (Attribute attr : dmDataset.getFeatures()){
            logger.info("Attribute name: " + attr.name()); //-- should display all features, except the target class
            logger.info("Attribute index: " + attr.index());
            logger.info("Is numeric datatype: " + attr.isNumeric());
            logger.info("Is nominal datatype: " + attr.isNominal());
            logger.info("Is string datatype: " + attr.isString());
            logger.info("Is datetime datatype: " + attr.isDate());
            logger.info("Attribute datatype: " + Attribute.typeToString(attr));
        }
    }

    /**
     * Test with file has missing values.
     */
    @Test
    public void test1CheckMissingValue(){
        String fName = propGetter.getProperty("test.dataset.contain.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.contain.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        boolean hasMissingValue = dmDataset.hasMissingValue();
        assertTrue(hasMissingValue);
    }

    /**
     * Test with file has NO missing value.
     */
    @Test
    public void test2CheckMissingValue(){
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        boolean hasMissingValue = dmDataset.hasMissingValue();
        assertFalse(hasMissingValue);
    }

    /**
     * Test with file has missing values.
     *
     */
    @Test
    public void test1ScanMissingValue(){
        String fName = propGetter.getProperty("test.dataset.contain.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.contain.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        Map<Attribute, Double> mReport = dmDataset.scanMissingValue();
        for (Map.Entry<Attribute, Double> entry : mReport.entrySet()){
            logger.info(entry.getKey() + "/" + entry.getValue());
        }
    }

    /**
     * Test with file has NO missing values.
     *
     */
    @Test
    public void test2ScanMissingValue(){
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        Map<Attribute, Double> mReport = dmDataset.scanMissingValue(); //-- should return NULL b/c no missing value
        assertNull(mReport);
    }

    /**
     *
     */
    @Test
    public void testFeaturesToString(){
        String fName = propGetter.getProperty("test.dataset.no.missingvalues");
        String targetClassName = propGetter.getProperty("test.dataset.no.missingvalues.targetclass");
        dmDataset = new DMDataset(fName, targetClassName);
        logger.info(dmDataset.featuresToString());
    }

}
