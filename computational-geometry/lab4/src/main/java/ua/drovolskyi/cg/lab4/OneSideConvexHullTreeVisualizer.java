package ua.drovolskyi.cg.lab4;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;

public class OneSideConvexHullTreeVisualizer {
    public static void visualize(OneSideConvexHull tree, String filename, String title) throws IOException {
        String dotString = toGraphvizString(tree, title);

        MutableGraph g = new Parser().read(dotString);
        Graphviz.fromGraph(g).width(1400).render(Format.PNG).toFile(new File(filename));
    }
    private static String toGraphvizString(OneSideConvexHull tree, String title){
        StringBuilder sb = new StringBuilder();

        sb.append("digraph G{\n");
        toGraphvizStringImpl(tree.getRoot(), sb);
        sb.append("labelloc=\"t\"");
        sb.append("label=\"" + title + '\"');
        sb.append("}");

        return sb.toString();
    }

    private static void toGraphvizStringImpl(OneSideConvexHull.Node node, StringBuilder sb){
        if(node == null){
            return;
        }

        // put information about node
        sb.append(node.hashCode());
        sb.append("[label=\"");
        sb.append("Point: " + node.getPoint().getId() + "\n");
        sb.append("ConvexHull: ");
        if(node.getConvexHull() != null){
            sb.append(node.getConvexHull());
        }
        else{
            sb.append("null");
        }
        sb.append("\n");

        sb.append("Pivot point: ");
        if(node.getPivotPoint() != null){
            sb.append(node.getPivotPoint().getId());
        }
        else{
            sb.append("null");
        }
        sb.append("\n");

        sb.append("SubConvexHull: " + node.getSubConvexHull() + "\n");
        sb.append("\"]\n");

        if(node.getParent() != null){
            sb.append(node.getParent().hashCode());
            sb.append(" -> ");
            sb.append(node.hashCode());
            sb.append("\n");
        }

        toGraphvizStringImpl(node.getLeft(), sb);
        toGraphvizStringImpl(node.getRight(), sb);

        // checking
        if(!node.isLeaf()){
            if(node.getLeft().getParent() != node){
                throw new RuntimeException("Node has incorrect parent references");
            }
            if(node.getRight().getParent() != node){
                throw new RuntimeException("Node has incorrect parent references");
            }
        }
    }

}
