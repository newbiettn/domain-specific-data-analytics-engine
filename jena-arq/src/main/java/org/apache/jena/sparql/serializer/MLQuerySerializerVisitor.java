package org.apache.jena.sparql.serializer;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryVisitor;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.core.VarExprList;
import org.apache.jena.sparql.expr.Expr;

import java.io.OutputStream;

/** Do serializer for MLQuery
 *
 * @project diabetes-engine
 * @author newbiettn on 2/2/18
 *
 */

public class MLQuerySerializerVisitor implements MLQueryVisitor {
    static final int BLOCK_INDENT = 2 ;
    protected FormatterTemplate fmtTemplate ;
    protected FormatterElement fmtElement ;
    protected FmtExprSPARQL fmtExpr ;
    protected IndentedWriter out = null ;

    MLQuerySerializerVisitor(IndentedWriter      iwriter,
                    FormatterElement    formatterElement,
                    FmtExprSPARQL       formatterExpr,
                    FormatterTemplate   formatterTemplate)
    {
        out = iwriter ;
        fmtTemplate = formatterTemplate ;
        fmtElement = formatterElement ;
        fmtExpr = formatterExpr ;
    }

    @Override
    public void visitCreatePredictionModel(MLQuery mlQuery) {
        out.print("CREATE PREDICTION MODEL ");
//        out.print(mlQuery.getModel().getMLModelName());
        out.newline();
    }

}
