package planner.jshop;

import JSHOP2.*;

public class CheckListEmpty implements Calculate {
    public Term call(List l) {
        TermList t = (TermList) l.getHead();
//        System.out.println(t.isNil());
        if (t.isNil())
            return new TermNumber(1);
        else
            return new TermNumber(0);
    }
}
