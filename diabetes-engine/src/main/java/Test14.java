import db.DbUtils;

public class Test14 {
    public static void main (String[] args){
        DbUtils dbUtils = new DbUtils();
//        dbUtils.viewAllDatasetCluster();
//        dbUtils.viewAllBestWorkflowByCluster();
        dbUtils.viewAllSummarizedExperimentHistoryTable();
    }
}
