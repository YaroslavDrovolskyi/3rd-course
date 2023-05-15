package ua.drovolskyi.compilers.lab2.parser;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.File;
import java.io.IOException;

public class AstVisualizer {
    private Integer nodeCount = 0;

    public void visualize(AbstractSyntaxTree tree, String filename) throws IOException {
        String dotString = toGraphvizString(tree);

        MutableGraph g = new guru.nidi.graphviz.parse.Parser().read(dotString);
        Graphviz.fromGraph(g).width(14000).render(Format.PNG).toFile(new File(filename));
    }

    private String toGraphvizString(AbstractSyntaxTree tree){
        StringBuilder sb = new StringBuilder();

        sb.append("digraph G{\n");
        toGraphvizStringImpl(tree.getRoot(), null, sb);
        sb.append("labelloc=\"t\"");
        sb.append("}");

        return sb.toString();
    }

    private void toGraphvizStringImpl(AstNode node, Integer parentCount, StringBuilder sb){
        if(node == null){
            return;
        }

        nodeCount++;

        int nodeId = nodeCount;

        // put information about node
        sb.append(nodeId);
        sb.append("[label=\"");
//        sb.append(nodeId).append("\n");
        sb.append(node.getType()).append("\n");
        if(node.getValue() != null){
            String value = node.getValue();
            if(node.getType() == AstNode.Type.LITERAL_STRING){
                value = value.substring(1, value.length() - 1); // remove " at start and at end
            }
            sb.append(value).append("\n");
        }
        sb.append("\"]\n");

        // add connection with parent
        if(parentCount != null){
            sb.append(parentCount).append(" -> ").append(nodeId).append("\n");
        }

        for(AstNode child : node.getChilds()){
            toGraphvizStringImpl(child, nodeId, sb);
        }
    }
}
