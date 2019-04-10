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

import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.graph.Node;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.ExprVars;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.util.FmtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

import java.io.*;
import java.util.*;

/** Example 1 : Execute a simple SELECT query on a model
 *  to find the DC titles contained in a model. */

public class ExML2
{
    static public final String NL = System.getProperty("line.separator") ;
    private static Logger logger = LoggerFactory.getLogger(ExML2.class);
    
    public static void main(String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        String filePath = propGetter.getProperty("sparqlml.tmp.data.filepath");
        int seed = 1;
        MetafeatureGenerator mfGen = new MetafeatureGenerator();
        DbUtils dbUtils = new DbUtils();
        int clockTimeout = 30000;
        String trainingCsv = filePath + "sparql_data_tmp.csv";
        String trainingArff = filePath + "sparql_data_tmp.arff";

        // Create the data.
        // First part or the query string
        String prolog = "PREFIX : <file:///Users/newbiettn/Downloads/d2rq-0.8.1/mapping.nt#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX vocab: <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/>" ;

        // ML query string.
        String queryString = prolog + NL +
                "PREDICT ?publish " +
                "WHERE {" +
                "?publish FEATURE vocab:papers_Publish." +
                "?title FEATURE vocab:papers_Title." +
                "?year FEATURE vocab:papers_Year" +
                "} " +
                "FILTER (?year > 2001 && ?year < 2015)" +
                "USING MODEL ?m";
        MLQuery q = MLQueryFactory.create(queryString);
        LinkedHashMap<Var, Node> cpmWhereVars = q.getCPMWhereVars();
        Var tVar = q.setTargetName();
        Var mVar = q.getModelName();
        String modelName = mVar.getVarName();
        ElementFilter filterEl = (ElementFilter)q.getFilterEle();

        // Create SELECT query
        StringBuilder whereStr = new StringBuilder();
        StringBuilder selectStr = new StringBuilder();
        for (Map.Entry<Var, Node> e : cpmWhereVars.entrySet()){
            Var v = e.getKey();
            Node n = e.getValue();
            selectStr = selectStr.append("?").append(v.getVarName()).append(" ");
            whereStr = whereStr.append("?s ").append("<").append(n.getURI()).append(">").append(" ").append("?").append(v.getVarName()).append(".").append(NL);
        }

        String selectQuery = prolog + NL +
                "SELECT " + selectStr.toString() +
                " WHERE { " + NL +
                whereStr.toString() +
                filterEl.toString() +
                " }";

        Query query = QueryFactory.create(selectQuery);
        query.serialize(new IndentedWriter(System.out,true)) ;

        // Gather as a dataset for further ML execution
        try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/D2/query", query) ) {
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
                    trainingCsv,
                    false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(header.toString() + row.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Convert CSV file to ARFF */
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(new File(trainingCsv));
        Instances data = csvLoader.getDataSet();

        // Put the target attribute to the last position to manage easier
        Attribute tAttr = data.attribute(tVar.getVarName());
        int tIndex = tAttr.index() + 1;
        StringBuilder attrIds = new StringBuilder();
        for (int i = 1; i <= data.numAttributes(); i++){
            if (i != tIndex){
                attrIds.append(i).append(",");
            }
        }
        attrIds.append(tIndex);
        Reorder reorder = new Reorder();
        reorder.setAttributeIndices(attrIds.toString());
        reorder.setInputFormat(data);
        data = Filter.useFilter(data, reorder);
        data.setClassIndex(data.numAttributes() - 1);

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(trainingArff));
        saver.writeBatch();
    }

    public static double[] normalize(double[] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < mf.length; j++){
            if (j == mf.length - 1)
                stringBuilder.append(mf[j]);
            else
                stringBuilder.append(mf[j]).append(",");
        }
        stringBuilder.append("\n");

        FileWriter fw = new FileWriter(
               "resources/R-utilities/new.item.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();

        /*Normalize by R*/
        logger.info( "Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.test.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.new.item.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[] normalizedMf = new double[mf.length];
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf.length; col++){
                normalizedMf[col] = Double.parseDouble(line[col]);
            }
        }
        return normalizedMf;
    }
    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime()
                .exec(command, null, new File("resources/R-utilities/"));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info( command + " exitValue() " + pro.exitValue());
    }
    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info( name + " " + line);
        }
    }
}
