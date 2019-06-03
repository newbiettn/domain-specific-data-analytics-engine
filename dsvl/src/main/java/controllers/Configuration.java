package controllers;

import config.*;

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
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
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

        DataType dt1 = new DataType();
        dt1.setType(DataType.Type.CATEGORY);
        ArrayList<String> values = new ArrayList<>();
        values.add("1");
        values.add("2");
        dt1.setValues(values);
        c1.setAllowedDataTypes(dt1);

        Project p = new Project();
        p.setPrologs(prologs);

        ArrayList<Condition> conditions = new ArrayList<>();
        conditions.add(c1);
        p.setConditions(conditions);

        try {
            File f = new File("resources/config/dsvl/config1.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jaxbMarshaller.marshal(p, System.out);
//            jaxbMarshaller.marshal(p, f);


//            System.out.println(Configuration.getSingleton().getProject().getPrologs());
//            for (Condition cond : Configuration.getSingleton().getProject().getConditions()){
//                System.out.println(cond.getName());
//            }
            for (Prolog proglog : Configuration.getSingleton().getProject().getPrologs()){
                System.out.println(proglog.getUri());
            }

            Endpoint endpoint = Configuration.getSingleton().getProject().getEndpoint();
            System.out.println(endpoint.getUri());

            Condition c = Configuration.getSingleton()
                    .getProject()
                    .getConditionByName("admissionnumber");
            for (Operator cond : c.getAllowedOperators()){
                System.out.println(cond);
            }
            DataType dataType = c.getAllowedDataTypes();
            for (String val : dataType.getValues()){
                System.out.println(val);
            }
//
//            for (Condition con : Configuration.getSingleton().getProject().getConditions()){
//                System.out.println(con.getName());
//            }



        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
