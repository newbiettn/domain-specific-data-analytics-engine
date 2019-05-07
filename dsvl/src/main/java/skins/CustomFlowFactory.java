package skins;

import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.skin.ConnectionSkin;
import eu.mihosoft.vrl.workflow.skin.SkinFactory;
import eu.mihosoft.vrl.workflow.skin.VNodeSkin;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-07
 */
public class CustomFlowFactory {
    /**
     * Creates a new instance of a flow
     * @return the newly created {@code VFlow}
     */
    public static VFlow newFlow() {

        VFlowModel model = CustomFlowFactory.newFlowModel();

        VFlow flow = new VFlowImpl(null,model);

        return flow;
    }

    /**
     * Creates a new instance of a flow and specify its skin
     * @param skinFactory Defines the skin factory used to render the workflow
     * @return the newly created {@code VFlow}
     */
    public static VFlow newFlow(
            SkinFactory<? extends ConnectionSkin, ? extends VNodeSkin> skinFactory) {

        VFlowModel model = CustomFlowFactory.newFlowModel();

        VFlow flow = new VFlowImpl(null,model, skinFactory);

        return flow;
    }

    /**
     * Creates a new flow model
     * @return
     */
    public static VFlowModel newFlowModel() {
        VFlowModel result = new CustomVFlowModelImpl(null);
        result.setId("ROOT");
        return result;
    }

    /**
     * Returns a new id generator.
     * @return id generator
     */
    public static IdGenerator newIdGenerator() {
        return new IdGeneratorImpl();
    }
}
