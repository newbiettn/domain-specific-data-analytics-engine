package org.apache.jena.ml;

/** The data structure for ML query.
 *
 * @project diabetes-engine
 * @author newbiettn on 31/1/18
 *
 */

import org.apache.jena.graph.Node;
import org.apache.jena.query.QueryException;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MLQuery extends Prologue{
    // There are different ML query types
    int queryType = QueryTypeUnknown ;
    public static final int QueryTypeUnknown                      = -123 ;
    public static final int QueryTypeCreatePredictionModel        = 555 ;
    public static final int QueryTypePredict                      = 555 ;

    public int getQueryType()                    { return queryType ; }

    public void setQueryCreatePredictionModelType() { queryType = QueryTypeCreatePredictionModel ; }

    public boolean isCreatePredictionModelType() { return queryType == QueryTypeCreatePredictionModel; }

    public void setQueryPredictType() { queryType = QueryTypePredict; }

    public boolean isCreatePredictType() { return queryType == QueryTypePredict; }

    private MLModel model;
    public MLModel getModel() { return model; }

    // The WHERE clause
    private Element queryPattern = null ;

    /** Create a new empty ML query
     *
     */
    public MLQuery(){
        this.model = new MLModel();
    }

    // ---- CREATE PREDICTION MODEL ----------------------------------------
    private ArrayList<Var> featureVars = new ArrayList<>();
    private String modelFileName;
    private Var targetName;
    private String learningAlgorithmName;
    private boolean interpretability;

    // ---- PREDICT --------------------------------------------------------
    Element filterEle;

    public Element getFilterEle() {
        return filterEle;
    }

    public void setFilterEle(Element filterEle) {
        this.filterEle = filterEle;
    }

    /**
     * Get interpretability.
     * @return
     */
    public boolean isInterpretability() {
        return interpretability;
    }

    /**
     * Set interpretability
     * @param interpretability
     */
    public void setInterpretability(boolean interpretability) {
        this.interpretability = interpretability;
    }

    /**
     * Get learning algorithm.
     * @return
     */
    public String getLearningAlgorithmName() {
        return learningAlgorithmName;
    }

    /**
     * Set learning algorithm.
     * @param learningAlgorithmName
     */
    public void setLearningAlgorithmName(String learningAlgorithmName) {
        this.learningAlgorithmName = learningAlgorithmName;
    }

    /** Set model file name
     *
     * @param modelFileName
     */
    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    /** Get name variable.
     */
    public String getModelFileName(){
        return this.modelFileName;
    }

    /** Set target variable.
     *
     * @param v corresponding to SPARQL variables, such as ?s, ?p
     */
    public void setTargetName(Var v) {
        if ( !v.isVariable() )
            throw new QueryException("Not a variable: "+v) ;
        this.targetName = v;
    }

    /** Return the target variable
     *
     * @return
     */
    public Var setTargetName(){
        return this.targetName;
    }

    /** Add feature descriptions of ML model from query.
     *  Those feature descriptions will be used to generate SELECT queries to gather the data
     *
     * @param v corresponding to SPARQL variables that will be made later, such as ?s, ?p
     */
    public void addFeatureVar(Var v){
        if (this.featureVars.contains(v)){
            throw new QueryException("Duplicate variable: "+v);
        }
        this.featureVars.add(v);
    }

    /**
     * Return variables of WHERE of the query
     *
     * @return LinkedHashMap of variables and their corresponding graph nodes.
     */
    public ArrayList<Var> getFeatureVars(){
        return this.featureVars;
    }

    /**
     * WHERE clause
     *
     * @param elt
     */
    public void setQueryPattern(Element elt) {
        this.queryPattern = elt ;
    }

    public Element getQueryPattern() { return queryPattern ; }

    /** Visitor pattern for MLQuery
     *
     * @param visitor
     */
    public void visit(MLQueryVisitor visitor){
        if (this.isCreatePredictionModelType()){
            visitor.visitCreatePredictionModel(this);
        } else if (this.isCreatePredictType()){
            visitor.visitPredict(this);
        }
    }

}
