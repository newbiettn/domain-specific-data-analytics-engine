
/*
 *
 * @project diabetes-engine
 * @author newbiettn on 19/2/18
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class App {
    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command, null, new File("/Users/newbiettn/Dropbox/Swinburne/Github/codes/DiabetesDiscoveryV2/diabetes-engine/src/test/java"));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    public static void main(String[] args) {
        try {
            runProcess("java JSHOP2.InternalDomain datamining");
            runProcess("java JSHOP2.InternalDomain -r problem");
//            runProcess("javac gui.java");
//            runProcess("rm datamining.txt problem.java datamining.java");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
