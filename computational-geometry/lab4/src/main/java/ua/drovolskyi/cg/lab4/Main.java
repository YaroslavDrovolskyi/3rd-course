package ua.drovolskyi.cg.lab4;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Point> points = Arrays.asList(
            new Point(1, 5.0, 5.0),
            new Point(2, 6.0, 5.0),
            new Point(3, 8.0, 5.0),
            new Point(4, 10.0, 5.0),
            new Point(5, 25.0, 5.0),
            new Point(6, 30.0, 5.0),
            new Point(7, 30.0, 10.0)
        );
        List<Integer> insertIndices = Arrays.asList(2, 3, 0, 6, 5, 1, 4);


        OneSideConvexHull tree = OneSideConvexHull.createTopSideConvexHull();

        Integer count = 0;
        for(Integer i : insertIndices){
            System.out.println("i = " + i); //////////////////////////////////
            tree.insert(points.get(i));
            String title = (count+1) + " inserted " + (i+1);
            OneSideConvexHullTreeVisualizer.visualize(tree, title+".png", title);
            System.out.println(title);
            count++;
        }
    }
}