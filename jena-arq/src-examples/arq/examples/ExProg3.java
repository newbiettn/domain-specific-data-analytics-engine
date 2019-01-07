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

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.expr.E_Regex;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.resultset.TextOutput;
import org.apache.jena.sparql.sse.writers.WriterBasePrefix;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.vocabulary.DC;

/** Example : Build a query with a filter programmatically.
 *  Note: it is often better to build and execute an algebra expression.  See other examples. */

public class ExProg3
{
    static public final String NL = System.getProperty("line.separator") ;

    static public void main(String...argv) {
        String prolog = "PREFIX : <file:///Users/newbiettn/Downloads/d2rq-0.8.1/mapping.nt#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX vocab: <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/>" ;
        String queryStr = prolog + NL + "SELECT ?pub ?title ?year " +
                "WHERE {" +
                "  ?s <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/papers_Publish> ?pub. " +
                "  ?s vocab:papers_Title ?title. " +
                "  ?s vocab:papers_Year ?year" +
                "}" +
                "LIMIT 25";
        Query query = QueryFactory.create(queryStr);
        // Remote execution.
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/D1/sparql", query) ) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);

//            ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(rs);
//            int numCols = resultSetRewindable.getResultVars().size();
//
//            StringBuilder header = new StringBuilder();
//            StringBuilder row = new StringBuilder();
//            for (int r = 0; resultSetRewindable.hasNext(); r++) {
//                QuerySolution rBind = resultSetRewindable.nextSolution();
//                for ( int col = 0 ; col < numCols ; col++ ) {
//                    String rVar = rs.getResultVars().get(col);
//                    // Print col headers
//                    if (r == 0){
//                        if (col < numCols-1)
//                            header.append(rVar).append(",");
//                        else
//                            header.append(rVar);
//                    }
//                    // Print row
//                    RDFNode obj = rBind.get(rVar);
//                    String v = FmtUtils.stringForRDFNode(obj);
//                    if (col < numCols-1)
//                        row.append(v).append(",");
//                    else
//                        row.append(v).append("\n");
//                }
//            }
//            System.out.println(header);
//            System.out.println(row);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
