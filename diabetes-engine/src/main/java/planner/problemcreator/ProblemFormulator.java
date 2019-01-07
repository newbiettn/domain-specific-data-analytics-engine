package planner.problemcreator;

import model.DMDataset;

/**
 * Scan the input, which are likely datasets, and extract all metadata to formulate planning problem.
 *
 * @author newbiettn on 6/3/18
 * @project diabetes-engine
 */

public class ProblemFormulator {
    private DMDataset ds;

    /**
     * tClass by column name
     */
    public ProblemFormulator(DMDataset ds){
        this.ds = ds;
    }

    public void setDs(DMDataset ds) {
        this.ds = ds;
    }

    public DMDataset getDs() {
        return ds;
    }

}
