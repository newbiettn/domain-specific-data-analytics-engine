import JSHOP2.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TestCustomFunc implements Calculate {
    public Term call(List l) {
        TermConstant x1N = (TermConstant) l.getHead();
        l = l.getRest();
        TermConstant x2N = (TermConstant) l.getHead();

//        double x1 = (double) x1N.getNumber();
//        double x2 = (double) x2N.getNumber();

//        System.out.println(x1 + x2);
        return new TermNumber(1);
    }
}
