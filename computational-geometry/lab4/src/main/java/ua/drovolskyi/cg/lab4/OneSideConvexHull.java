package ua.drovolskyi.cg.lab4;

import java.util.*;

public class OneSideConvexHull {
    private static final Comparator<Point> topPointComparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(MathUtils.areEqual(p1.getX(), p2.getX())){
                if(MathUtils.areEqual(p1.getY(), p2.getY())){
                    return 0;
                }
                else if(p1.getY() < p2.getY()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else if(p1.getX() < p2.getX()){
                return -1;
            }
            else{
                return 1;
            }
        }
    };

    private static final Comparator<Point> bottomPointComparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(MathUtils.areEqual(p1.getX(), p2.getX())){
                if(MathUtils.areEqual(p1.getY(), p2.getY())){
                    return 0;
                }
                else if(p1.getY() < p2.getY()){
                    return 1;
                }
                else{
                    return -1;
                }
            }
            else if(p1.getX() < p2.getX()){
                return 1;
            }
            else{
                return -1;
            }
        }
    };

    private final Comparator<Point> pointComparator;

    public OneSideConvexHull(Comparator<Point> pointComparator){
        this.pointComparator = pointComparator;
    }

    private Node root = null;

    public void insert(Point p){
        if(root == null){
            Node node = new Node(null, null, null, p, pointComparator);
            node.addPointIntoSubConvexHall(p);
            node.pivotPoint = null;
            this.root = node;
        }
        else{
            // root.convexHull = root.partOfConvexHall
            // insertIntoSubtree(root)
        }
    }
    //////////////////// pivotPoint, maxPointOfLeftSubtree?????
    // maxPointInLeftSubtree := point

    // convexHull is convex hull for node
    private void insertIntoSubtree(Node node, Point p){
        if(!node.isLeaf()){
            // here node.convexHull is filled
            List<SortedSet<Point>> untie = untieConvexHull(node);

            SortedSet<Point> leftSubConvexHull = untie.get(0);
            leftSubConvexHull.addAll(node.left.getSubConvexHull());
            node.left.convexHull = leftSubConvexHull;

            SortedSet<Point> rightSubConvexHull = untie.get(1);
            rightSubConvexHull.addAll(node.right.getSubConvexHull());
            node.right.convexHull = rightSubConvexHull;

            if(pointComparator.compare(p, node.point) <= 0){
                insertIntoSubtree(node.left, p);
            }
            else{
                insertIntoSubtree(node.right, p);
            }
        }
        else{
            // if this point is already in tree
            if(pointComparator.compare(p, node.getPoint()) == 0){
                // need to go up to the root
            }
            else{
                Node grandparent = node.parent.parent;
                if(grandparent.left == node.parent){
                    // insert newNode with
                    // newNode.left = node.parent
                    // newNode.right = new Node(p)
                    // newNode.parent = grandparent
                    // grandparent.left = node.parent
                }
                else{

                }
            }
        }


    }


    private List<SortedSet<Point>> untieConvexHull(Node node){
        SortedSet<Point> convexHull = node.convexHull;
        SortedSet<Point> partOfLeftConvexHull = convexHull.subSet(convexHull.first(), node.getPivotPoint());
        SortedSet<Point> partOfRightConvexHull = convexHull.subSet(node.getPivotPoint(), convexHull.last());
        partOfRightConvexHull.add(convexHull.last()); // because .subset() is end-exclusive operation

        return Arrays.asList(partOfLeftConvexHull, partOfRightConvexHull);
    }




    private static class Node{
        private Point point; // point (if leaf node) or maximum in left subtree (otherwise)
        private TreeSet<Point> subConvexHull;
        private SortedSet<Point> convexHull;
        private Point pivotPoint; // pivot point of convex hall of subtree of this node
        // pivot point is null when node is leaf
        // is right end of bridge line segment
//        private Point maxPointInLeftSubtree = null;
        private Node parent;
        private Node left;
        private Node right;

        public Node(Node parent, Node left, Node right, Point point,
                    Comparator<Point> pointComparator){
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.point = point;
            this.subConvexHull = new TreeSet<>(pointComparator);
            this.convexHull = new TreeSet<>(pointComparator);
        }
        public Boolean isLeaf(){
            return left == null || right == null;
        }

        public Point getPoint() {
            return point;
        }

        public TreeSet<Point> getSubConvexHull() {
            return subConvexHull;
        }

        public void addPointIntoSubConvexHall(Point p){
            subConvexHull.add(p);
        }

        public Point getPivotPoint() {
            return pivotPoint;
        }

        public Node getParent() {
            return parent;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }
}
