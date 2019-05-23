package config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for PROLOG attribute.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */
@XmlRootElement(name = "prolog")
@XmlAccessorType(XmlAccessType.FIELD)
public class Prolog {
    private String prefix;
    private String uri;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
