import config.*;
import planner.Trainer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

/**
 * Language configuration.
 *
 * @author Ngoc Tran
 * @since 2019-05-23
 */
public class Configuration {
    private static Configuration singleton = new Configuration(new File("resources/config/dsvl/config.xml"));
    private Project project;

    public Configuration(File f){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.project =(Project) jaxbUnmarshaller.unmarshal(f);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getSingleton(){
        return singleton;
    }

    public Project getProject() {
        return project;
    }

    public static void main(String[] args){
        ArrayList<Prolog> prologs = new ArrayList<>();
        Prolog p1 = new Prolog();
        p1.setPrefix("diab");
        p1.setUri("localhost:2020/resource/");

        prologs.add(p1);

        Condition c1 = new Condition();
        c1.setName("name");
        ArrayList<Operator> allowedOperators = new ArrayList<>();
        allowedOperators.add(Operator.DIFFERENT);
        allowedOperators.add(Operator.EQUAL);
        c1.setAllowedOperators(allowedOperators);

        ArrayList<DataType> allowedDataTypes = new ArrayList<>();
        DataType dt1 = new DataType();
        dt1.setType(DataType.Type.CATEGORY);
        allowedDataTypes.add(dt1);
        c1.setAllowedDataTypes(allowedDataTypes);

        ArrayList<String> allowedValues = new ArrayList<>();
        allowedValues.add("value 1");
        allowedValues.add("value 2");
        c1.setAllowedValues(allowedValues);

        Project p = new Project();
        p.setPrologs(prologs);

        ArrayList<Condition> conditions = new ArrayList<>();
        conditions.add(c1);
        p.setConditions(conditions);

        try {
            File f = new File("resources/config/dsvl/config.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(p, System.out);
//            jaxbMarshaller.marshal(p, f);


            for (Condition cond : Configuration.getSingleton().getProject().getConditions()){
//                System.out.println(cond.getName());
            }
            System.out.println(Configuration.getSingleton().getProject().getConditionByName("age"));


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
