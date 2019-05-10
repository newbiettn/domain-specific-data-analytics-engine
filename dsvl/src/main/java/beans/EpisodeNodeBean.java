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
    public EpisodeNodeBean(int id, ArrayList<Pair<String, Class>> outputs) {
        super(id, outputs);
        this.sparqlValue = "?episode" + id;
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
