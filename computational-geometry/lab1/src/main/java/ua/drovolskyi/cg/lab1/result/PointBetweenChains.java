package ua.drovolskyi.cg.lab1.result;

import ua.drovolskyi.cg.lab1.Graph;
import ua.drovolskyi.cg.lab1.Point;

// when point is exactly between edges, it isn't on one of edges
public class PointBetweenChains extends PointLocalizationResult {
    private final Point point;
    private final Graph.Edge leftEdge;
    private final Graph.Edge rightEdge;

    public PointBetweenChains(Point point, Graph.Edge leftEdge, Graph.Edge rightEdge){
        this.point = point;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public Point getPoint() {
        return point;
    }

    public Graph.Edge getLeftEdge() {
        return leftEdge;
    }

    public Graph.Edge getRightEdge() {
        return rightEdge;
    }
}
