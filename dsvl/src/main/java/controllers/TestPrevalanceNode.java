package controllers;

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
public class TestPrevalanceNode {
    static public final String NL = System.getProperty("line.separator") ;

    public static void main(String[] args) {
        int population = -1;
        int group = -1;
        float prevalance = -1;
        String prolog = "PREFIX diab: <http://www.semanticweb.org/newbiettn/ontologies/2017/11/diabetes_inpatient_study#>\n" +
                "PREFIX : <http://localhost:2020/>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>";

        // Create SELECT query
        String s1 = prolog + NL +
                "SELECT (COUNT(?x) as ?population) WHERE {\n" +
                "    ?x a diab:Patient.\n" +
                "}";
        String s2 = prolog + NL +
                "SELECT (COUNT(DISTINCT ?x) as ?group) WHERE {\n" +
                "  \t?x a diab:Patient.\n" +
                "    ?x diab:hasEpisode ?episode.\n" +
                "    ?episode diab:hasAge ?age \n" +
                "  \tFILTER (?age > 90)\n" +
                "  }";

        Query q1 = QueryFactory.create(s1);
        q1.serialize(new IndentedWriter(System.out,true)) ;
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", q1) ) {
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");
            ResultSet rs = qexec.execSelect();
            for ( ; rs.hasNext() ; ) {
                QuerySolution rb = rs.nextSolution() ;
                RDFNode x = rb.get("population") ;
                if ( x instanceof Literal ) {
                    population = ((Literal)x).getInt();
                    System.out.println("population: " + population);
                }
            }
        }

        Query q2 = QueryFactory.create(s2);
        q2.serialize(new IndentedWriter(System.out,true)) ;
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", q2) ) {
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");
            ResultSet rs = qexec.execSelect();
            for ( ; rs.hasNext() ; ) {
                QuerySolution rb = rs.nextSolution() ;
                RDFNode x = rb.get("group") ;
                if ( x instanceof Literal ) {
                    group = ((Literal)x).getInt();
                    System.out.println("group: " + group);
                }
            }
        }
        if (group != - 1 && population != -1){
            prevalance = (float)group / (float)population;
            System.out.println(prevalance);
        }
    }
}
