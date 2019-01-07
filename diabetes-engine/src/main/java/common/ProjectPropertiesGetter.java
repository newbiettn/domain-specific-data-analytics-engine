package common;

import java.io.*;
import java.util.Properties;

/**
 * Read the Properties file and get corresponding value for each property.
 *
 * @author newbiettn on 6/3/18
 * @project diabetes-engine
 */

public class ProjectPropertiesGetter {
    //-- singleton
    private static final ProjectPropertiesGetter singleton = new ProjectPropertiesGetter();
    public static ProjectPropertiesGetter getSingleton() { return singleton; }

    //-- variables
    private Properties prop = new Properties();
    private String propFileName = "project.properties";

    /**
     * Private constructor to avoid public initialization.
     *
     */
    private ProjectPropertiesGetter() {
        File propFile = new File("diabetes-engine/" + propFileName);
        InputStream is = null;
        try {
            is = new FileInputStream(propFile);
            prop.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handy method to get properties values.
     *
     * @param aPropertyName
     * @return
     */
    public String getProperty(String aPropertyName) {
        return prop.getProperty(aPropertyName);
    }

}
