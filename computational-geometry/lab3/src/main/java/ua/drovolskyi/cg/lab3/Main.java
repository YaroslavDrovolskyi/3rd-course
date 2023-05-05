package ua.drovolskyi.cg.lab3;

import ua.drovolskyi.cg.lab3.intersections_finder.IntersectionsFinder;
import ua.drovolskyi.cg.lab3.intersections_finder.result.IntersectionsSet;
import ua.drovolskyi.cg.lab3.ui.CartesianFrame;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<LineSegment> segments = Arrays.asList(
                new LineSegment(1, new Point(3.0,6.0), new Point(5.5,7.0)),
                new LineSegment(2, new Point(5.0,8.0), new Point(11.0,8.0)),
                new LineSegment(3, new Point(4.0,4.0), new Point(7.0,3.0)),
                new LineSegment(4, new Point(3.0,9.0), new Point(5.0,1.5)),
                new LineSegment(5, new Point(1.0,3.0), new Point(9.0,11.0)),
                new LineSegment(6, new Point(6.0,2.0), new Point(6.0,11.0)),
                new LineSegment(7, new Point(10.0,5.0), new Point(10.0,8.0)),
                new LineSegment(8, new Point(11.0,8.0), new Point(14.0,14.0)),
                new LineSegment(9, new Point(13.0,12.0), new Point(16.0,18.0)),
                new LineSegment(10, new Point(1.0,4.0), new Point(17.0,10.0)),
                new LineSegment(11, new Point(5.0,11.0), new Point(17.0,7.0)),
                new LineSegment(12, new Point(15.0,6.0), new Point(15.0,11.0))
        );

        CartesianFrame.displaySegments(segments, "Segments");

        IntersectionsFinder intersectionsFinder = new IntersectionsFinder();
        IntersectionsSet intersections = intersectionsFinder.findAllIntersections(segments);

        System.out.println("\n\nFound intersections:\n" + intersections);
        CartesianFrame.displaySegmentsAndIntersections(segments, intersections, "Intersections");

    }

}