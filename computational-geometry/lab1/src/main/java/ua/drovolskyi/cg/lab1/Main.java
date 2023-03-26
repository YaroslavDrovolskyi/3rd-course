package ua.drovolskyi.cg.lab1;


import ua.drovolskyi.cg.lab1.ui.CartesianFrame;

import java.util.Arrays;
import java.util.List;

public class Main{
    public static void main(String[] args) {
        DoublyConnectedEdgeList.Vertex[] vertices = {
                new DoublyConnectedEdgeList.Vertex(0, new Point(5.0, 1.0)),
                new DoublyConnectedEdgeList.Vertex(1, new Point(8.0, 3.0)),
                new DoublyConnectedEdgeList.Vertex(2, new Point(2.0, 5.0)),
                new DoublyConnectedEdgeList.Vertex(3, new Point(5.5, 7.0)),
                new DoublyConnectedEdgeList.Vertex(4, new Point(7.5, 9.0)),
                new DoublyConnectedEdgeList.Vertex(5, new Point(5.0, 9.5)),
                new DoublyConnectedEdgeList.Vertex(6, new Point(7.0, 10.0)),
                new DoublyConnectedEdgeList.Vertex(7, new Point(1.0, 11.0)),
                new DoublyConnectedEdgeList.Vertex(8, new Point(4.0, 12.0))
        };

        DoublyConnectedEdgeList.Edge[] edges = {
                new DoublyConnectedEdgeList.Edge(0, vertices[0], vertices[1], 0,0),
                new DoublyConnectedEdgeList.Edge(1, vertices[0], vertices[2], 0,0),
                new DoublyConnectedEdgeList.Edge(2, vertices[0], vertices[3], 0,0),
                new DoublyConnectedEdgeList.Edge(3, vertices[0], vertices[4], 0,0),
                new DoublyConnectedEdgeList.Edge(4, vertices[1], vertices[4], 0,0),
                new DoublyConnectedEdgeList.Edge(5, vertices[2], vertices[3], 0,0),
                new DoublyConnectedEdgeList.Edge(6, vertices[3], vertices[4], 0,0),
                new DoublyConnectedEdgeList.Edge(7, vertices[4], vertices[5], 0,0),
                new DoublyConnectedEdgeList.Edge(8, vertices[4], vertices[6], 0,0),
                new DoublyConnectedEdgeList.Edge(9, vertices[2], vertices[5], 0,0),
                new DoublyConnectedEdgeList.Edge(10, vertices[5], vertices[6], 0,0),
                new DoublyConnectedEdgeList.Edge(11, vertices[6], vertices[8], 0,0),
                new DoublyConnectedEdgeList.Edge(12, vertices[2], vertices[7], 0,0),
                new DoublyConnectedEdgeList.Edge(13, vertices[7], vertices[8], 0,0),
                new DoublyConnectedEdgeList.Edge(14, vertices[3], vertices[5], 0,0),
                new DoublyConnectedEdgeList.Edge(15, vertices[5], vertices[8], 0,0)
        };

        DoublyConnectedEdgeList list = new DoublyConnectedEdgeList(Arrays.asList(edges), vertices.length);

        Graph graph = new Graph(list);
        System.out.println(graph);
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawGraph(graph);
        frame.showUI();


    }
}