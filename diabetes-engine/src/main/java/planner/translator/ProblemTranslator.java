package planner.translator;

import common.ProjectPropertiesGetter;
import model.DMDataset;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Read the data source and generate a complete problem description for JSHOP.
 *
 * @project diabetes-engine
 * @author newbiettn on 5/3/18
 *
 */

public class ProblemTranslator {
    private static Logger logger = LoggerFactory.getLogger(ProblemTranslator.class);

    //-- shortcuts
    private String LP = "(";
    private String RP = ")";
    private String NL = "\n";
    private String TAB = "\t";
    private String OUTPUTFILE;

    //-- private variables
    private StringBuilder output;
    private DMDataset dmDataset;
    private String classifier;
    private String attributeSelection;

    public ProblemTranslator(DMDataset ds,
                             String classifier,
                             String attributeSelection){
        this.OUTPUTFILE = ProjectPropertiesGetter.getSingleton().getProperty("problem.desc.file");
        this.output = new StringBuilder();
        this.dmDataset = ds;
        this.classifier = classifier;
        this.attributeSelection = attributeSelection;
    };

    //-- getters & setters
    public DMDataset getDmDataset() { return dmDataset; }
    public void setDmDataset(DMDataset dmDataset) { this.dmDataset = dmDataset; }

    /**
     * create a complete problem description for JSHOP
     * @return
     */
    public String create(){
        createProblemHeader();

        openInitialState();
        createTaskType();
        createDataMiningReq();
        createTargetClass();
        createFeature();
        createDataTable();
        createFileName();
        createDataMiningInfo();
        closeInitialState();

        createGoalState();

        createProblemFooter();
        return output.toString();
    }

    public File outputFile() throws IOException {
        File existing = new File(OUTPUTFILE);
        if (existing.canRead())
            existing.delete();
        existing.createNewFile();
        output.setLength(0); //-- empty StringBuilder before creating
        String problem = create();
        PrintWriter out = new PrintWriter(OUTPUTFILE);
        out.println(problem);
        out.close();
        return new File(OUTPUTFILE);
    }

    public void createProblemHeader(){
        this.output.append(LP).append("defproblem problem datamining").append(NL);
    }

    public void openInitialState(){
        this.output.append(TAB).append(LP).append(NL);
    }

    public void createTaskType(){
        this.output.append(TAB).append(LP).append("data-mining-task-type-instance taskType").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-val taskType BinaryClassificationTask").append(RP).append(NL);
        this.output.append(NL);
    }
    public void createDataMiningReq(){
        this.output.append(TAB).append(LP).append("data-mining-requirement-instance dataMiningRequirement").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-").append(classifier).append("-algorithm-requirement dataMiningRequirement").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-attribute-selection-algorithm dataMiningRequirement ").append(attributeSelection).append(RP).append(NL);
        this.output.append(NL);
    }
    public void createTargetClass(){
        String targetClassName = dmDataset.getTargetClass().name();
        this.output.append(TAB).append(LP).append("target-class-instance targetClass").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-name targetClass ").append(targetClassName).append(RP).append(NL);
        this.output.append(NL);
    }
    public void createFeature() {
        int i = 0;
        for (Attribute attr : dmDataset.getFeatures()){
            String fName = attr.name();
            int fIdx = attr.index();
            String fDatatype = Attribute.typeToString(attr);
            double mPercentage = dmDataset.getMissingValuePercentage(attr);
            i++;

            this.output.append(TAB).append(LP).append("feature-instance feature").append(i).append(RP).append(NL);
            this.output.append(TAB).append(LP).append("has-name feature"+i+" ").append(fName.replaceAll("[^a-zA-Z0-9_-]", "")).append("Attr").append(RP).append(NL);
            this.output.append(TAB).append(LP).append("has-index feature"+i+" ").append(fIdx).append(RP).append(NL);
            this.output.append(TAB).append(LP).append("has-data-type feature"+i+" ").append(fDatatype).append(RP).append(NL);
            this.output.append(TAB).append(LP).append("has-missing-percentage feature"+i+" ").append(mPercentage).append(RP).append(NL);
        }

        this.output.append(NL);
    }
    public void createDataTable(){
        int nSamples = dmDataset.getN(); //-- number of instances
        int nFeatures = dmDataset.getNumberofFeatures(); //-- number of features
        String fNames = dmDataset.featuresToString(); //-- concatenated string of feature names

        boolean hasMissingVal = dmDataset.hasMissingValue();
        String filetype = dmDataset.getFileType();

        this.output.append(TAB).append(LP).append("data-table-instance dataTable").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-feature dataTable ")
                .append(LP).append(fNames).append(RP).append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-target-class dataTable targetClass").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("contains-data-type dataTable Numerical").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-number-of-data-points dataTable ").append(nSamples).append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-number-of-features dataTable ").append(nFeatures).append(RP).append(NL);
        this.output.append(TAB).append(LP).append("has-input-data-file-type dataTable ").append(filetype).append(RP).append(NL);

        if (hasMissingVal){
            this.output.append(TAB).append(LP).append("has-missing-value dataTable").append(RP).append(NL);
        }

        this.output.append(NL);
    }
    public void createFileName(){
        String filename = FilenameUtils.getBaseName(dmDataset.getFilename());
        this.output.append(TAB).append(LP).append("has-file-name dataTable ").append(filename.replaceAll("[^a-zA-Z0-9_-]", "")).append(RP).append(NL);
        this.output.append(NL);
    }
    public void createDataMiningInfo(){
        this.output.append(TAB).append(LP).append("data-mining-model-instance dataMiningModel").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("data-mining-result-instance dataMiningResult").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("is-empty dataMiningResult").append(RP).append(NL);
        this.output.append(TAB).append(LP).append("is-empty dataMiningModel").append(RP).append(NL);
        this.output.append(NL);
    }

    public void closeInitialState(){
        this.output.append(TAB).append(RP).append(NL);
    }

    public void createGoalState(){
        this.output.append(TAB).append(LP).append(LP).append("generate-data-mining-workflow")
                .append(" taskType ").append(" dataMiningRequirement ").append(" dataTable ")
                .append(" dataMiningModel ").append(" dataMiningResult").append(RP).append(RP).append(NL);
    }

    public void createProblemFooter(){
        this.output.append(RP);
    }
}
