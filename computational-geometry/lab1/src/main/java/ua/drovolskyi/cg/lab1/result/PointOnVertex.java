package ua.drovolskyi.cg.lab1.result;

import ua.drovolskyi.cg.lab1.Graph;
import ua.drovolskyi.cg.lab1.Point;

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
}
