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

package org.apache.jena.fuseki.servlets;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.system.FusekiNetLib;
import org.apache.jena.fuseki.system.Upload;
import org.apache.jena.fuseki.system.UploadDetailsWithName;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.riot.web.HttpNames;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.web.HttpSC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static java.lang.String.format;
import static org.apache.jena.riot.web.HttpNames.paramQuery;

/**
 * Upload data into a graph within a dataset. This is {@code fuseki:serviceUpload}.
 * 
 * It is better to use GSP POST with the body being the content.
 * 
 * This class works with general HTML form file upload where the name is somewhere in the form and that may be
 * after the data.
 * 
 * Consider this service useful for small files and use GSP POST for large ones.
 */
public class SPARQL_ML extends ActionService
{
    public SPARQL_ML() {
        super() ;
    }

    // Methods to respond to.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doCommon(request, response) ;
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        setCommonHeadersForOptions(response) ;
        response.setHeader(HttpNames.hAllow, "OPTIONS,POST") ;
        response.setHeader(HttpNames.hContentLengh, "0") ;
    }

    @Override
    protected void validate(HttpAction action)
    {}

    @Override
    protected void perform(HttpAction action) {
        ServletOps.success(action) ;
        String queryString = action.request.getParameter(paramQuery) ;
        System.out.println(paramQuery);
        execute(queryString, action) ;
        try {
//            action.response.setContentType("text/html") ;
//            action.response.setStatus(HttpSC.OK_200);
//            PrintWriter out = action.response.getWriter() ;
//            out.println("<html>") ;
//            out.println("<head>") ;
//            out.println("</head>") ;
//            out.println("<body>") ;
//            out.println("<h1>This is an ML </h1>");
//            out.println("<p>") ;
//            out.println("<p>") ;
//            out.println("</p>") ;
//            out.println("<button onclick=\"timeFunction()\">Back to Fuseki</button>");
//            out.println("</p>") ;
//            out.println("<script type=\"text/javascript\">");
//            out.println("function timeFunction(){");
//            out.println("window.location.href = \"/fuseki.html\";}");
//            out.println("</script>");
//            out.println("</body>") ;
//            out.println("</html>") ;
//            out.flush() ;
        }
        catch (Exception ex) { ServletOps.errorOccurred(ex) ; }
    }

    protected void execute(String queryString, HttpAction action) {
        String queryStringLog = ServletOps.formatForLog(queryString);
        if (action.verbose) {
            String str = queryString;
            if (str.endsWith("\n"))
                str = str.substring(0, str.length() - 1);
            action.log.info(format("[%d] Query = \n%s", action.id, str));
        } else
            action.log.info(format("[%d] Query = %s", action.id, queryStringLog));
    }
}
