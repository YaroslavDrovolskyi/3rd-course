package ua.drovolskyi.cg.lab4;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        testVerticalPoints();





    }

    //////////////////////////////////////////////////////////////////////////////////
    // + if point 2.2 then go to nearest left-parent and fix field 'point'
    // + fix split CoupledQueue to get possibility to get empty set (array of size 0 is allowed)
    // + make table of complexities (ideal and of my implementation)
    // + test a-b, c-d when a-b is lower than c-d
    // + make goFixUp() clear CH in root
    // test vertical



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