package ua.drovolskyi.cg.lab2;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;

public class TwoDTreeVisualizer {

    public static void visualize(TwoDTree tree, String filename) throws IOException {
        String dotString = toGraphvizString(tree);

        MutableGraph g = new Parser().read(dotString);
        Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File(filename));
    }
    public static String toGraphvizString(TwoDTree tree){
        StringBuilder sb = new StringBuilder();

        sb.append("digraph G{\n");
        toGraphvizStringImpl(tree.getRoot(), null, sb);
        sb.append("}");

        return sb.toString();
    }

    private static void toGraphvizStringImpl(TwoDTree.Node node, TwoDTree.Node parent, StringBuilder sb){
        if(node == null){
            return;
        }

        sb.append(node.getPointsOnLine().get(0).getId());
        sb.append("\n");

        if(parent != null){
            sb.append(parent.getPointsOnLine().get(0).getId());
            sb.append(" -> ");
            sb.append(node.getPointsOnLine().get(0).getId());
            sb.append("\n");
        }

        toGraphvizStringImpl(node.getLeft(), node, sb);
        toGraphvizStringImpl(node.getRight(), node, sb);
    }
}

/*
Example about Graphviz in Java: https://github.com/nidi3/graphviz-java
Another example: https://medium.com/objective-look/graphviz-java-a9f325a14d65
 */