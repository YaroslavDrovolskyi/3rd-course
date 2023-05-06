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

    public static OneSideConvexHull createTopSideConvexHull(){
        return new OneSideConvexHull(topPointComparator);
    }

    public static OneSideConvexHull createBottomSideConvexHull(){
        return new OneSideConvexHull(bottomPointComparator);
    }

    private Node root = null;
    private final Comparator<Point> pointComparator;

    private OneSideConvexHull(Comparator<Point> pointComparator){
        this.pointComparator = pointComparator;
    }

    public Node getRoot(){
        return this.root;
    }

    public void insert(Point p){
        if(root == null){
            this.root = Node.createLeafNode(null, p, pointComparator);
            this.root.subConvexHull = this.root.convexHull;
        }
        else{
            root.convexHull = root.subConvexHull;
            insertIntoSubtreeGoDown(root, p);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////
    // + if point 2.2 then go to nearest left-parent and fix field 'point'
    // + fix split CoupledQueue to get possibility to get empty set (array of size 0 is allowed)

    // convexHull is convex hull for node
    private void insertIntoSubtreeGoDown(Node node, Point p){
        if(!node.isLeaf()){
            // here node.convexHull is filled

            List<CoupledQueue> splitting = CoupledQueue.splitLeftmost(node.convexHull, node.pivotPoint);

            node.left.convexHull = CoupledQueue.couple(splitting.get(0), node.left.subConvexHull);
            node.right.convexHull = CoupledQueue.couple(node.right.subConvexHull, splitting.get(1));

            if(pointComparator.compare(p, node.point) <= 0){
                insertIntoSubtreeGoDown(node.left, p);
            }
            else{
                insertIntoSubtreeGoDown(node.right, p);
            }
        }
        else{
            if(pointComparator.compare(p, node.getPoint()) != 0){ // if tree does not already contain p
                if(node.parent != null){
                    if(node == node.parent.left){
                        // here p < node.point

                        Node newParent = Node.createInnerNode(node.parent, null, node, p);
                        Node newLeaf = Node.createLeafNode(newParent, p, pointComparator);
                        newParent.left = newLeaf;
                        node.parent.left = newParent;
                        node.parent = newParent;
                    }
                    else if(node == node.parent.right){
                        if(pointComparator.compare(p, node.point) < 0){ // newNode is left to node
                            Node newParent = Node.createInnerNode(node.parent, null, node, p);
                            Node newLeaf = Node.createLeafNode(newParent, p, pointComparator);
                            newParent.left = newLeaf;
                            node.parent.right = newParent;
                            node.parent = newParent;
                        }
                        else{ // newNode is right to node
                            Node newParent = Node.createInnerNode(node.parent, node, null, node.point);
                            Node newLeaf = Node.createLeafNode(newParent, p, pointComparator);
                            newParent.right = newLeaf;
                            node.parent.right = newParent;
                            node.parent = newParent;

                            // go to the first node n whose left subtree contains newNode
                            // in order to fix point of n (max point in left subtree)
                            Node parent = newParent.parent;
                            while(parent.parent != null && parent != parent.parent.left){
                                parent = parent.parent;
                            }
                            if(parent.parent != null){
                                parent.parent.point = p;
                            }
                        }
                    }
                }
                else{ // if node is root
                    Node leftNode = null;
                    Node rightNode = null;

                    if(pointComparator.compare(p, node.getPoint()) < 0){
                        leftNode = Node.createLeafNode(null, p, pointComparator);
                        rightNode = node;
                    }
                    else{
                        leftNode = node;
                        rightNode = Node.createLeafNode(null, p, pointComparator);
                    }

                    Node parent = Node.createInnerNode(null, leftNode, rightNode, leftNode.point);
                    leftNode.parent = parent;
                    rightNode.parent = parent;
                    this.root = parent;
                }

            }
            insertIntoSubtreeGoUp(node);
        }
    }

    private void insertIntoSubtreeGoUp(Node node){
        if(this.root == node) {
            node.subConvexHull = node.convexHull;
        }
        else{
            Node parent = node.parent;
            Node sibling = null;
            ConnectingConvexHallsResult connectionResult = null;
            if(node == parent.left){ // node is left child
                sibling = parent.right;
                connectionResult = connectConvexHulls(node.convexHull, sibling.convexHull, parent.point.getX());
            }
            else{ // node is right child
                sibling = parent.left;
                connectionResult = connectConvexHulls(sibling.convexHull, node.convexHull, parent.point.getX());
            }
            parent.left.subConvexHull = connectionResult.u12;
            parent.right.subConvexHull = connectionResult.u21;
            parent.convexHull = connectionResult.u;
            parent.pivotPoint = connectionResult.pivotPoint;

            insertIntoSubtreeGoUp(parent);
        }
//        node.convexHull = null; // comment it if you don;t want to store convex hull in each node
    }

    // x is x-coordinate of the biggest point in left tree we are processing now
    // according to slide 10
    // u1 is left CH, and u2 is right CH
    private ConnectingConvexHallsResult connectConvexHulls(CoupledQueue u1, CoupledQueue u2, Double x){
        List<Point> bridgeLineSegment = findSupportingPoints(u1, u2, x);
        Point p1 = bridgeLineSegment.get(0);
        Point p2 = bridgeLineSegment.get(1);

        List<CoupledQueue> splitU1 = CoupledQueue.splitLeftmost(u1, p1);
        List<CoupledQueue> splitU2 = CoupledQueue.splitRightmost(u2, p2);

        CoupledQueue u = CoupledQueue.couple(splitU1.get(0), splitU2.get(1)); // result convex hull

        return new ConnectingConvexHallsResult(splitU1.get(0), splitU1.get(1),
                splitU2.get(0), splitU2.get(1), u, p1);
    }

    private static class ConnectingConvexHallsResult{
        private CoupledQueue u11;
        private CoupledQueue u12;
        private CoupledQueue u21;
        private CoupledQueue u22;
        private CoupledQueue u; // coupled u11 and u22
        private Point pivotPoint;

        public ConnectingConvexHallsResult(CoupledQueue u11, CoupledQueue u12,
                                           CoupledQueue u21, CoupledQueue u22,
                                           CoupledQueue u, Point pivotPoint) {
            this.u11 = u11;
            this.u12 = u12;
            this.u21 = u21;
            this.u22 = u22;
            this.u = u;
            this.pivotPoint = pivotPoint;
        }
    }


    // p1 is point from left convex hull p2 is- from right one
    // x is x-coordinate of the biggest point in left tree we now processing
    private List<Point> findSupportingPoints(CoupledQueue leftConvexHull, CoupledQueue rightConvexHull, Double x){
        Point p1 = leftConvexHull.getMiddlePoint();
        Point p2 = rightConvexHull.getMiddlePoint();

        // if during searching some of iterating points become null, throw exception, because it isn't normal

        while(true){
            List<PointType> classification = classifyPoints(
                    leftConvexHull.getPrevPoint(p1), p1, leftConvexHull.getNextPoint(p1),
                    rightConvexHull.getPrevPoint(p2), p2, rightConvexHull.getNextPoint(p2)
            );

            PointType typeP1 = classification.get(0);
            PointType typeP2 = classification.get(1);

            if(typeP1 == PointType.CONCAVE){
                if(typeP2 == PointType.CONCAVE){
                    Point intersection = GeometricUtils.findIntersectionOfLines(
                            p1, leftConvexHull.getNextPoint(p1),
                            rightConvexHull.getPrevPoint(p2), p2
                    );

                    // let comparing vertical line l be x coordinate of the biggest point in left subtree of parent
                    if(intersection == null ||
                        MathUtils.areEqual(intersection.getX(), x) ||
                            intersection.getX() < x){ // p is left to line l
                        p1 = leftConvexHull.getNextPoint(p1);
                        // p2 does not change
                    }
                    else{ // p is right to line l
                        // p1 does not change
                        p2 = rightConvexHull.getPrevPoint(p2);
                    }
                }
                else if(typeP2 == PointType.SUPPORTING){
                    p1 = leftConvexHull.getNextPoint(p1);
                    p2 = rightConvexHull.getNextPoint(p2);
                }
                else if(typeP2 == PointType.CONVEX){
                    p2 = rightConvexHull.getNextPoint(p2);
                }
            }
            else if(typeP1 == PointType.SUPPORTING){
                if(typeP2 == PointType.CONCAVE){
                    p1 = leftConvexHull.getPrevPoint(p1);
                    p2 = rightConvexHull.getPrevPoint(p2);
                }
                else if(typeP2 == PointType.SUPPORTING){
                    return Arrays.asList(p1, p2);
                }
                else if(typeP2 == PointType.CONVEX){
                    p1 = leftConvexHull.getPrevPoint(p1);
                    p2 = rightConvexHull.getNextPoint(p2);
                }
            }
            else if(typeP1 == PointType.CONVEX){
                if(typeP2 == PointType.CONCAVE){
                    p1 = leftConvexHull.getPrevPoint(p1);
                }
                else if(typeP2 == PointType.SUPPORTING){
                    p1 = leftConvexHull.getPrevPoint(p1);
                    p2 = rightConvexHull.getNextPoint(p2);
                }
                else if(typeP2 == PointType.CONVEX){
                    p1 = leftConvexHull.getPrevPoint(p1);
                    p2 = rightConvexHull.getNextPoint(p2);
                }
            }
        }
        // following is under question ???????????????????
        // when SUPPORTING = SUPPORTING is found:
        // while p1 and nextP1 have the same y-coordinate, shift to right
        // while prevP2 and p2 have the same y-coordinate, shift to left
    }

    private List<PointType> classifyPoints(
            Point prevP1, Point p1, Point nextP1,
            Point prevP2, Point p2, Point nextP2
    ){
        if(relativePosition(p1, p2, prevP1) == 1 &&
                relativePosition(p1, p2, nextP1) == -1){ // p1 is CONCAVE
            if(relativePosition(p1, p2, prevP2) == -1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.CONCAVE, PointType.CONCAVE);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.CONCAVE, PointType.SUPPORTING);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == -1){
                return Arrays.asList(PointType.CONCAVE, PointType.CONVEX);
            }
        }
        else if(relativePosition(p1, p2, prevP1) == 1 &&
                relativePosition(p1, p2, nextP1) == 1){ // p1 is SUPPORTING
            if(relativePosition(p1, p2, prevP2) == -1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.SUPPORTING, PointType.CONCAVE);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.SUPPORTING, PointType.SUPPORTING);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == -1){
                return Arrays.asList(PointType.SUPPORTING, PointType.CONVEX);
            }
        }
        else if(relativePosition(p1, p2, prevP1) == -1 &&
                relativePosition(p1, p2, nextP1) == 1){ // p1 is CONVEX
            if(relativePosition(p1, p2, prevP2) == -1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.CONVEX, PointType.CONCAVE);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == 1){
                return Arrays.asList(PointType.CONVEX, PointType.SUPPORTING);
            }
            else if(relativePosition(p1, p2, prevP2) == 1 &&
                    relativePosition(p1, p2, nextP2) == -1){
                return Arrays.asList(PointType.CONVEX, PointType.CONVEX);
            }
        }
        throw new RuntimeException("Unknown classification of points");
    }

    /*
    private List<PointType> classifyPoint(
            Point prevP, Point p, Point nextP,
            Point endOfVector
    ){

    }

     */

    // if point is on the line, then it is UNDER the line, and method returns 1
    private int relativePosition(Point start, Point end, Point p){
        if(p == null){
            return 1;
        }
        else{
            Integer result = GeometricUtils.relativePosition(start, end, p);
            if (result == 0) {
                return 1;
            }
            else{
                return result;
            }
        }
    }


    private static enum PointType{
        CONCAVE,
        SUPPORTING,
        CONVEX
    }




    public static class Node{
        private Node parent;
        private Node left;
        private Node right;
        private Point point; // point (if leaf node) or maximum in left subtree (otherwise)
        private Point pivotPoint = null; // it is left end of bridge between convex hulls
        private CoupledQueue subConvexHull = null;
        private CoupledQueue convexHull = null;


        public static Node createLeafNode(Node parent, Point point, Comparator<Point> pointComparator){
            Node node = new Node(parent, null, null, point);
            node.pivotPoint = null;
            node.subConvexHull = null;

            node.convexHull = new CoupledQueue(pointComparator);
            node.convexHull.add(point);

            return node;
        }

        // create not-leaf node
        // all parameters that set to null, will be initialized during going up the tree
        public static Node createInnerNode(Node parent, Node left, Node right, Point point){
            Node node = new Node(parent, left, right, point);
            node.pivotPoint = null;
            node.subConvexHull = null;
            node.convexHull = null;

            return node;
        }

        public Node(Node parent, Node left, Node right, Point point){
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.point = point;
        }
        public Boolean isLeaf(){
            return left == null || right == null;
        }

        public Point getPoint() {
            return point;
        }

        public CoupledQueue getSubConvexHull() {
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

        public CoupledQueue getConvexHull(){
            return this.convexHull;
        }
    }
}
