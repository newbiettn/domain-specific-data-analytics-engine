package controllers;

import beans.EpisodeNodeBean;
import beans.PatientNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for episode entities.
 *
 * @author newbiettn
 * @since 2019-03-26
 *
 */
public class EpisodeNodeController {
    private VNode node;

    private EpisodeNodeBean episodeNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField variableEpisodeNode;

    public EpisodeNodeController() {
    }

    @FXML
    public void initialize() {
    }

    public VNode getNode() {
        return node;
    }

    public void setNode(VNode node) {
        this.node = node;
    }

    public EpisodeNodeBean getEpisodeNodeBean() {
        return episodeNodeBean;
    }

    public void setEpisodeNodeBean(EpisodeNodeBean episodeNodeBean) {
        this.episodeNodeBean = episodeNodeBean;
    }

}
