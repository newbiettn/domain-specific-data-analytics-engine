package model;
/**
 * An abstract class for decorators of DM Components
 * @project diabetes-engine
 * @author newbiettn on 5/3/18
 *
 */

public abstract class DMComponentDecorator extends DMComponent{
    //-- a concrete component, such as DMOperator, DMPlan
    public DMComponent com;

    /**
     * Set a concrete component.
     *
     * @param c
     */
    public void setTheComponent(DMComponent c){
       this.com = c;
    }

    /**
     * Translate a concrete component to json.
     * @return
     */
    public String translate(int seed, String filePath, boolean performOptimization){
        if (this.com != null)
            return this.com.translate(seed, filePath, performOptimization);
        return null;
    }
}
