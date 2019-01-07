package model;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.util.*;

/**
 * Describe the data structure of the input of the system.
 * In detail, we have a dataset and then we extract all its metadata. All extracted metadata will be stored in
 * this model in order to formulate the planning problem.
 *
 * @author newbiettn on 6/3/18
 * @project diabetes-engine
 */

public class DMDataset {
    private static Logger logger = LoggerFactory.getLogger(DMDataset.class);

    private String filepath;                    //-- file path to the dataset.
    private String filename;                    //-- name of the file
    private DataSource ds;                      //-- data source
    private Instances instances;                //-- instances
    private Attribute targetClass;              //-- target class of the dataset
    private Set<Attribute> features;            //-- features (not including target class)

    public DMDataset(String fName, String tClass){
        try {
            this.filepath = fName;
            this.ds = new DataSource(this.filepath);
            this.instances = this.ds.getDataSet();
            this.targetClass = this.instances.attribute(tClass);
            this.instances.setClass(this.targetClass);
            this.filename = new File(filepath).getName();

            features = new HashSet<>(Collections.list(instances.enumerateAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DMDataset(String fName, int tClass){
        try {
            this.filepath = fName;
            this.ds = new DataSource(this.filepath);
            this.instances = this.ds.getDataSet();
            this.targetClass = this.instances.attribute(tClass);

            this.instances.setClass(this.targetClass);
            this.filename = new File(filepath).getName();
            features = new HashSet<>(Collections.list(instances.enumerateAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-- getters & setters
    public String getFilename() { return filename; }
    public void setFilename(String filepath) { this.filepath = filepath; }

    public String getFilepath() { return filepath; }
    public void setFilepath(String filepath) { this.filepath = filepath; }

    public DataSource getDs() { return ds; }
    public void setDs(DataSource ds) { this.ds = ds; }

    public Instances getInstances() { return instances; }
    public void setInstances(Instances instances) { this.instances = instances; }

    public Attribute getTargetClass() { return targetClass; }
    public void setTargetClass(Attribute targetClass) { this.targetClass = targetClass; }

    public Set<Attribute> getFeatures() { return features; }
    public void setFeatures(Set<Attribute> features) { this.features = features; }



    /**
     * Check missing value of dataset
     *
     * @return
     */
    public boolean hasMissingValue(){
        boolean hasMissingVal = false;
        for (Enumeration<Instance> e = this.instances.enumerateInstances(); e.hasMoreElements();){
            Instance i = e.nextElement();
            if (i.hasMissingValue()) {
                hasMissingVal = true;
                break;
            }
        }
        return hasMissingVal;
    }

    /**
     * Scan every feature of dataset to make a report of missing percentage of them
     *
     * @return
     */
    public Map<Attribute, Double> scanMissingValue(){
        boolean hasMissingVal = this.hasMissingValue();
        if (!hasMissingVal)
            return null;
        Map<Attribute, Double> mReport = new HashMap<>(); //-- missing value report
        int numInst = instances.numInstances();
        for (Attribute attr : features){
            int mCount = 0; //-- count missing instances per attr
            for (Enumeration<Instance> e = instances.enumerateInstances(); e.hasMoreElements();){
                Instance i = e.nextElement();
                if (i.isMissing(attr))
                    mCount++;
            }
            double mPercent = 100*(mCount*1.000)/numInst; //-- missing percentage by attr
            mReport.put(attr, mPercent);
        }
        return mReport;
    }

    /**
     * Scan every feature of dataset to make a report of missing percentage of them
     *
     * @return
     */
    public double getMissingValuePercentage(Attribute attr){
        boolean hasMissingVal = this.hasMissingValue();
        if (!hasMissingVal)
            return 0;
        int numInst = instances.numInstances();
        int mCount = 0; //-- count missing instances per attr
        for (Enumeration<Instance> e = instances.enumerateInstances(); e.hasMoreElements();){
            Instance i = e.nextElement();
            if (i.isMissing(attr))
                mCount++;
        }
        double mPercent = 100*(mCount*1.000)/numInst; //-- missing percentage by attr
        return mPercent;
    }

    /**
     * Output a list of feature names for display.
     *
     * @return a string of concatenated feature names, such as "feature1 feature2 feature3"
     */
    public String featuresToString(){
        String output;
        List<String> fNames = new ArrayList<>();
        int i = 1;
        for (Attribute attr : features){
            fNames.add("feature" + i);
            i++;
        }
        output = String.join(" ", fNames);
        return output;
    }

    /**
     * @return total number of examples in the dataset.
     */
    public int getN(){
        return instances.numInstances();
    }

    /**
     * @return number of features of the dataset.
     */
    public int getNumberofFeatures(){
        return (instances.numAttributes() - 1);
    }

    public String getFileType(){
        return FilenameUtils.getExtension(filename);
    }

}
