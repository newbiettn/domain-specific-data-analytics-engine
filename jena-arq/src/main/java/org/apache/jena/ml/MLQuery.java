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
    private LinkedHashMap<Var, Node> cpmWhereVars = new LinkedHashMap<>();
    private Var modelName;
    private Var targetName;

    // ---- PREDICT --------------------------------------------------------
    Element filterEle;

    public Element getFilterEle() {
        return filterEle;
    }

    public void setFilterEle(Element filterEle) {
        this.filterEle = filterEle;
    }

    /** Set variable model name.
     *
     * @param v corresponding to SPARQL variables, such as ?s, ?p
     */
    public void setModelName(Var v) {
        if ( !v.isVariable() )
            throw new QueryException("Not a variable: "+v) ;
//        model.setName(v.getName());
        this.modelName = v;
    }

    /** Get name variable.
     */
    public Var getModelName(){
        return this.modelName;
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
     * @param n corresponding to OWL classes, such as Patient, Episodes
     */
    public void addCPMWhereVars(Var v, Node n){
        if (this.cpmWhereVars.containsKey(v)){
            throw new QueryException("Duplicate variable: "+v);
        }
        this.cpmWhereVars.put(v, n);
    }

    /**
     * Return variables of WHERE of the query
     *
     * @return LinkedHashMap of variables and their corresponding graph nodes.
     */
    public LinkedHashMap<Var, Node> getCPMWhereVars(){
        return this.cpmWhereVars;
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
