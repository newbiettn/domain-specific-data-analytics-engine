package org.apache.jena.ml;

/** Visitor interface specifically for MLQuery
 *
 * @project diabetes-engine
 * @author newbiettn on 2/2/18
 *
 */

public interface MLQueryVisitor {
    public void visitCreatePredictionModel(MLQuery mlQuery);

}
