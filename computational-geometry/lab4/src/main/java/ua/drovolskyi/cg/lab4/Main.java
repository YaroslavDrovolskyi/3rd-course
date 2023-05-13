package ua.drovolskyi.cg.lab4;

import ua.drovolskyi.cg.lab4.ui.CartesianFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Point> points = Arrays.asList(
                new Point(1, 8.0, 2.5),
                new Point(2, 5.0, 5.0),
                new Point(3, 9.5, 9.0),
                new Point(4, 7.0, 8.0),
                new Point(5, 11.0, 7.5),
                new Point(6, 9.0, 6.0),
                new Point(7, 15.5, 13.0),
                new Point(8, 13.5, 10.0),
                new Point(9, 8.0, 14.0),
                new Point(10, 2.0, 6.0),
                new Point(11, 16.0, 18.5),
                new Point(12, 13.0, 4.0),
                new Point(13, 14.0, 8.0),
                new Point(14, 19.0, 10.0),
                new Point(15, 5.0, 18.0),
                new Point(16, 18.0, 5.0),
                new Point(17, 16.0, 2.0),
                new Point(18, 3.0, 9.0),
                new Point(19, 17.0, 2.0),
                new Point(20, 1.0, 1.0),
                new Point(21, 10.0, 10.0)
        );
//        List<Integer> removeIndices = Arrays.asList(5, 1, 4, 3, 6, 0, 2);
        List<Integer> removeIndices = Arrays.asList(14); // , 17, 1, 0
        // 17, 1, 0

        ConvexHull u = new ConvexHull();

        // insert
        for(Point p : points){
            u.insert(p);
            System.out.println("inserted " + p);
        }

        CartesianFrame.displayConvexHull(u, "Convex hull before removing");
        System.out.print("\n\n\nConvex hull:");
        printPoints(u.getPoints());


        // remove
        for(Integer i : removeIndices){
            u.remove(points.get(i));
            System.out.println("removed " + points.get(i));
        }

        CartesianFrame.displayConvexHull(u, "Convex hull after removing");
        System.out.print("\n\n\nConvex hull:");
        printPoints(u.getPoints());

    }

    //////////////////////////////////////////////////////////////////////////////////
    // + if point 2.2 then go to nearest left-parent and fix field 'point'
    // + fix split CoupledQueue to get possibility to get empty set (array of size 0 is allowed)
    // + make table of complexities (ideal and of my implementation)
    // + test a-b, c-d when a-b is lower than c-d (all is ok, but more same-y-coordinate points wil give incorrect result)
    // + make goFixUp() clear CH in root
    // + test vertical

    public static void printPoints(List<Point> points){
        String result;

        if(points.isEmpty()){
            result = "[EMPTY]";
        }
        else{
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for(Point p : points){
                sb.append(p.getId() + ", ");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");

            result = sb.toString();
        }
        System.out.println(result);
    }

    // template function for testing OneSideConvexHull
    public static void testInsertRemove(List<Point> points, List<Integer>insertIndices,
                                        List<Integer> removeIndices) throws IOException {
        OneSideConvexHull tree = OneSideConvexHull.createTopSideConvexHull();
        Integer count = 0;

        // inserting
        for(Integer i : insertIndices){
            tree.insert(points.get(i));
            String title = (count+1) + ") inserted " + (i+1);
            OneSideConvexHullTreeVisualizer.visualize(tree, title+".png", title);
            System.out.println(title);
            count++;
        }

        // removing
        for(Integer i : removeIndices){
            tree.remove(points.get(i));
            String title = (count+1) + ") removed " + (i+1);
            OneSideConvexHullTreeVisualizer.visualize(tree, title+".png", title);
            System.out.println(title);
            count++;
        }
    }


    public static void testHorizontalPoints() throws IOException {
        List<Point> points = Arrays.asList(
                new Point(1, 5.0, 5.0),
                new Point(2, 6.0, 5.0),
                new Point(3, 8.0, 5.0),
                new Point(4, 10.0, 5.0),
                new Point(5, 25.0, 5.0),
                new Point(6, 30.0, 5.0),
                new Point(7, 35.0, 5.0)
        );
        List<Integer> insertIndices = Arrays.asList(2, 3, 0, 6, 5, 1, 4);
        List<Integer> removeIndices = Arrays.asList(5,1,4,3,6,0,2);

        testInsertRemove(points, insertIndices, removeIndices);
    }

    public static void testVerticalPoints() throws IOException {
        List<Point> points = Arrays.asList(
                new Point(1, 5.0, 5.0),
                new Point(2, 5.0, 6.0),
                new Point(3, 5.0, 8.0),
                new Point(4, 5.0, 10.0),
                new Point(5, 5.0, 25.0),
                new Point(6, 5.0, 30.0),
                new Point(7, 5.0, 35.0)
        );
        List<Integer> insertIndices = Arrays.asList(2, 3, 0, 6, 5, 1, 4);
        List<Integer> removeIndices = Arrays.asList(5,1,4,3,6,0,2);

        testInsertRemove(points, insertIndices, removeIndices);
    }

    public static void testTwoHorizontalLines() throws IOException {
        List<Point> points = Arrays.asList(
                new Point(1, 5.0, 5.0),
                new Point(2, 6.0, 5.0),
                new Point(3, 8.0, 5.0),
                new Point(4, 10.0, 5.0),
                new Point(5, 25.0, 10.0),
                new Point(6, 30.0, 10.0),
                new Point(7, 35.0, 10.0)
        );
        List<Integer> insertIndices = Arrays.asList(2, 3, 0, 6, 5, 1, 4);
        List<Integer> removeIndices = Arrays.asList(5,1,4,3,6,0,2);

        testInsertRemove(points, insertIndices, removeIndices);
    }
}