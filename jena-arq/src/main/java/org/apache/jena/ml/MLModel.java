package org.apache.jena.ml;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;

import java.util.LinkedHashMap;

public class MLModel {
    private String name;
    private String target;
    private LinkedHashMap<Var, Node> vars;

    public MLModel() {
        this.vars = new LinkedHashMap<>();
    }

    /**
     *
     * @return
     */
    public LinkedHashMap<Var, Node> getVars() { return vars; }
    public String getTarget() { return target; }
    public String getName() { return name; }
    public void setTarget(String target) { this.target = target; }
    public void setName(String name) { this.name = name; }
    public void setVars(LinkedHashMap<Var, Node> vars) { this.vars = vars; }

}
