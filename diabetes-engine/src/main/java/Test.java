import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author newbiettn on 18/6/18
 * @project DiabetesDiscoveryV2
 */





public class Test {

    public static void main(String[] argv) throws Exception {

        URL[] urls = null;

        File dir = new File("diabetes-engine/target/classes/");
        File classFile = new File(dir,"MyClass.class");
        long lastModified = classFile.lastModified();
        URL url = dir.toURI().toURL();
        urls = new URL[] { url };

        ClassLoader cl = new URLClassLoader(urls);
        compileClass("first 1 class", dir.getAbsolutePath());
        Class cls = cl.loadClass("MyClass");
        Executer myObj = (Executer) cls.newInstance();
        System.out.println(myObj.getClass().hashCode());
        myObj.execute();

        compileClass("another 2 class", dir.getAbsolutePath());
        cl = new URLClassLoader(urls);
        cls = cl.loadClass("MyClass");
        myObj = (Executer) cls.newInstance();
        System.out.println(myObj.getClass().hashCode());
        myObj.execute();
    }

    public static void compileClass(String message, String destination) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
//        out.println("package loader;");
        out.println("public class MyClass implements Executer{");
        out.println("  public void execute() {");
        out.println("    System.out.println(\""+message+"\");");
        out.println("  }");
        out.println("}");
        out.close();
        JavaFileObject file = new JavaSourceFromString("MyClass", writer.toString());

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);

        List<String> optionList = new ArrayList<String>();

        JavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null,null);
        List<String> params = new ArrayList();
        params.add(destination);
        fileManager.handleOption("-d",params.iterator());
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnits);

        boolean success = task.call();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));

        }
        System.out.println("Success: " + success);

    }

}

class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
