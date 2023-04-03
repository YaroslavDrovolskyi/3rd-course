package ua.drovolskyi.cg.lab1;

import java.util.List;

public class PointLocalizer {

    public static void localize(DoublyConnectedEdgeList edgeList, Point p){
        Graph graph = new Graph(edgeList);
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);

        if(!GraphUtils.isCorrect(graph)){
            throw new RuntimeException("Graph must be correct: correct order of indexes of vertices," +
                    " and edge (i->j) can exist only when i<j");
        }

        // regularize graph
        if(!GraphUtils.isRegular(graph)){
            GraphRegularizator.regularize(graph);
        }

        // check if p is on vertex
        Graph.Vertex pVertexLocation = findVertexWherePointLies(graph, p);
        if(pVertexLocation != null){
            ///////////////////////////////////// return corresponding result
        }

        // check if p is on edge
        Graph.Edge pEdgeLocation = findEdgeWherePointLies(graph, p);
        if(pEdgeLocation != null){
            //////////////////////////////////// return corresponding result
        }

        // if point is above or under the graph
        if(p.getY() < vertices[0].getCoords().getY()
                || p.getY() > vertices[vertices.length - 1].getCoords().getY()){
            /////////////////// return result: point is out the graph
        }

        // balance weight of edges in graph
        GraphUtils.balanceByWeight(graph);

        // build full set of chains
        FullSetOfChainsBuilder chainsBuilder = new FullSetOfChainsBuilder();
        Chain[] chains = chainsBuilder.build(graph).toArray(new Chain[0]);

        if(relativePositionToChain(chains[0], p) == -1){
            ///////////////////// return: the left to leftmost chain
            /// also need to localize and determine edge
        }
        if(relativePositionToChain(chains[chains.length - 1], p) == 1){
            ///////////////////// return: the right to rightmost chain
            /// also need to localize and determine edge
        }

        // point can't be on chain
        // binary search
        int low = 0;
        int high = chains.length - 1;
        int mid = 0;
        while(high > low + 1){
            mid = (low + high) / 2;
            if (relativePositionToChain(chains[mid], p) == -1){ // point is on left of mid-chain
                high = mid;
            }
            else if (relativePositionToChain(chains[mid], p) == 1){ // point is on right to mid-chain
                low = mid;
            }
        }

        ///////////////////// return that point is between chains [low] and [high]
        /// need to determine edges of this two chains

        /*
            Types of results:
                - (1) p is on vertex
                - (2) p is on edge
                - (3) p is above or under the graph
                - (4) p is left to leftmost chain (chain + left + vertices)
                - (4) p is right to rightmost chain (chain + right + vertices)
                        (this and prev case handle situation when only one chain exists)
                        (also they handled case when all chains are similar)
                - (5) Usual case: p is between two chains (chains + 4 vertices)
         */

        // When some chains are similar then point never will be between them
        // (because it is impossible, and point can't be on chain)
        // so binary search at the end will give us two different chains, and the point will be between them



        // + convert list to graph
        // + check if graph is correct
        // + check if graph is regular
        // + regularize graph
        // + check if point on edge or on vertex of graph


        // + weight-balancing of graph
        // + build full set of chains
        // + localization point through chains (check when point is put of bounds of chains)
            // (handle case when point is on edge (because new edges have been added during regularization))
            // при локалізації додатковво перевіряти чи саме на ребрі лежить
            // якщо ребро горизонтальне, то якщо нам показує що на прямій (ікси однакові), то треба перевірити ігреки
            // і сказати зілва (point.y < edge.end.y) чи справа (point.y > edge.end.y)
            // point can't be on chain because we have checked this
        // build result
    }

    // return vertex where point lies on, or null if such vertex does not exist
    public static Graph.Vertex findVertexWherePointLies(Graph graph, Point p){
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);

        for(Graph.Vertex v : vertices){
            Point vCoords = v.getCoords();
            if(MathUtils.areEqual(vCoords.getX(), p.getX(), 1e-6) &&
                    MathUtils.areEqual(vCoords.getY(), p.getY(), 1e-6)){
                return v;
            }
        }
        return null;
    }

    // return edge where point lies on, or null if such edge does not exist
    public static Graph.Edge findEdgeWherePointLies(Graph graph, Point p) {
        Graph.Edge[] edges = graph.getEdges().toArray(new Graph.Edge[0]);

        for (Graph.Edge e : edges) {
            if (GeometricUtils.isInSegment(e.getStart().getCoords(), e.getEnd().getCoords(), p)) {
                return e;
            }
        }

        return null;
    }

    // performs operation discrimination over chain and point p
    // p shouldn't be above or under the chain
    // p shouldn't be on any edge or any vertex (this case must be already handled)
    // returns -1 when point is left to chain, and 1 when point is right to chain
    public static Integer relativePositionToChain(Chain chain, Point p){
        Graph.Vertex[] vertices = chain.getVertices();

        // first step (find edge)
        int edgeIndex = localize(vertices, p);

        if(edgeIndex == -1 || edgeIndex == vertices.length - 1){
            throw new RuntimeException("Point shouldn't be above or under the chain");
        }

        Graph.Edge edge = chain.getEdge(edgeIndex);

        // second step (check if point is left or right to edge)
        if(MathUtils.areEqual(edge.getStart().getCoords().getY(),
                edge.getEnd().getCoords().getY(),1e-6)){ // if edge is horizontal
            if(p.getX() > edge.getEnd().getCoords().getX()){
                return 1;
            }
            else{
                return -1;
            }
        }
        else{
            return GeometricUtils.relativePosition(edge.getStart().getCoords(),
                    edge.getEnd().getCoords(), p);
        }
    }


    // localize point p using only y-coordinates
    // vertices should be in correct order (from bottom to top)
    // vertices must contain at least two vertices
    // returns index of interval where p is located (where -1 means point is under the bottom vertex)
    private static Integer localize(Graph.Vertex[] vertices, Point p){
        // if point is outside the interval
        if(p.getY() < vertices[0].getCoords().getY()){
            return -1;
        }
        if(p.getY() > vertices[vertices.length - 1].getCoords().getY()){
            return vertices.length - 1;
        }

        // binary search
        int low = 0;
        int high = vertices.length - 1;
        int mid = 0;
        while(high > low + 1){
            mid = (low + high) / 2;
            if (p.getY() <= vertices[mid].getCoords().getY()){ // point is on left of mid-vertex
                high = mid;
            }
            else if (p.getY() >= vertices[mid].getCoords().getY()){ // point is on right to mid-vertex
                low = mid;
            }
        }
        return low;
    }

}

// abstract class PointLocalization
// class CommonLocalization
// class
