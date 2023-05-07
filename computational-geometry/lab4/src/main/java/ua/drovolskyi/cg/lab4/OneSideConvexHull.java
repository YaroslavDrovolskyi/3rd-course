package ua.drovolskyi.cg.lab4;

import java.util.*;



/**
 * Possible improvements for class:
 <ul>
    <li>Implement CoupledQueue in that way to split it in O(log(n)) time (instead of O(n))
        <p>(this will reduce time complexity of inserting and removing points)</p>
    </li>
    <li>Implement CoupledQueue in that way to couple them in O(log(n)) time (instead of O(nlog(n)))
        <p>(this will reduce time complexity of going up after inserting and removing point)</p></li>
    <li>When finding bridge, use iterator for CoupledQueue object instead of getNextBounded() and getPrevBounded().
        <p>(this will reduce the time complexity of finding bridge from O(log^2) to O(log))</p></li>
    <li>Make optionally storing or not the convexHull in each node
        (for this you need to uncomment/comment specific code in fixGoUp()()).
        Storing convexHull in each node increases space complexity, but it is useful for
        educational purposes and debug (if you want to display it using visualizer)
        <p>/Not storing convexHull in node means keep this field null.
        We can't remove this field because it is used in inserting and removing point/</p></li>
    <li>In inserting and removing, if no changes have been performed,
        make fixGoUp() only setting convexHull of n and of n.sibling to null
        (instead of real fixing)</li>
    <li>Handle case when connecting two convexHulls that contains horizontal lines.
        For example, now if left CH is a-b and right CH is c-d, (a,b,c,d have the same y-coordinates)
        their common convex hull is a-d, but correct is a-b-c-d.
        For better understanding launch Main.testHorizontalPoints()
        <p>The same situation is when a-b-c-d have the same x-coordinates</p></li>
 </ul>

 <br>
 <p>Table of time complexities of some operations in algorithm:</p>

 <pre>
 |        Operation       | Ideal complexity | Implementation complexity |
 |------------------------|------------------|---------------------------|
 | CoupledQueue.split()   | O(log(n))        | O(n)                      |
 | Find bridge            | O(log(n))        | O(log^2(n))               |
 | CoupledQueue.couple()  | O(log(n))        | O(n*log(n))               |
 </pre>
 <p>Ways how to fix non-ideal complexities described above in Possible improvements.</p>

 <br>
 <br>
 <p>Sources:</p>
 <ul>
    <li>lection</li>
    <li>Algorithm (same with lections):
        https://neerc.ifmo.ru/wiki/index.php?title=%D0%94%D0%B8%D0%BD%D0%B0%D0%BC%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F_%D0%B2%D1%8B%D0%BF%D1%83%D0%BA%D0%BB%D0%B0%D1%8F_%D0%BE%D0%B1%D0%BE%D0%BB%D0%BE%D1%87%D0%BA%D0%B0_(%D0%B4%D0%BE%D1%81%D1%82%D0%B0%D1%82%D0%BE%D1%87%D0%BD%D0%BE_log%5E2_%D0%BD%D0%B0_%D0%B4%D0%BE%D0%B1%D0%B0%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5/%D1%83%D0%B4%D0%B0%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5)</li>
    <li>Intersections of lines: https://uk.wikipedia.org/wiki/%D0%9F%D0%B5%D1%80%D0%B5%D1%82%D0%B8%D0%BD_%D0%BF%D1%80%D1%8F%D0%BC%D0%B8%D1%85</li>
 </ul>
 */
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

    public static Comparator<Point> getTopPointComparator(){
        return topPointComparator;
    }

    public static Comparator<Point> getBottomPointComparator(){
        return bottomPointComparator;
    }

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

    // check if points belongs to convex hull
    public Boolean contains(Point p){
        if(root != null){
            return root.subConvexHull.contains(p);
        }
        return false;
    }

    public CoupledQueue getPoints(){
        if(getRoot() != null){
            return getRoot().getSubConvexHull();
        }
        return null;
    }

    public NavigableSet<Point> getAllPoints(){
        NavigableSet<Point> result = new TreeSet<>(pointComparator);
        if(getRoot() != null){
            getAllPointsImpl(getRoot(), result);
        }
        return result;
    }

    private void getAllPointsImpl(Node node, NavigableSet<Point> points){
        if(node.isLeaf()){
            points.add(node.getPoint());
        }
        else{
            getAllPointsImpl(node.getLeft(), points);
            getAllPointsImpl(node.getRight(), points);
        }
    }

    public Boolean isEmpty(){
        return root == null;
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

    public void remove(Point p){
        if(this.root == null){
            throw new IllegalStateException("OneSideConvexHull is empty");
        }
        root.convexHull = root.subConvexHull;
        removeFromSubtreeGoDown(root, p);
    }


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
            fixGoUp(node);
        }
    }

    // Recursively go up from node to root and for each node n on the way do following:
    //      - calc and set subConvexHull of n and n.sibling
    //      - calc and set pivotPoint of n.parent
    // All mentioned actions are performing only because n and its sibling have correct convexHull fields
    // So, this function can be called ONLY IF convexHull of n and of n.sibling are not null
    // This method is used to fix nodes fields after inserting or removing
    // This method set convexHull of n and n.sibling to null (if you uncomment some code in the method body)
    private void fixGoUp(Node node){
        if(this.root == node) {
            node.subConvexHull = node.convexHull;

            ///////////////// uncomment it if you don't want to store convex hull in each node
            // node.convexHull = null;
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

            fixGoUp(parent);

            ///////////////// uncomment it if you don't want to store convex hull in each node
            // node.convexHull = null;
            // sibling.convexHull = null;
        }
    }

    private void removeFromSubtreeGoDown(Node node, Point p){
        // here node.convexHull is filled

        if(!node.isLeaf()){
            List<CoupledQueue> splitting = CoupledQueue.splitLeftmost(node.convexHull, node.pivotPoint);

            node.left.convexHull = CoupledQueue.couple(splitting.get(0), node.left.subConvexHull);
            node.right.convexHull = CoupledQueue.couple(node.right.subConvexHull, splitting.get(1));

            if(pointComparator.compare(p, node.point) <= 0){
                removeFromSubtreeGoDown(node.left, p);
            }
            else{
                removeFromSubtreeGoDown(node.right, p);
            }
        }
        else{
            if(pointComparator.compare(p, node.getPoint()) != 0){ // if tree does not contain p
                fixGoUp(node);
            }
            else{
                if(node.parent != null){
                    Node parent = node.getParent();
                    Node sibling = node.getSibling();

                    if(node.parent.parent != null){
                        if(node.parent.isLeftChild()){ // node parent is left child
                            if(node.isLeftChild()){ // case 1
                                parent.parent.left = sibling;
                                sibling.parent = parent.parent;
                            }
                            else{ // case 2
                                parent.parent.left = sibling;
                                sibling.parent = parent.parent;
                                sibling.parent.point = sibling.point;
                            }

                        }
                        else{ // node parent is right child
                            if(node.isRightChild()){ // case 3
                                parent.parent.right = sibling;
                                sibling.parent = parent.parent;
                                fixMaxPointInFirstRightParent(sibling);
                            }
                            else{ // case 4
                                parent.parent.right = sibling;
                                sibling.parent = parent.parent;
                            }
                        }
                        fixGoUp(sibling);
                    }
                    else{ // tree contains node, and its sibling(with sibling subtree) and root
                        // remove node and its parent (current root), make node's sibling a root

                        sibling.parent = null;
                        this.root = sibling;
//                        sibling.subConvexHull = sibling.convexHull;
                        fixGoUp(this.root);
                    }
                }
                else{ // if node is root
                    this.root = null;
                }
            }
        }
    }


    // go to the first node n whose left subtree contains newNode
    // in order to fix point of n (max point in left subtree)
    // node.point must be max point in left subtree in which node is located
    private void fixMaxPointInFirstRightParent(Node node){
        Point maxPoint = node.getPoint();
        while(node.parent != null && node != node.parent.left){
            node = node.parent;
        }
        if(node.parent != null){
            node.parent.point = maxPoint;
        }
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


    // p1 is point from left convex hull, p2 is from right one
    // x is x-coordinate of the biggest point in left subtree we now processing
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
                    // if intersection is null (l1 l2 are parallel or are overlapping) consider intersection is left to l
                    // is intersection is on l, consider intersection is left to l
                    if(intersection == null ||
                        MathUtils.areEqual(intersection.getX(), x) ||
                            intersection.getX() < x){ // p is left to line l
                        p1 = leftConvexHull.getNextPointBounded(p1);
                        // p2 does not change
                    }
                    else{ // p is right to line l
                        // p1 does not change
                        p2 = rightConvexHull.getPrevPointBounded(p2);
                    }
                }
                else if(typeP2 == PointType.SUPPORTING){
                    p1 = leftConvexHull.getNextPointBounded(p1);
//                    p2 = rightConvexHull.getNextPointBounded(p2);
                }
                else if(typeP2 == PointType.CONVEX){
                    p2 = rightConvexHull.getNextPointBounded(p2);
                }
            }
            else if(typeP1 == PointType.SUPPORTING){
                if(typeP2 == PointType.CONCAVE){
//                    p1 = leftConvexHull.getPrevPointBounded(p1);
                    p2 = rightConvexHull.getPrevPointBounded(p2);
                }
                else if(typeP2 == PointType.SUPPORTING){
                    return Arrays.asList(p1, p2);
                }
                else if(typeP2 == PointType.CONVEX){
//                    p1 = leftConvexHull.getPrevPointBounded(p1);
                    p2 = rightConvexHull.getNextPointBounded(p2);
                }
            }
            else if(typeP1 == PointType.CONVEX){
                if(typeP2 == PointType.CONCAVE){
                    p1 = leftConvexHull.getPrevPointBounded(p1);
                }
                else if(typeP2 == PointType.SUPPORTING){
                    p1 = leftConvexHull.getPrevPointBounded(p1);
//                    p2 = rightConvexHull.getNextPointBounded(p2);
                }
                else if(typeP2 == PointType.CONVEX){
                    p1 = leftConvexHull.getPrevPointBounded(p1);
                    p2 = rightConvexHull.getNextPointBounded(p2);
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


    // if point is on the line, then it is UNDER the line, and method returns 1
    // if point is null, then it is UNDER the line, and method returns 1
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
        private Point point; // point (if leaf node), or maximum in left subtree (otherwise)
        private Point pivotPoint = null; // it is left end of bridge between convex hulls
        private CoupledQueue convexHull = null; // is convex hull for all points in node's subtree
        // part of node's convex hull that does not belong to convex hull of node.parent
        private CoupledQueue subConvexHull = null;


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

        public Node getSibling(){
            if(this.parent != null){
                if(this == this.parent.left){ // this is left child
                    return this.parent.right;
                }
                else{ // this is right child
                    return this.parent.left;
                }
            }
            return null;
        }

        public Boolean isLeftChild(){
            if(this.parent != null){
                return this == this.parent.left;
            }
            return null;
        }

        public Boolean isRightChild(){
            if(this.parent != null){
                return this == this.parent.right;
            }
            return null;
        }
    }
}
