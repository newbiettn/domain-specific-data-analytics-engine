import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private String gender;

    Person() {
    };

    Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        foo();
        System.out.println(foo());
        return "Name:" + name + "\nAge: " + age + "\nGender: " + gender;
    }

    public static int foo(){
        System.out.println("Calling from foo!!!!!");
        return 1+1;


    }
}