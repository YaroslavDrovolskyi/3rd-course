package ua.drovolskyi.cg.lab1.result;

import ua.drovolskyi.cg.lab1.Point;

public class PointOutsideChainsVertically extends PointLocalizationResult {
    private final Point point;
    private final int side;

    public PointOutsideChainsVertically(Point point, int side){
        this.point = point;
        this.side = side;
    }

    public Point getPoint() {
        return point;
    }

    // returns 1 if point is above chains, and -1 if point is under the chains
    public int getSide() {
        return side;
    }
}
