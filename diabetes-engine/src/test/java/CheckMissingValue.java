import JSHOP2.*;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

public class CheckMissingValue implements Calculate {
    public Term call(List l) {
        TermConstant dataFileName = (TermConstant) l.getHead();
        l = l.getRest();
        TermConstant targetClassName = (TermConstant) l.getHead();

        String fileName = dataFileName.toString();
        String className = targetClassName.toString();
        boolean hasMissingVal = false;
        String filePath = "/Users/newbiettn/OneDrive - Swinburne University/Swinburne/Github/" +
                "codes/DiabetesDiscoveryV2/diabetes-engine/src/main/resources/datasets/" + fileName;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            Instances data = new Instances(reader);
            reader.close();
            Attribute classAttr = data.attribute(className);
            data.deleteAttributeAt(classAttr.index());
            for (Enumeration<Instance> e = data.enumerateInstances(); e.hasMoreElements();){
                Instance i = e.nextElement();
                if (i.hasMissingValue()) hasMissingVal = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hasMissingVal)
            return new TermNumber(1);
        else
            return new TermNumber(-1);
    }
}
