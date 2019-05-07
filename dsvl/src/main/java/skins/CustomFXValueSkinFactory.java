package skins;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.*;
import eu.mihosoft.vrl.workflow.skin.ConnectionSkin;
import eu.mihosoft.vrl.workflow.skin.VNodeSkin;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Custom skins for connection as well as node.
 *
 * @author Ngoc Tran
 * @since 2019-04-02
 */
public class CustomFXValueSkinFactory extends FXSkinFactory {
    private Map<String, Class<? extends CustomFXFlowNodeSkinBase>> valueSkins = new HashMap<>();
    private final Map<String, Class<? extends FXConnectionSkin>> connectionSkins = new HashMap<>();

    private Class<? extends CustomFXFlowNodeSkinBase> defaultNodeSkinClass = CustomFXFlowNodeSkinBase.class;
    private Class<? extends CustomFXConnectionSkin> defaultConnectionSkinClass = CustomFXConnectionSkin.class;

    public CustomFXValueSkinFactory(Parent parent) {
        super(parent);
    }
    private ConnectionSkin customConnectionSkin;

    @Override
    public ConnectionSkin createSkin(Connection c, VFlow flow, String type) {
        customConnectionSkin = new CustomFXConnectionSkin(this, getFxParent(), c, flow, type).init();
        return customConnectionSkin;
    }

    @Override
    public VNodeSkin createSkin(VNode n, VFlow flow) {
        return chooseNodeSkin(n, flow);
    }

    public ConnectionSkin getCustomConnectionSkin() {
        return customConnectionSkin;
    }

    /**
     * Adds a skin class for the specified value type.
     * @param valueType value type
     * @param skinClass skin class
     */
    public void addSkinClassForValueType(Class<?> valueType,
                                         Class<? extends CustomFXFlowNodeSkinBase> skinClass) {

        boolean notAvailable = true;

        // check whether correct constructor is available
        try {
            Constructor<?> constructor
                    = CustomFXFlowNodeSkinBase.class.getConstructor(
                    FXSkinFactory.class, VNode.class, VFlow.class);
            notAvailable = false;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FXValueSkinFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FXValueSkinFactory.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        // we cannot accept the specified skin class as it does not provide
        // the required constructor
        if (notAvailable) {
            throw new IllegalArgumentException(
                    "Required constructor missing: ("
                            + FXSkinFactory.class.getSimpleName()
                            + ", " + VNode.class.getSimpleName() + ", "
                            + VNode.class.getSimpleName() + ")");
        }

        valueSkins.put(valueType.getName(), skinClass);
    }

    /**
     * Chooses the best matching node skin depending on the value object.
     *
     * @param n node
     * @param flow parent flow
     * @return best matching node skin depending on the value object or the
     * default node skin if no matching node skin exists
     */
    private CustomFXFlowNodeSkin chooseNodeSkin(VNode n, VFlow flow) {

        Object value = n.getValueObject().getValue();

        if (value == null) {
            return createSkinInstanceOfClass(getDefaultNodeSkin(), n, flow);
        }

        Class<?> valueClass = value.getClass();
        Class<? extends CustomFXFlowNodeSkinBase> skinClass = null;

        while (skinClass == null && valueClass != null) {
            skinClass = valueSkins.get(valueClass.getName());
            valueClass = valueClass.getSuperclass();
        }

        if (skinClass == null) {

            skinClass = getDefaultNodeSkin();
        }

        return createSkinInstanceOfClass(skinClass, n, flow);
    }
    /**
     * Creates an instance of the specified skin class.
     *
     * @param skinClass skin class
     * @param n flow node that shall be visualized by the skin
     * @param flow parent flow
     * @return an instance of the specified skin class that represents node
     * {@code n} or {@code null} if the specified class cannot be instantiated
     */
    private CustomFXFlowNodeSkin createSkinInstanceOfClass(
            Class<? extends CustomFXFlowNodeSkinBase> skinClass, VNode n, VFlow flow) {
        try {

            Constructor<?> constructor
                    = skinClass.getConstructor(
                    FXSkinFactory.class, VNode.class, VFlow.class);

            CustomFXFlowNodeSkin skin
                    = (CustomFXFlowNodeSkin) constructor.newInstance(this, n, flow);

            return skin;

        } catch (NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | InstantiationException ex) {
            Logger.getLogger(FXValueSkinFactory.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * @return the defaultNodeSkin
     */
    public Class<? extends CustomFXFlowNodeSkinBase> getDefaultNodeSkin() {
        return defaultNodeSkinClass;
    }

    /**
     * @param defaultNodeSkin the defaultNodeSkin to set
     */
    public void setDefaultNodeSkin(Class<? extends CustomFXFlowNodeSkinBase> defaultNodeSkin) {

        Objects.requireNonNull(defaultNodeSkin);

        this.defaultNodeSkinClass = defaultNodeSkin;
    }


}
