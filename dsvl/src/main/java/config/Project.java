package config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

    @XmlElementWrapper(name = "prologs")
    @XmlElement(name = "prolog")
    private ArrayList<Prolog> prologs;

    @XmlElementWrapper(name = "conditions")
    @XmlElement(name = "condition")
    private ArrayList<Condition> conditions;

    public ArrayList<Prolog> getPrologs() {
        return prologs;
    }

    public void setPrologs(ArrayList<Prolog> prologs) {
        this.prologs = prologs;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    public Condition getConditionByName(String name){
        for( Condition c: conditions ){
            if( c.getName().equals(name) ) return c;
        }
        return null;
    }
}
