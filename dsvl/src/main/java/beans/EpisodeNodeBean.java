package beans;


import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents an episode.
 *
 * @author newbiettn
 * @since  2019-03-26
 *
 */
public class EpisodeNodeBean extends ObjectBean{
    private static int count = 0;
    public EpisodeNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "episode";
        this.SPARQLClass = "diab:Episode";
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
