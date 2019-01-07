import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Random;

public class Test13 {
    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource("/Users/newbiettn/Dropbox/Swinburne/Github/codes/DiabetesDiscoveryV2/resources/datasets/tmp/colic.arff");
        Instances instances = source.getDataSet();
        if (instances.classIndex() == -1)
            instances.setClassIndex(instances.numAttributes() - 1);

        AttributeSelection as = new AttributeSelection();
        ASSearch asSearch = ASSearch.forName("weka.attributeSelection.GreedyStepwise", new String[]{"-C", "-R"});
        as.setSearch(asSearch);
        ASEvaluation asEval = ASEvaluation.forName("weka.attributeSelection.CfsSubsetEval", new String[]{"-M"});
        as.setEvaluator(asEval);
        as.SelectAttributes(instances);
        instances = as.reduceDimensionality(instances);
        Classifier classifier = AbstractClassifier.forName("weka.classifiers.trees.J48", new String[]{"-B", "-M", "3"});
        classifier.buildClassifier(instances);


        Evaluation eval = new Evaluation(instances);
        eval.crossValidateModel(classifier, instances, 10, new Random(1));
        System.out.println(1-eval.errorRate());
        System.out.println(eval.errorRate());


    }

}
