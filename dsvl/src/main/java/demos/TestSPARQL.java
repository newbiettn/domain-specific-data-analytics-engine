package demos;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-13
 */
public class TestSPARQL {
    public static void main(String[] args){
        String selectQuery = "SELECT ?subject ?predicate ?object\n" +
                "WHERE {" +
                "  ?subject ?predicate ?object" +
                "}" +
                "LIMIT 25";
        Query query = QueryFactory.create(selectQuery);
        query.serialize(new IndentedWriter(System.out,true)) ;

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", query);
        ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

        // Execute.
        ResultSet rs = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(rs);
        int numCols = resultSetRewindable.getResultVars().size();
        StringBuilder header = new StringBuilder();
        StringBuilder row = new StringBuilder();
        for (int r = 0; resultSetRewindable.hasNext(); r++) {
            QuerySolution rBind = resultSetRewindable.nextSolution();
            for ( int col = 0 ; col < numCols ; col++ ) {
                String rVar = rs.getResultVars().get(col);
                // Print col headers
                if (r == 0){
                    if (col < numCols-1)
                        header.append(rVar).append(",");
                    else
                        header.append(rVar).append("\n");
                }
                // Print row
                RDFNode obj = rBind.get(rVar);
                String v = FmtUtils.stringForRDFNode(obj);
                if (col < numCols-1)
                    row.append(v).append(",");
                else
                    row.append(v).append("\n");
            }
        }
        System.out.print(row.toString());
    }
}
