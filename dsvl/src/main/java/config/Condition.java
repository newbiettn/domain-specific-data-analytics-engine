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
    private ArrayList<Operator> allowedOperators;

    @XmlElement(name = "allowedDataType")
    private DataType allowedDataTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Operator> getAllowedOperators() {
        return allowedOperators;
    }

    public void setAllowedOperators(ArrayList<Operator> allowedOperators) {
        this.allowedOperators = allowedOperators;
    }

    public DataType getAllowedDataTypes() {
        return allowedDataTypes;
    }

    public void setAllowedDataTypes(DataType allowedDataTypes) {
        this.allowedDataTypes = allowedDataTypes;
    }

}
