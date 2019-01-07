/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arq.examples;


// The ARQ application API.
import org.apache.jena.atlas.io.IndentedWriter ;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.* ;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.vocabulary.DC ;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/** Example 1 : Execute a simple SELECT query on a model
 *  to find the DC titles contained in a model. */

public class ExQuerySelect1
{
    static public final String NL = System.getProperty("line.separator") ; 
    
    public static void main(String[] args) {
        // Create the data.
        // First part or the query string
        String prolog = "PREFIX : <file:///Users/newbiettn/Downloads/d2rq-0.8.1/mapping.nt#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX vocab: <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/>" ;

        // Query string.
        String queryString = prolog + NL +
                "CREATE PREDICTION MODEL ?m " +
                "TARGET ?survive " +
                "WHERE {" +
                "?pub FEATURE vocab:papers_Publish." +
                "?title FEATURE vocab:papers_Title." +
                "?year FEATURE vocab:papers_Year." +
                "}";
        MLQuery q = MLQueryFactory.create(queryString);
        LinkedHashMap<Var, Node> cpmWhereVars = q.getCPMWhereVars();
        StringBuilder whereStr = new StringBuilder();
        for (Map.Entry<Var, Node> e : cpmWhereVars.entrySet()){
            Var v = e.getKey();
            Node n = e.getValue();
            whereStr = whereStr.append("?s ").append("<").append(n.getURI()).append(">").append(" ").append("?").append(v.getVarName()).append(".").append(NL);
        }
//        System.out.println(whereStr);

//        query.serialize(new IndentedWriter(System.out,true)) ;
        // Print with line numbers
        Log.info("MLQuery", "Successfully parse MLQuery");

        // Create SELECT query
        String selectQuery = prolog + NL +
                "SELECT ?pub ?title ?year WHERE { " + NL +
                whereStr.toString() +
                " }";
        System.out.println(selectQuery);

        Query query = QueryFactory.create(selectQuery);
        // Remote execution.
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/d1/sparql", query) ) {
            // Set the DBpedia specific timeout.
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
            // Save to CSV
            FileWriter fw = new FileWriter(
                    "sparql_data_tmp.csv",
                    false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(header.toString() + row.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
