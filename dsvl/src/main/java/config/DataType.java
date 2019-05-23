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
        NUMERIC("numeric"),
        CATEGORY("category");

        private String value;
        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    public String type;
    private ArrayList<String> values;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
