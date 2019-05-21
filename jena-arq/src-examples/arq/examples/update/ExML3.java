package arq.examples.update;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-16
 */
public class ExML3 {
    static public final String NL = System.getProperty("line.separator") ;

    public static void main(String[] args) {
        String prolog = "PREFIX diab: <http://www.semanticweb.org/newbiettn/ontologies/2017/11/diabetes_inpatient_study#>\n" +
                "PREFIX : <http://localhost:2020/>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>";

        // Create SELECT query
        String selectQuery = prolog + NL +
                "SELECT (COUNT(?x) as ?population) WHERE {\n" +
                "    ?x a diab:Patient.\n" +
                "}";

        Query query = QueryFactory.create(selectQuery);
        query.serialize(new IndentedWriter(System.out,true)) ;

        // Gather as a dataset for further ML execution
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", query) ) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");
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
            System.out.print(header.toString());
            System.out.print(row.toString());
        }

    }

}
