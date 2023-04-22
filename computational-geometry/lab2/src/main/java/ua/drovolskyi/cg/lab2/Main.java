package ua.drovolskyi.cg.lab2;

import ua.drovolskyi.cg.lab2.ui.CartesianFrame;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Set<Point> points = new HashSet<>(Arrays.asList(
 //               new Point(1,3.0, 7.0),
                new Point(2,5.5, 7.0),
                new Point(3,9.0, 3.5),
                new Point(4,3.0, 7.0),
                new Point(5,9.5, 8.0),
                new Point(6,6.5, 5.0),
                new Point(7,5.0, 1.5),
                new Point(8,6.0, 7.5),
                new Point(9,10.5, 5.5),
                new Point(10,2.5,1.5),
                new Point(11,11.5, 1.5),
                new Point(12,10.5, 4.0),
                new Point(13,6.0, 10.0),
                new Point(14,5.5, 6.0),
                new Point(15,8.0, 8.0),
                new Point(16,4.5,0.5),
                new Point(17,10.0, 15.0),
                new Point(19,5.0, 10.0),
                new Point(20,7.2, 7.0),
                new Point(21,7.5, 4.0),
                new Point(22, 3.0, 5.0), // vertex of rectangle
                new Point(23, 3.0, 7.5), // vertex of rectangle
                new Point(24, 10.0, 7.5), // vertex of rectangle
                new Point(25, 10.0, 5.0), // vertex of rectangle
                new Point (26, 13.0, 12.0)
        ));


/*        Set<Point> points = new HashSet<>(Arrays.asList(
                new Point(1,0.5, 2.0),
                new Point(2,1.5, 1.0),
                new Point(3,2.5, 4.0),
                new Point(4,3.5, 1.5),
                new Point(5,4.5, 2.5),
                new Point(6,6.0, 4.5),
                new Point(7,6.5, 1.0),
                new Point(8,7.5, 3.5),
                new Point(9,8.5, 0.5)
        )); */

        // input data
        System.out.println("Input points: ");
        printListOfPoints(points.stream().toList());
        CartesianFrame.displayPoints(points.stream().toList(), "Input points");

        Rectangle rect = new Rectangle(3.0, 10.0, 5.0, 10.0);
        System.out.println("Input rectangle: " + rect + "\n");

        // preliminary processing
        TwoDTree tree = TwoDTreeBuilder.build(points);
        System.out.println("\n\n" + tree + "\n");

        String filename = "2d-tree.png";
        TwoDTreeVisualizer.visualize(tree, filename);
        CartesianFrame.displayTwoDTreeFromImage(filename, "2d tree");

        // regional searching
        List<Point> result = RegionalSearcher.search(tree, rect);


        System.out.println("\n\nResult points: ");
        printListOfPoints(result.stream().toList());
        CartesianFrame.displayResultOfRegionalSearch(points.stream().toList(), rect, result, "Result of regional search");
    }


    public static void printListOfPoints(List<Point> points){
        if(points.isEmpty()){
            System.out.println("[List is EMPTY]");
            return;
        }

        for(Point p : points){
            System.out.println(p);
        }
    }
}
