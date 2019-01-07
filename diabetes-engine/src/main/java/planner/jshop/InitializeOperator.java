package planner.jshop;

/** To initialize Java class on-the-fly while finding plan.
  *
  * @project diabetes-engine
  * @author newbiettn on 27/2/18
  *
  **/


import JSHOP2.*;
import javafx.util.Pair;
import planner.Trainer;

import java.util.ArrayList;


public class InitializeOperator implements Calculate {
    public Term call(List l) {
        Pair<String, java.util.List> opConfig;
        String opName;
        java.util.List<String> params = new ArrayList<String>();

        //-- operator
        TermConstant t = (TermConstant) l.getHead();
        opName = t.toString();

        //-- params of that operator
        l = ((TermList)l.getRest().getHead()).getList();
//        System.out.println(t.toString());
        while (l != null){
            String param;
            //-- if list have > 1 element
            if (l.getTail() instanceof TermList){
                param = l.getHead().toString();
                l = l.getRest();
            } else {
                param = l.getHead().toString();
            }
            params.add(param);
        }
        opConfig = new Pair(opName, params);
        Trainer.getSingleton().initializeOperator(opConfig);
        return new TermNumber(1);
    }
}