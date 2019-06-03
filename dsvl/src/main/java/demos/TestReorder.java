package demos;

import common.ProjectPropertiesGetter;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

import java.io.File;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-06-03
 */
public class TestReorder {
    public static void main (String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        String testDatafilePath = propGetter.getProperty("sparqlml.test.data.filepath");
        String testArff = testDatafilePath + "tmp_test_dataset.arff";

        ArffLoader arffLoader = new ArffLoader();
        arffLoader.setSource(new File(testArff));
        Instances testInstances = arffLoader.getDataSet();


        Reorder reorder = new Reorder();
        String[] newIndex = new String[]{"2", "1", "last"};
        String attributeOrder = String.join(",", newIndex);
        System.out.println(attributeOrder);
        reorder.setAttributeIndices(attributeOrder);
        reorder.setInputFormat(testInstances);
        testInstances = Filter.useFilter(testInstances, reorder);

//        System.out.println(testInstances.toString());

        ArffSaver saver = new ArffSaver();
        saver.setInstances(testInstances);
        saver.setFile(new File(testArff));
        saver.writeBatch();
    }
}
