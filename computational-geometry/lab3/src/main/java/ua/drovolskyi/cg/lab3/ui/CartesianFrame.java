package ua.drovolskyi.cg.lab3.ui;

import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.Point;
import ua.drovolskyi.cg.lab3.intersections_finder.result.InPointIntersection;
import ua.drovolskyi.cg.lab3.intersections_finder.result.IntersectionsSet;
import ua.drovolskyi.cg.lab3.intersections_finder.result.OverlappingIntersection;

import javax.swing.*;
import java.util.List;

public class CartesianFrame extends JFrame {
    private final CartesianPanel panel;

    public CartesianFrame() {
        panel = new CartesianPanel();
        add(panel);
    }

    public void showUI(String title) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        setSize(700, 700);
        setVisible(true);
    }

    public CartesianPanel getPanel(){
        return panel;
    }

    public static void displaySegments(List<LineSegment> segments, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawSegments(segments);
        frame.showUI(title);
    }

    public static void displaySegmentsAndIntersections(List<LineSegment> segments,
                                                       IntersectionsSet intersections, String title){
        CartesianFrame frame = new CartesianFrame();

        // draw overlapping intersections
        List<OverlappingIntersection> oi = intersections.getOverlappingIntersections().stream().toList();
        for(OverlappingIntersection i : oi){
            frame.getPanel().drawSpecialSegments(i.getLineSegments());
        }

        // draw in-point intersections
        List<Point> ip = intersections.getInPointIntersections().keySet().stream().toList();
        frame.getPanel().drawSpecialPoints(ip);

        frame.showUI(title);
    }
}

/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java
    How to display image from file in JFrame: https://stackoverflow.com/questions/20098124/displaying-an-image-in-a-jframe
 */

