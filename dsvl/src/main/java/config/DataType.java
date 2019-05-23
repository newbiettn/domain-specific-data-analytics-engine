package config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */
@XmlRootElement(name = "dataType")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataType {
    public enum Type {
        NUMERIC,
        CATEGORY
    }
    public Type type;
    private ArrayList<String> values;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
