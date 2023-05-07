package ua.drovolskyi.cg.lab4;

import java.util.*;

public class ConvexHull {
    private final OneSideConvexHull topConvexHull;
    private final OneSideConvexHull bottomConvexHull;
    private final NavigableSet<Point> points; // all points

    public ConvexHull(){
        topConvexHull = OneSideConvexHull.createTopSideConvexHull();
        bottomConvexHull = OneSideConvexHull.createBottomSideConvexHull();
        points = new TreeSet<>(OneSideConvexHull.getTopPointComparator());
    }

    public void insert(Point p){
        topConvexHull.insert(p);
        bottomConvexHull.insert(p);
        points.add(p);
    }

    public void remove(Point p){
        topConvexHull.remove(p);
        bottomConvexHull.remove(p);
        points.remove(p);
    }

    // check if p belongs to convex hull
    public Boolean contains(Point p){
        return topConvexHull.contains(p) || bottomConvexHull.contains(p);
    }

    // returns points of convex hull
    public List<Point> getPoints(){
        CoupledQueue uTop = topConvexHull.getPoints();
        CoupledQueue uBottom = bottomConvexHull.getPoints();

        if(uTop == null || uBottom == null){
            return new ArrayList<>();
        }
        return mergeTopBottomConvexHulls(uTop, uBottom);
    }

    // top and bottom are not null and not empty
    private List<Point> mergeTopBottomConvexHulls(CoupledQueue top, CoupledQueue bottom){
        List<Point> topPoints = top.getPoints();
        List<Point> bottomPoints = bottom.getPoints();

        List<Point> result = new ArrayList<>();
        result.addAll(topPoints);

        // if topPoints.last != bottomPoints.first
        if(!bottomPoints.get(0).equals(topPoints.get(topPoints.size() - 1))){
            result.add(bottomPoints.get(0));
        }

        // add to result bottom points from second to pre-last
        result.addAll(bottomPoints.subList(1, bottomPoints.size() - 1));

        // if topPoints.first != bottomPoints.first
        if(!bottomPoints.get(bottomPoints.size() - 1).equals(topPoints.get(0))){
            result.add(bottomPoints.get(bottomPoints.size() - 1));
        }

        return result;
    }

    public NavigableSet<Point> getAllPoints(){
        return points;
    }

    public OneSideConvexHull getTopConvexHull() {
        return topConvexHull;
    }

    public OneSideConvexHull getBottomConvexHull() {
        return bottomConvexHull;
    }
}
