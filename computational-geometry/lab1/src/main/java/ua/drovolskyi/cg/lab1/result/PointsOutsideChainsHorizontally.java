package ua.drovolskyi.cg.lab1.result;

import ua.drovolskyi.cg.lab1.Graph;
import ua.drovolskyi.cg.lab1.Point;

public class PointsOutsideChainsHorizontally extends PointLocalizationResult {
    private final Point point;
    private final Graph.Edge edge;
    private final int side;

    public PointsOutsideChainsHorizontally(Point point, Graph.Edge edge, int side){
        this.point = point;
        this.edge = edge;
        this.side = side;
    }

    public Point getPoint() {
        return point;
    }

    // returns -1 if point is left to edge, and 1 if point is right to edge
    public int getSide() {
        return side;
    }

    public Graph.Edge getEdge() {
        return edge;
    }

    @Override
    public String toString(){
        String sideStr = (side == -1) ? "left" : "right";
        return "Point " + point + " is "  + sideStr + " to edge {" +
                edge.getId() + ", " + edge.getStart().getId() + " -> " + edge.getEnd().getId() + "}";
    }
}
