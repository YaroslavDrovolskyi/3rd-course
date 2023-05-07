package ua.drovolskyi.cg.lab4;

import java.util.*;

public class CoupledQueue {
    private NavigableSet<Point> points;

    public CoupledQueue(Comparator<? super Point> pointComparator){
        points = new TreeSet<>(pointComparator);
    }

    // split queue and put pivotPoint into first CoupledQueue
    public static List<CoupledQueue> splitLeftmost(CoupledQueue queue, Point pivotPoint){
        Point[] array = queue.points.toArray(new Point[0]);
        Integer pivotIndex = Arrays.binarySearch(array, 0, array.length, pivotPoint, queue.points.comparator());

        CoupledQueue leftQueue = new CoupledQueue(queue.points.comparator());
        leftQueue.points.addAll(Arrays.asList(Arrays.copyOfRange(array, 0, pivotIndex + 1)));

        CoupledQueue rightQueue = new CoupledQueue(queue.points.comparator());
        rightQueue.points.addAll(Arrays.asList(Arrays.copyOfRange(array, pivotIndex + 1, array.length)));

        return Arrays.asList(leftQueue, rightQueue);
    }

    // split queue and put pivotPoint into second CoupledQueue
    public static List<CoupledQueue> splitRightmost(CoupledQueue queue, Point pivotPoint){
        Point[] array = queue.points.toArray(new Point[0]);
        Integer pivotIndex = Arrays.binarySearch(array, 0, array.length, pivotPoint, queue.points.comparator());

        CoupledQueue leftQueue = new CoupledQueue(queue.points.comparator());
        leftQueue.points.addAll(Arrays.asList(Arrays.copyOfRange(array, 0, pivotIndex)));

        CoupledQueue rightQueue = new CoupledQueue(queue.points.comparator());
        rightQueue.points.addAll(Arrays.asList(Arrays.copyOfRange(array, pivotIndex, array.length)));

        return Arrays.asList(leftQueue, rightQueue);
    }

    public static CoupledQueue couple(CoupledQueue leftQueue, CoupledQueue rightQueue){
        CoupledQueue result = new CoupledQueue(leftQueue.points.comparator());

        result.points.addAll(leftQueue.points);
        result.points.addAll(rightQueue.points);

        return result;
    }

    public void add(Point p){
        points.add(p);
    }

    public Point getMiddlePoint(){
        Point[] array = points.toArray(new Point[0]);
        return array[array.length / 2];
    }

    public Point getNextPoint(Point p){
        return points.higher(p);
    }

    public Point getPrevPoint(Point p){
        return points.lower(p);
    }

    public Point getNextPointBounded(Point p){
        Point result = points.higher(p);
        if(result == null){
            return p;
        }
        return result;
    }

    public Point getPrevPointBounded(Point p){
        Point result = points.lower(p);
        if(result == null){
            return p;
        }
        return result;
    }

    public Boolean isEmpty(){
        return points.isEmpty();
    }

    public Boolean contains(Point p){
        return points.contains(p);
    }

    @Override
    public String toString(){
        if(points.isEmpty()){
            return "[EMPTY]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Point p : points){
            sb.append(p.getId() + ", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();
    }
}
