/**
 * @author newbiettn on 19/6/18
 * @project DiabetesDiscoveryV2
 */

public class Test1 {
    public static void main(String[] args) throws
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {

        ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
        MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
        Class myObjectClass = classLoader.loadClass("MyObj");

        Executer object1 = (Executer) myObjectClass.newInstance();
        System.out.println(object1.getClass().hashCode());
        //create new class loader so classes can be reloaded.
        classLoader = new MyClassLoader(parentClassLoader);
        myObjectClass = classLoader.loadClass("MyObj");

        Executer object2 = (Executer) myObjectClass.newInstance();
        System.out.println(object2.getClass().hashCode());
    }

}
