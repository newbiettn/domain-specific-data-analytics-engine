package org.apache.jena.sparql.lang;

/** The parser for ML queries only.
 *
 * @project diabetes-engine
 * @author newbiettn on 2/2/18
 *
 */

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.ml.MLQuery;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.Syntax;
import org.apache.jena.shared.JenaException;
import org.apache.jena.sparql.lang.sparql_11.SPARQLParser11;

import java.io.Reader;
import java.io.StringReader;

public class MLParser {
    public static MLParser createParser(Syntax syntaxURI)
    {
        return new MLParser();
    }
    private interface Action { void exec(SPARQLParser11 parser) throws Exception ; }
    public MLQuery parse$(final MLQuery mlQuery, String queryString)
    {
        Action action = new Action() {
            @Override
            public void exec(SPARQLParser11 parser) throws Exception
            {
                parser.MLQuery();
            }
        } ;

        perform(mlQuery, queryString, action) ;
        return mlQuery ;
    }

    // All throwable handling.
    private static void perform(MLQuery mlQuery, String string, MLParser.Action action)
    {
        Reader in = new StringReader(string) ;
        SPARQLParser11 parser = new SPARQLParser11(in) ;

        try {
            parser.setMLQuery(mlQuery);
            action.exec(parser) ;
        }
        catch (org.apache.jena.sparql.lang.sparql_11.ParseException ex)
        {
            throw new QueryParseException(ex.getMessage(),
                    ex.currentToken.beginLine,
                    ex.currentToken.beginColumn
            ) ; }
        catch (org.apache.jena.sparql.lang.sparql_11.TokenMgrError tErr)
        {
            // Last valid token : not the same as token error message - but this should not happen
            int col = parser.token.endColumn ;
            int line = parser.token.endLine ;
            throw new QueryParseException(tErr.getMessage(), line, col) ; }

        catch (QueryException ex) { throw ex ; }
        catch (JenaException ex)  { throw new QueryException(ex.getMessage(), ex) ; }
        catch (Error err)
        {
            System.err.println(err.getMessage()) ;
            // The token stream can throw errors.
            throw new QueryParseException(err.getMessage(), err, -1, -1) ;
        }
        catch (Throwable th)
        {
            Log.warn(ParserSPARQL11.class, "Unexpected throwable: ",th) ;
            throw new QueryException(th.getMessage(), th) ;
        }
    }
}
