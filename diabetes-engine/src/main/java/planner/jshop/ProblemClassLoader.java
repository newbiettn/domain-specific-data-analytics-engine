package planner.jshop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author newbiettn on 18/6/18
 * @project DiabetesDiscoveryV2
 */

public class ProblemClassLoader extends ClassLoader{

    public ProblemClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        if(!"planner.jshop.problem".equals(name))
            return super.loadClass(name);

        try {
            String url = "file:/Users/newbiettn/Dropbox/Swinburne/Github/codes/DiabetesDiscoveryV2/diabetes-engine/target/classes/planner/jshop/problem.class";
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass("planner.jshop.problem",
                    classData, 0, classData.length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
