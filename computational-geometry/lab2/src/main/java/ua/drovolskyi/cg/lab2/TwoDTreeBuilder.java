package ua.drovolskyi.cg.lab2;

import java.util.*;

public class TwoDTreeBuilder {
    /**
     * Builds 2d tree for given set of points
     * @param setOfPoints is set of points
     * @return 2d tree for given set of points
     */
    public static TwoDTree build(Set<Point> setOfPoints){
        System.out.println("\n======================== BUILD 2D-TREE ========================================");
        List<Point> pointsSortedByX = Arrays.asList(setOfPoints.toArray(new Point[0]));
        pointsSortedByX.sort(pointCompByX);

        List<Point> pointsSortedByY = Arrays.asList(setOfPoints.toArray(new Point[0]));
        pointsSortedByY.sort(pointCompByY);

        TwoDTree.Node root = buildImpl(pointsSortedByX, pointsSortedByY, CuttingLine.Type.VERTICAL);
        return new TwoDTree(root);
    }

    /**
     * Recursively Build 2d tree on given set of points.
     * Lists pointsSortedByX and pointsSortedByY contains the SAME points,
     * but sorted by x and by y -coordinates respectively.
     * @param pointsSortedByX given points sorted by x-coordinate
     * @param pointsSortedByY given points sorted by y-coordinate
     * @param type is type of first cutting line (horizontal or vertical),
     *             then cutting lines will alternate through the levels of the tree
     * @return 2d tree for given set of points
     */
    private static TwoDTree.Node buildImpl(List<Point> pointsSortedByX,
                                           List<Point> pointsSortedByY,
                                           CuttingLine.Type type){
        if(pointsSortedByX.isEmpty()){
            return null;
        }

        if(type == CuttingLine.Type.VERTICAL){
            Integer medianIndex = pointsSortedByX.size() / 2;
            Double medianX = pointsSortedByX.get(medianIndex).getX();
            List<Point> leftPointsSortedByX = new LinkedList<>();
            List<Point> leftPointsSortedByY = new LinkedList<>();
            List<Point> rightPointsSortedByX = new LinkedList<>();
            List<Point> rightPointsSortedByY = new LinkedList<>();
            List<Point> pointsOnLine = new LinkedList<>();

            for(Point p : pointsSortedByX){
                if(p.getX() < medianX){
                    leftPointsSortedByX.add(p);
                }
                else if (p.getX() > medianX){
                    rightPointsSortedByX.add(p);
                }
            }
            for(Point p : pointsSortedByY){
                if(p.getX() < medianX){
                    leftPointsSortedByY.add(p);
                }
                else if (p.getX() > medianX){
                    rightPointsSortedByY.add(p);
                }
                else{
                    pointsOnLine.add(p);
                }
            }

            System.out.println("Created VERTICAL cutting line through points " + pointsOnLine);
            TwoDTree.Node leftNode = buildImpl(leftPointsSortedByX, leftPointsSortedByY, CuttingLine.Type.HORIZONTAL);
            TwoDTree.Node rightNode = buildImpl(rightPointsSortedByX, rightPointsSortedByY, CuttingLine.Type.HORIZONTAL);
            return new TwoDTree.Node(pointsOnLine,
                    new CuttingLine(medianX, CuttingLine.Type.VERTICAL),
                    leftNode, rightNode);
        }
        else{ // need build horizontal cutting line
            Integer medianIndex = pointsSortedByY.size() / 2;
            Double medianY = pointsSortedByY.get(medianIndex).getY();
            List<Point> lowerPointsSortedByX = new LinkedList<>();
            List<Point> lowerPointsSortedByY = new LinkedList<>();
            List<Point> higherPointsSortedByX = new LinkedList<>();
            List<Point> higherPointsSortedByY = new LinkedList<>();
            List<Point> pointsOnLine = new LinkedList<>();

            for(Point p : pointsSortedByX){
                if(p.getY() < medianY){
                    lowerPointsSortedByX.add(p);
                }
                else if (p.getY() > medianY){
                    higherPointsSortedByX.add(p);
                }
                else{
                    pointsOnLine.add(p);
                }
            }
            for(Point p : pointsSortedByY){
                if(p.getY() < medianY){
                    lowerPointsSortedByY.add(p);
                }
                else if (p.getY() > medianY){
                    higherPointsSortedByY.add(p);
                }
            }

            System.out.println("Created HORIZONTAL cutting line through points " + pointsOnLine);
            TwoDTree.Node leftNode = buildImpl(lowerPointsSortedByX, lowerPointsSortedByY, CuttingLine.Type.VERTICAL);
            TwoDTree.Node rightNode = buildImpl(higherPointsSortedByX, higherPointsSortedByY, CuttingLine.Type.VERTICAL);
            return new TwoDTree.Node(pointsOnLine,
                    new CuttingLine(medianY, CuttingLine.Type.HORIZONTAL),
                    leftNode, rightNode);
        }
    }



    private static final Comparator<Point> pointCompByX = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(p1.getX() < p2.getX()){
                return -1;
            }
            else if(MathUtils.areEqual(p1.getX(), p2.getX())){
                return 0;
            }
            else{
                return 1;
            }
        }
    };

    private static final Comparator<Point> pointCompByY = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(p1.getY() < p2.getY()){
                return -1;
            }
            else if(MathUtils.areEqual(p1.getY(), p2.getY())){
                return 0;
            }
            else{
                return 1;
            }
        }
    };
}
