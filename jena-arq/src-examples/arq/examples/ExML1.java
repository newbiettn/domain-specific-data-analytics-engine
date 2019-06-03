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
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.ml.MLQueryFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

import java.io.*;
import java.util.*;

/**
 * Example 1 : Execute a simple SELECT query on a model
 * to find the DC titles contained in a model.
 */

public class ExML1 {
    static public final String NL = System.getProperty("line.separator");
    private static Logger logger = LoggerFactory.getLogger(ExML1.class);

    public static void main(String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        String filePath = propGetter.getProperty("sparqlml.training.data.filepath");

        // Create the data.
        // First part or the query string
        String prolog = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX diab: <http://localhost:2020/resource/>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX fo: <http://www.w3.org/1999/XSL/Format#>";

        // ML query string.
        String queryString = prolog + NL +
                "CREATE PREDICTION MODEL" +
                "TARGET ?d " +
                "DESCRIBE {" +
                "FEATURE ?age." +
                "FEATURE ?gender." +
                "FEATURE ?hba1c" +
                "} " +
                "WHERE {" +
                "?e rdf:type diab:Episode." +
                "?e diab:hasAge ?age." +
                "?e diab:hasGender ?gender." +
                "?e diab:hasHbA1cTestResult ?hba1c." +
                "?e diab:hasAdmissionNumber ?admissionNumber." +
                "?e diab:hasDeceased ?d." +
                "FILTER (?admissionNumber = 1)." +
                "} " +
                "SAVE MODEL 'modelName'";
        MLQuery q = MLQueryFactory.create(queryString);
        ArrayList<Var> featureVars = q.getFeatureVars();
        Var tVar = q.setTargetName();
        String modelName = q.getModelFileName();

        Query selectQuery = QueryFactory.make();
        selectQuery.setQuerySelectType();
        selectQuery.setQueryPattern(q.getQueryPattern());
        selectQuery.getPrefixMapping().setNsPrefix("diab", "http://localhost:2020/resource/");
        selectQuery.getPrefixMapping().setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
        selectQuery.getPrefixMapping().setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns");
        selectQuery.addResultVar(Var.alloc(tVar.getVarName()));
        for (Var v : featureVars) {
            Var var = Var.alloc(v.getVarName());
            selectQuery.addResultVar(var);
        }


        selectQuery.serialize(new IndentedWriter(System.out, true));
        System.out.println();

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/austin/query", selectQuery)) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            // Execute.
            ResultSet rs = qexec.execSelect();
            String result = convertResultSet(rs);

            // Create Instances type from string
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(new ByteArrayInputStream(result.getBytes()));
            Instances data = csvLoader.getDataSet();

            // Put the target attribute to the last position to manage easier
            Attribute tAttr = data.attribute(tVar.getVarName());
            int tIndex = tAttr.index() + 1;
            StringBuilder attrIds = new StringBuilder();
            for (int i = 1; i <= data.numAttributes(); i++) {
                if (i != tIndex) {
                    attrIds.append(i).append(",");
                }
            }
            attrIds.append(tIndex);
            Reorder reorder = new Reorder();
            reorder.setAttributeIndices(attrIds.toString());
            reorder.setInputFormat(data);
            data = Filter.useFilter(data, reorder);
            data.setClassIndex(data.numAttributes() - 1);

            // Store training file for further prediction
            String trainingArff = filePath + "training_" + modelName + ".arff";
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(trainingArff));
            saver.writeBatch();

            Trainer.getSingleton().executeForSPARQLML(trainingArff, modelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("=========================================================================================");
    }

    public static double[] normalize(double[] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < mf.length; j++) {
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
        logger.info("Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.test.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.new.item.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[] normalizedMf = new double[mf.length];
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf.length; col++) {
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
        logger.info(command + " exitValue() " + pro.exitValue());
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info(name + " " + line);
        }
    }

    /**
     * Convert ResultSet to a string of CSV format for further manipulation.
     *
     * @param rs
     * @return
     */
    public static String convertResultSet(ResultSet rs) {
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(rs);
        int numCols = resultSetRewindable.getResultVars().size();
        StringBuilder header = new StringBuilder();
        StringBuilder row = new StringBuilder();
        for (int r = 0; resultSetRewindable.hasNext(); r++) {
            QuerySolution rBind = resultSetRewindable.nextSolution();
            for (int col = 0; col < numCols; col++) {
                String rVar = rs.getResultVars().get(col);
                // Print col headers
                if (r == 0) {
                    if (col < numCols - 1)
                        header.append(rVar).append(",");
                    else
                        header.append(rVar).append("\n");
                }
                // Print row
                RDFNode obj = rBind.get(rVar);
                Literal l = obj.asLiteral();
                String v = l.getString();
                if (col < numCols - 1)
                    row.append(v).append(",");
                else
                    row.append(v).append("\n");
            }
        }
        return (header.toString() + row.toString());
    }
}
