package ua.drovolskyi.cg.lab3;

import java.util.Comparator;

// start is left point, end is right point
// in other words, start < end according to GeometricUtils.getPointComparator()
public class LineSegment {
    private final Point start;
    private final Point end;

    public LineSegment(Point p1, Point p2){
        Comparator<Point> comp = GeometricUtils.getPointComparator();
        if(comp.compare(p1, p2) < 0){
            this.start = p1;
            this.end = p2;
        }
        else if (comp.compare(p1, p2) > 0){
            this.start = p2;
            this.end = p1;
        }
        else{
            throw new IllegalArgumentException("Start and end points of line segment must not be equal");
        }
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof LineSegment)){
            return false;
        }
        LineSegment lineSegment = (LineSegment)obj;
        return this.getStart().equals(lineSegment.getStart()) &&
                this.getEnd().equals(lineSegment.getEnd());
    }
}
