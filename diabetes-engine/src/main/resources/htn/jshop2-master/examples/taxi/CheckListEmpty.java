import JSHOP2.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CheckListEmpty implements Calculate {
    public Term call(List l) {
        TermList t = (TermList) l.getHead();
        if (t.isNil())
            return new TermNumber(1);
        else
            return new TermNumber(0);
    }
}
