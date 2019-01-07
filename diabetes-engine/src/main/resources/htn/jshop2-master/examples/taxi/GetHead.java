import JSHOP2.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class GetHead implements Calculate {
    public Term call(List l) {
        TermList t = (TermList) l.getHead();
        List list = t.getList();

        return  list.getHead();

    }
}
