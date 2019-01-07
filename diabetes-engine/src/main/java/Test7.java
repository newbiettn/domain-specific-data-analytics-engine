import common.ProjectPropertiesGetter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author newbiettn on 23/7/18
 * @project DiabetesDiscoveryV2
 */
public class Test7 {
    private static Logger logger = LoggerFactory.getLogger(Test7.class);

    public static void main (String[] args) throws IOException {
        FileInputStream stream = new FileInputStream(
                new File(ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "/best_workflow_index.txt"));
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer);
        String input = writer.toString().replace("\n", "");
        logger.info(input);
        String[] inputNumber =  input.split(",");
        logger.info(inputNumber[1].substring(1));
//        int bestWorkflowIndex = Integer.parseInt(input);
//        logger.info("The best workflow index: " + bestWorkflowIndex);

    }
}
