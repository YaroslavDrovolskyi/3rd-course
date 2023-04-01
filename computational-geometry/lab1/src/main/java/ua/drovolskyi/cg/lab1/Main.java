package ua.drovolskyi.cg.lab1;


import ua.drovolskyi.cg.lab1.ui.CartesianFrame;

import java.util.Arrays;

import java.util.List;

public class Main{
    public static void main(String[] args) {
        testGraph1();
    }

    private static void testGraph1(){
        Graph.Vertex[] vertices = new Graph.Vertex[]{
             new Graph.Vertex(0, new Point(4.0, 1.0)),
             new Graph.Vertex(1, new Point(6.0, 2.0)),
             new Graph.Vertex(2, new Point(2.5, 2.5)),
             new Graph.Vertex(3, new Point(9.0, 3.0)),
             new Graph.Vertex(4, new Point(8.5, 4.5)),
             new Graph.Vertex(5, new Point(7.0, 5.0)),
             new Graph.Vertex(6, new Point(11.0, 5.5)),
             new Graph.Vertex(7, new Point(2.5, 7.0)),
             new Graph.Vertex(8, new Point(4.5, 7.0)),
             new Graph.Vertex(9, new Point(6.5, 7.0)),
             new Graph.Vertex(10, new Point(5.5, 8.0)),
             new Graph.Vertex(11, new Point(1.0, 9.5)),
             new Graph.Vertex(12, new Point(5.5, 11.0))
        };

        Graph.Edge[] edges = new Graph.Edge[]{
                new Graph.Edge(0, vertices[1], vertices[3]),
                new Graph.Edge(1, vertices[2], vertices[5]),
                new Graph.Edge(2, vertices[3], vertices[4]),
                new Graph.Edge(3, vertices[8], vertices[9]),
                new Graph.Edge(4, vertices[7], vertices[10]),
                new Graph.Edge(5, vertices[6], vertices[10]),
                new Graph.Edge(6, vertices[6], vertices[12])
        };

        Graph graph = new Graph(vertices, edges);
        System.out.println("\n\n\nIs graph correct: "+ GraphUtils.isCorrect(graph));
        System.out.println("Is graph regular: "+ GraphUtils.isRegular(graph) + "\n\n\n");

        // regularizing
        System.out.println("GRAPH BEFORE REGULARIZING:");
        System.out.println(graph + "\n\n");

        CartesianFrame frame1 = new CartesianFrame();
        frame1.getPanel().drawGraph(graph);
        frame1.showUI();

        // regularize graph
        GraphRegularizator regularizator = new GraphRegularizator();
        regularizator.regularize(graph);

        System.out.println("\n\n\nGRAPH AFTER REGULARIZING: \n" + graph + "\n\n");

        CartesianFrame frame2 = new CartesianFrame();
        frame2.getPanel().drawGraph(graph);
        frame2.showUI();

        System.out.println("\n\n\nIs graph regular: "+ GraphUtils.isRegular(graph) + "\n\n\n");



        GraphUtils.balanceByWeight(graph);
        System.out.println("\n\n\nGraph after balance: ");
        System.out.println(graph);
        System.out.println("Is graph balanced by weigh: " + GraphUtils.isBalancedByWeight(graph));

    }


    private static void testDoublyConnectedEdgeList(){
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
        /////////////////////////////////////////////////////////////////////////

        System.out.println(graph);

        GraphRegularizator regularizator = new GraphRegularizator();
        regularizator.regularize(graph);

        System.out.println("\n\n\nGraph after regularization: " + graph);


        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawGraph(graph);
        frame.showUI();


    }

}