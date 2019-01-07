package model;
/**
 * An abstract class to describe DM components
 *
 * @project diabetes-engine
 * @author newbiettn on 5/3/18
 *
 */

public abstract class DMComponent {
    public abstract String translate(int seed, String filePath, boolean performOptimization);
}
