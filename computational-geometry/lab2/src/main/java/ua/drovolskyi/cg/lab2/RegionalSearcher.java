package ua.drovolskyi.cg.lab2;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RegionalSearcher {
    public static List<Point> search(TwoDTree twoDTree, Rectangle rectangle){
        System.out.println("\n\n\n =========================== REGIONAL SEARCHING =========================");
        System.out.println("Rectangle: " + rectangle + "\n");

        List<Point> result = new LinkedList<>();
        searchImpl(twoDTree.getRoot(), rectangle, result);
        return result;
    }

    private static void searchImpl(TwoDTree.Node node, Rectangle rectangle, List<Point> result){
        if(node == null){
            return;
        }

        Double start;
        Double end;
        Double pCoord = node.getCuttingLine().getCoordinate();
        if(node.getCuttingLine().getType() == CuttingLine.Type.VERTICAL){
            start = rectangle.getStartX();
            end = rectangle.getEndX();
        }
        else{
            start = rectangle.getStartY();
            end = rectangle.getEndY();
        }
        System.out.println("\n" + CuttingLine.Type.toString(node.getCuttingLine().getType()) +
                " line with coord = " + pCoord + ": ");


        // check if points in node are in rectangle. It is the only place where point can be added to result
        if(Double.compare(start, pCoord) <= 0 &&
                Double.compare(pCoord, end) <= 0){ // if pCoord in [start,end]
            for(Point p : node.getPointsOnLine()){
                if(isInRectangle(p, rectangle)){
                    result.add(p);
                    System.out.println("Point " + p + " added to result");
                }
            }
        }

        // only for output
        if(Double.compare(start, pCoord) < 0){
            System.out.println("Need to go to left subtree");
        }
        if(Double.compare(pCoord, end) < 0){
            System.out.println("Need to go to right subtree");
        }

        // decide in what subtree go to search
        if(Double.compare(start, pCoord) < 0){
            searchImpl(node.getLeft(), rectangle, result);
        }
        if(Double.compare(pCoord, end) < 0){
            searchImpl(node.getRight(), rectangle, result);
        }
    }

    private static Boolean isInRectangle(Point p, Rectangle rectangle){
        if(Double.compare(rectangle.getStartX(), p.getX()) <= 0 &&
                Double.compare(p.getX(), rectangle.getEndX()) <= 0 &&
                Double.compare(rectangle.getStartY(), p.getY()) <= 0 &&
                Double.compare(p.getY(), rectangle.getEndY()) <= 0){
            return true;
        }
        return false;
    }
}