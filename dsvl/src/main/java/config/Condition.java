package config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Entity class for CONDITION attribute.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */

@XmlRootElement(name = "condition")
@XmlAccessorType(XmlAccessType.FIELD)
public class Condition {
    private String name;

    @XmlElementWrapper(name = "allowedOperators")
    @XmlElement(name = "operator")
    private ArrayList<String> allowedOperators;

    @XmlElementWrapper(name = "allowedDataTypes")
    @XmlElement(name = "dataType")
    private ArrayList<DataType> allowedDataTypes;

    @XmlElementWrapper(name = "allowedValues")
    @XmlElement(name = "value")
    private ArrayList<String> allowedValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAllowedOperators() {
        return allowedOperators;
    }

    public void setAllowedOperators(ArrayList<String> allowedOperators) {
        this.allowedOperators = allowedOperators;
    }

    public ArrayList<DataType> getAllowedDataTypes() {
        return allowedDataTypes;
    }

    public void setAllowedDataTypes(ArrayList<DataType> allowedDataTypes) {
        this.allowedDataTypes = allowedDataTypes;
    }

    public ArrayList<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(ArrayList<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

}
