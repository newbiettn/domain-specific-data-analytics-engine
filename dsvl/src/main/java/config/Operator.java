package config;

/**
 * Entity class for OPERATOR attribute.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */
public enum Operator {
    SMALLER("<"),
    LARGER(">"),
    EQUAL("="),
    DIFFERENT("!=");

    private String value;

    Operator(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

}
