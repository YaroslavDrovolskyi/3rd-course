package ua.drovolskyi.cg.lab1.localizer.result;

import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.geometry.Point;

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

    @Override
    public String toString(){
        return "Point " + point + " is between edges " +
        "{" + leftEdge.getId() + ", " + leftEdge.getStart().getId() + " -> " + leftEdge.getEnd().getId() + "}" +
        " and " +
        "{" + rightEdge.getId() + ", " + rightEdge.getStart().getId() + " -> " + rightEdge.getEnd().getId() + "}";
    }
}
