package ua.drovolskyi.cg.lab1.localizer.result;

import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.geometry.Point;

public class PointOnVertex extends PointLocalizationResult {
    private final Point point;
    private final Graph.Vertex vertex;

    public PointOnVertex(Point point, Graph.Vertex vertex){
        this.point = point;
        this.vertex = vertex;
    }

    public Point getPoint() {
        return point;
    }

    public Graph.Vertex getVertex() {
        return vertex;
    }

    @Override
    public String toString(){
        return "Point " + point + " is on vertex " + vertex;
    }
}
