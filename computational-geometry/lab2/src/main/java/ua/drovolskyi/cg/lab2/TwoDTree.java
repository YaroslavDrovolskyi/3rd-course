package ua.drovolskyi.cg.lab2;

import java.util.List;

public class TwoDTree {
    private final Node root;

    public TwoDTree(Node root){
        this.root = root;
    }

    public Node getRoot(){
        return this.root;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("2dTree{\n");
        toStringImpl(root, sb);
        sb.append("}");

        return sb.toString();
    }

    private void toStringImpl(Node node, StringBuilder sb){
        if(node == null){
            return;
        }

        sb.append(node.getCuttingLine().getType());
        sb.append(" cutting line, coord = ");
        sb.append(node.getCuttingLine().getCoordinate());
        sb.append("\n");

        toStringImpl(node.getLeft(), sb);
        toStringImpl(node.getRight(), sb);
    }

    public static class Node{
        private final List<Point> pointsOnLine;
        private final CuttingLine cuttingLine;
        private final Node left;
        private final Node right;

        public Node(List<Point> pointsOnLine, CuttingLine cuttingLine, Node left, Node right) {
            this.pointsOnLine = pointsOnLine;
            this.cuttingLine = cuttingLine;
            this.left = left;
            this.right = right;
        }

        public List<Point> getPointsOnLine() {
            return pointsOnLine;
        }

        public CuttingLine getCuttingLine() {
            return cuttingLine;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

    }


}
