package planner.jshop;

import JSHOP2.Calculate;
import JSHOP2.List;
import JSHOP2.Term;
import JSHOP2.TermList;

public class GetTail implements Calculate {
    public Term call(List l) {
        TermList t = (TermList) l.getHead();
        List list = t.getList();
//        System.out.println(list.getTail());

        return list.getTail();
    }
}
