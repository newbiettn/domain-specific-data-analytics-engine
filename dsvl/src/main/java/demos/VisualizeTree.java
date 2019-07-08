package demos;

import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-07-06
 */
public class VisualizeTree {
    public static void main (String[] args) throws Exception {
        Instances data = new Instances(new BufferedReader(new FileReader("resources/sparqml/training_data/training_modelName.arff")));
        data.setClassIndex(data.numAttributes() - 1);

        DecisionTable classifier = new DecisionTable();
        classifier.buildClassifier(data);
        System.out.println(classifier.toString());


//        TreeVisualizer tv = new TreeVisualizer(null, classifier.graph(), new PlaceNode2());
//
//        JFrame frame = new javax.swing.JFrame("Tree Visualizer");
//        frame.setSize(800, 800);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.getContentPane().add(tv);
//        frame.setVisible(true);
//
//        tv.fitToScreen();
    }
}
