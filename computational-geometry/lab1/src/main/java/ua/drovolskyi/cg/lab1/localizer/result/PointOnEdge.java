package ua.drovolskyi.cg.lab1.localizer.result;

import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.geometry.Point;

// point is on edge but not on its start or end
public class PointOnEdge extends PointLocalizationResult {
    private final Point point;
    private final Graph.Edge edge;

    public PointOnEdge(Point point, Graph.Edge edge){
        this.point = point;
        this.edge = edge;
    }

    public Point getPoint() {
        return point;
    }

    public Graph.Edge getEdge() {
        return edge;
    }

    @Override
    public String toString(){
        return "Point " + point + " is on edge {"
        + edge.getId() + ", " + edge.getStart().getId() + " -> " + edge.getEnd().getId() + "}";
    }
}
