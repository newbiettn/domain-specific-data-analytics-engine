package org.apache.jena.ml;

/** The factory to create concrete MLQuery objects from the input string.
 *  The factory will invoke @see MLParser to parse the input string, and then
 *  create corresponding MLQuery objects.
 *
 * @project diabetes-engine
 * @author newbiettn on 2/2/18
 *
 */

import org.apache.jena.query.QueryException;
import org.apache.jena.query.Syntax;
import org.apache.jena.riot.system.IRIResolver;
import org.apache.jena.sparql.lang.MLParser;

public class MLQueryFactory {
    /** Create a ML query from the given string.
     *
     * @param queryString      The query string
     * @throws QueryException  Thrown when a parse error occurs
     */

    static public MLQuery create(String queryString) {
        MLQuery mlQuery = new MLQuery() ;
        String baseURI = null;
        return parse(mlQuery, queryString, baseURI, Syntax.defaultQuerySyntax) ;
    }

    /** Parse a query from the given string by calling the parser.
     *
     * @param mlQuery          Existing, uninitialized query
     * @param queryString      The query string
     * @param baseURI          URI for relative URI expansion
     * @param syntaxURI        URI for the syntax
     * @throws QueryException  Thrown when a parse error occurs
     */

    static public MLQuery parse(MLQuery mlQuery, String queryString, String baseURI, Syntax syntaxURI) {
        MLParser parser = MLParser.createParser(syntaxURI) ;

        if ( parser == null )
            throw new UnsupportedOperationException("Unrecognized syntax for parsing: "+syntaxURI) ;

        if ( mlQuery.getResolver() == null ) {
            IRIResolver resolver = null ;
            try {
                if ( baseURI != null ) {
                    // Sort out the baseURI - if that fails, dump in a dummy one and continue.
                    resolver = IRIResolver.create(baseURI) ;
                }
                else {
                    resolver = IRIResolver.create() ;
                }
            }
            catch (Exception ex) {}
            if ( resolver == null )
                resolver = IRIResolver.create("http://localhost/query/defaultBase#") ;
            mlQuery.setResolver(resolver) ;

        }
        return parser.parse$(mlQuery, queryString) ;
    }

}
