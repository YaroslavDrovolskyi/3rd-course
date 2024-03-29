package ua.drovolskyi.cg.lab1;


import ua.drovolskyi.cg.lab1.graph.DoublyConnectedEdgeList;
import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.geometry.Point;
import ua.drovolskyi.cg.lab1.localizer.Chain;
import ua.drovolskyi.cg.lab1.localizer.GraphRegularizator;
import ua.drovolskyi.cg.lab1.localizer.PointLocalizer;
import ua.drovolskyi.cg.lab1.localizer.result.PointLocalizationResult;
import ua.drovolskyi.cg.lab1.ui.CartesianFrame;
import ua.drovolskyi.cg.lab1.ui.CliUtils;

import java.util.Arrays;

public class Main{
    public static void main(String[] args) {
        testGraph1();
    }

    private static void testGraph2(){
        DoublyConnectedEdgeList.Vertex[] vertices = new DoublyConnectedEdgeList.Vertex[]{
                new DoublyConnectedEdgeList.Vertex(0, new Point(4.0, 1.0)),
                new DoublyConnectedEdgeList.Vertex(1, new Point(6.0, 2.0)),
                new DoublyConnectedEdgeList.Vertex(2, new Point(2.5, 2.5)),
                new DoublyConnectedEdgeList.Vertex(3, new Point(9.0, 3.0)),
                new DoublyConnectedEdgeList.Vertex(4, new Point(8.5, 4.5)),
                new DoublyConnectedEdgeList.Vertex(5, new Point(7.0, 5.0)),
                new DoublyConnectedEdgeList.Vertex(6, new Point(11.0, 5.5)),
                new DoublyConnectedEdgeList.Vertex(7, new Point(2.5, 7.0)),
                new DoublyConnectedEdgeList.Vertex(8, new Point(4.5, 7.0)),
                new DoublyConnectedEdgeList.Vertex(9, new Point(6.5, 7.0)),
                new DoublyConnectedEdgeList.Vertex(10, new Point(5.5, 8.0)),
                new DoublyConnectedEdgeList.Vertex(11, new Point(1.0, 9.5)),
                new DoublyConnectedEdgeList.Vertex(12, new Point(5.5, 11.0))
        };

        DoublyConnectedEdgeList.Edge[] edges = new DoublyConnectedEdgeList.Edge[]{
                new DoublyConnectedEdgeList.Edge(0, vertices[1], vertices[3]),
                new DoublyConnectedEdgeList.Edge(1, vertices[2], vertices[5]),
                new DoublyConnectedEdgeList.Edge(2, vertices[3], vertices[4]),
                new DoublyConnectedEdgeList.Edge(3, vertices[8], vertices[9]),
                new DoublyConnectedEdgeList.Edge(4, vertices[7], vertices[10]),
                new DoublyConnectedEdgeList.Edge(5, vertices[6], vertices[10]),
                new DoublyConnectedEdgeList.Edge(6, vertices[6], vertices[12])
        };

 //       DoublyConnectedEdgeList edgeList = new DoublyConnectedEdgeList(Arrays.asList(edges), vertices.length);

 //       Chain[] chains = PointLocalizer.buildFullSetOfChains(edgeList);

///        PointLocalizationResult result = PointLocalizer.localize(chains, new Point(5.5, 5.5));
 //       System.out.println(result);
    }

    private static void testGraph1(){
        Graph.Vertex[] vertices = new Graph.Vertex[]{
             new Graph.Vertex(0, new Point(4.0, 1.0)),
             new Graph.Vertex(1, new Point(6.0, 2.0)),
             new Graph.Vertex(2, new Point(2.5, 2.5)),
             new Graph.Vertex(3, new Point(9.0, 3.0)),
             new Graph.Vertex(4, new Point(9.0, 4.5)),
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

        CliUtils.setWaitingForKey(false);

        // preliminary processing (build full set of chains)
        Chain[] chains = PointLocalizer.buildFullSetOfChains(graph);
        System.out.println("\n\nFull set of chains: ");
        CliUtils.printChains(chains);

        // localizing (algorithm applying)
        Point[] points = new Point[]{
                vertices[0].getCoords(),
                vertices[1].getCoords(),
                vertices[2].getCoords(),
                vertices[3].getCoords(),
                vertices[4].getCoords(),
                vertices[5].getCoords(),
                vertices[6].getCoords(),
                vertices[7].getCoords(),
                vertices[8].getCoords(),
                vertices[9].getCoords(),
                vertices[10].getCoords(),
                vertices[11].getCoords(),
                vertices[12].getCoords(),
                new Point(3.0, 7.0), // 7->8
                new Point(5.5, 7.0), // 8->9
                new Point(9.0, 3.5), // 3->4
                new Point(2.9, 7.133), // 7->10
                new Point(8.66, 7.84), // 6->12
                new Point(6.58, 4.767), // 2->5
                new Point(4.95, 1.475), // 0->1
                new Point(6.0, 7.5), // 9->10
                new Point(10.44, 5.43), // 5->6
                new Point(2.5,1.5),
                new Point(11.5, 1.5),
                new Point(10.5, 4.0),
                new Point(5.5, 7.5),
                new Point(5.5, 6.0),
                new Point(8.0, 8.0),
                new Point(4.5,0.5),
                new Point(10.0, 15.0),
                new Point(5.0, 10.0),
                new Point(7.2, 7.0),
                new Point(7.5, 4.0),
                new Point(9.0, 1.0),
                new Point (10.0, 10.0)
        };
        CartesianFrame.displayChainsAndPoint(chains, points, "Chains and point to localize");
        CliUtils.waitForKey();
        System.out.println("\n\n");
        for(Point p : points){
            PointLocalizationResult result = PointLocalizer.localize(chains, p);
            System.out.println("Result: " + result);
        }
    }

    /*

        Program does not see point on vertex 8, 9
        Program see point on vertex: 0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12
     */


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
                new DoublyConnectedEdgeList.Edge(0, vertices[0], vertices[1]),
                new DoublyConnectedEdgeList.Edge(1, vertices[0], vertices[2]),
                new DoublyConnectedEdgeList.Edge(2, vertices[0], vertices[3]),
                new DoublyConnectedEdgeList.Edge(3, vertices[0], vertices[4]),
                new DoublyConnectedEdgeList.Edge(4, vertices[1], vertices[4]),
                new DoublyConnectedEdgeList.Edge(5, vertices[2], vertices[3]),
                new DoublyConnectedEdgeList.Edge(6, vertices[3], vertices[4]),
                new DoublyConnectedEdgeList.Edge(7, vertices[4], vertices[5]),
                new DoublyConnectedEdgeList.Edge(8, vertices[4], vertices[6]),
                new DoublyConnectedEdgeList.Edge(9, vertices[2], vertices[5]),
                new DoublyConnectedEdgeList.Edge(10, vertices[5], vertices[6]),
                new DoublyConnectedEdgeList.Edge(11, vertices[6], vertices[8]),
                new DoublyConnectedEdgeList.Edge(12, vertices[2], vertices[7]),
                new DoublyConnectedEdgeList.Edge(13, vertices[7], vertices[8]),
                new DoublyConnectedEdgeList.Edge(14, vertices[3], vertices[5]),
                new DoublyConnectedEdgeList.Edge(15, vertices[5], vertices[8])
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
  //      frame.showUI();


    }

}