package ua.drovolskyi.cg.lab3.intersections_finder.result;

import ua.drovolskyi.cg.lab3.LineSegment;

import java.util.List;

public abstract class Intersection {
    /**
     * Getter for line segments that connected with intersection object
     * (it can be segments that intersect in one point or two overlapping segments)
     *
     */
    public abstract List<LineSegment> getLineSegments();
}
