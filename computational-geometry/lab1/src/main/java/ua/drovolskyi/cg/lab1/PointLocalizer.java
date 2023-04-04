package ua.drovolskyi.cg.lab1;

import ua.drovolskyi.cg.lab1.result.*;
import ua.drovolskyi.cg.lab1.ui.CartesianFrame;
import ua.drovolskyi.cg.lab1.ui.CliUtils;

import java.util.List;

public class PointLocalizer {
    /**
     * Performs preliminary processing before chains localization algorithm.
     * Builds full set of chains for graph (planar subdivision).
     * <p>Time complexity: O(n*log(n)), where n is number of vertices in graph</p>
     * @param graph is a PLANAR graph as a lists of
     * @return full set of chains as list of chains sorted from leftmost chain to rightmost chain
     */
    public static Chain[] buildFullSetOfChains(Graph graph){
        System.out.println("Input graph: " + graph);
        CartesianFrame.displayGraph(graph, "Input graph");

        if(!GraphUtils.isCorrect(graph)){
            throw new RuntimeException("Graph must be correct: correct order of indexes of vertices," +
                    " and edge (i->j) can exist only when i<j");
        }
        System.out.println("\nGraph is correct\n\n");

        CliUtils.waitForKey();

        // regularize graph
        if(!GraphUtils.isRegular(graph)){
            GraphRegularizator.regularize(graph);
            System.out.println("Graph after regularization: " + graph);
            CartesianFrame.displayGraph(graph, "Regularized graph");
        }
        else{
            System.out.println("Graph is regular");
        }

        CliUtils.waitForKey();

        // balance weight of edges in graph
        balanceByWeight(graph);
        System.out.println("Graph after balancing a weight of edges: " + graph);

        CliUtils.waitForKey();

        // build full set of chains
        FullSetOfChainsBuilder chainsBuilder = new FullSetOfChainsBuilder();
        return chainsBuilder.build(graph).toArray(new Chain[0]);
    }

    /**
     * Performs the chain algorithm of localizing point on planar subdivision
     * <p>Time complexity: O(log2(n)), where n is number of vertices in graph</p>
     * @param chains is full set of chains of planar graph. chains in list must be sorted
     *               from leftmost chain to rightmost chain
     * @param p is point to localize
     * @return
     */
    public static PointLocalizationResult localize(Chain[] chains, Point p){

        // if point is above or under the graph
        if(p.getY() < getStartVertex(chains).getCoords().getY()){
            return resultPointOutsideChainsVertically(p, -1);
        }
        if(p.getY() > getEndVertex(chains).getCoords().getY()){
            return resultPointOutsideChainsVertically(p, 1);
        }

        // if point is left or right to the graph
        if(relativePositionToChain(chains[0], p) == -1){
           return resultPointOutsideChainsHorizontally(p, chains[0], -1);
        }
        if(relativePositionToChain(chains[chains.length - 1], p) == 1){
            return resultPointOutsideChainsHorizontally(p, chains[chains.length - 1], 1);
        }


        // binary search (find two chains where p is between them)
        int low = 0;
        int high = chains.length - 1;
        int mid = 0;
        while(high > low + 1){
            mid = (low + high) / 2;
            if (relativePositionToChain(chains[mid], p) == -1){ // point is left to mid-chain
                high = mid;
            }
            else if (relativePositionToChain(chains[mid], p) == 1){ // point is right to mid-chain
                low = mid;
            }
            else{ // if p is on mid-chain
                return resultPointOnChain(p, chains[mid]);
            }
        }
        // now we know that p is between chains[low] and chains[high] or on them

        if(relativePositionToChain(chains[low], p) == 0){ // p is on chains[low]
            return resultPointOnChain(p, chains[low]);
        }
        else if(relativePositionToChain(chains[high], p) == 0){ // p is on chains[high]
            return resultPointOnChain(p, chains[high]);
        }
        else{ // point is exactly between chains
            return resultPointBetweenChains(p, chains[low], chains[high]);
        }


        // ++ convert list to graph
        // ++ check if graph is correct
        // ++ check if graph is regular
        // ++ regularize graph
        // ++ weight-balancing of graph
        // ++ build full set of chains

        // ++ localization point through chains (check when point is put of bounds of chains)
        // ++ build result
    }

    /**
     * Balance graph by weights of edges.
     * After applying this function to graph we get that
     * for each vertex v in graph following is true: weight(IN(v)) = weight(OUT(v)).
     * @param graph is REGULAR graph
     */
    public static void balanceByWeight(Graph graph){
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);
        Graph.Edge[] edges = graph.getEdges().toArray(new Graph.Edge[0]);

        // initializing
        for(Graph.Edge e : edges){
            e.setWeight(1);
        }

        // first pass (from top to down)
        for(int i = 1; i < vertices.length - 1; i++){
            Graph.Vertex v = vertices[i];
            int inputWeight = v.getInputEdgesWeight();
            int outputWeight = v.getOutputEdgesWeight();
            Graph.Edge leftOutputEdge = v.getLeftOutputEdge();
            if(inputWeight > outputWeight){
                leftOutputEdge.setWeight(inputWeight - outputWeight + 1);
            }
        }

        // second pass (from down to top)
        for(int i = vertices.length - 2; i >= 1; i--){
            Graph.Vertex v = vertices[i];
            int inputWeight = v.getInputEdgesWeight();
            int outputWeight = v.getOutputEdgesWeight();
            Graph.Edge leftInputEdge = v.getLeftInputEdge();
            if(outputWeight > inputWeight){
                int w = outputWeight - inputWeight + leftInputEdge.getWeight();
                leftInputEdge.setWeight(w);
            }
        }
    }


    private static PointLocalizationResult resultPointOnChain(Point p, Chain chain){
        // determine edge
        int edgeIndex = localize(chain.getVertices(), p);
        Graph.Edge edge = chain.getEdge(edgeIndex);

        // if p is on some vertex of this edge
        if(MathUtils.areEqual(edge.getStart().getCoords().getX(), p.getX(), 1e-6) &&
                MathUtils.areEqual(edge.getStart().getCoords().getY(), p.getY(), 1e-6)){
            return new PointOnVertex(p, edge.getStart());
        }
        if(MathUtils.areEqual(edge.getEnd().getCoords().getX(), p.getX(), 1e-6) &&
                MathUtils.areEqual(edge.getEnd().getCoords().getY(), p.getY(), 1e-6)){
            return new PointOnVertex(p, edge.getEnd());
        }

        // else p is on edge itself
        return new PointOnEdge(p, edge);
    }

    // side=-1 when p is under chains, side=1 when p is above chains
    private static PointLocalizationResult resultPointOutsideChainsVertically(Point p, int side){
        return new PointOutsideChainsVertically(p, side);
    }

    // side=-1 when p is left to edge, side=1 when p is right to edge
    private static PointLocalizationResult resultPointOutsideChainsHorizontally(
            Point p, Chain chain, int side
    ){
        // determine edge
        int edgeIndex = localize(chain.getVertices(), p);
        Graph.Edge edge = chain.getEdge(edgeIndex);

        return new PointsOutsideChainsHorizontally(p, edge, side);
    }

    private static PointLocalizationResult resultPointBetweenChains(Point p, Chain leftChain, Chain rightChain){
        // determine edges
        int leftEdgeIndex = localize(leftChain.getVertices(), p);
        Graph.Edge leftEdge = leftChain.getEdge(leftEdgeIndex);

        int rightEdgeIndex = localize(rightChain.getVertices(), p);
        Graph.Edge rightEdge = rightChain.getEdge(rightEdgeIndex);

        return new PointBetweenChains(p, leftEdge, rightEdge);
    }

    // returns vertex where all chains start
    private static Graph.Vertex getStartVertex(Chain[] chains){
        Graph.Vertex[] vertices = chains[0].getVertices();
        return vertices[0];
    }

    // return vertex where all chains end
    private static Graph.Vertex getEndVertex(Chain[] chains){
        Graph.Vertex[] vertices = chains[0].getVertices();
        return vertices[vertices.length - 1];
    }

    /**
     * Performs operation discrimination over chain and point p in two steps:
     * project all vertices of chain on OY axis, localize p between these projections, get edge;
     * check on what side point lies relative to edge
     * WARNING: p shouldn't be above or under the chain
     * @param chain
     * @param p
     * @return -1 when point is left to chain, 1 when point is right to chain, and 0 when point is on chain
     */
    private static Integer relativePositionToChain(Chain chain, Point p){
        Graph.Vertex[] vertices = chain.getVertices();

        // first step (find edge)
        int edgeIndex = localize(vertices, p);

        if(edgeIndex == -1 || edgeIndex == vertices.length - 1){
            throw new RuntimeException("Point shouldn't be above or under the chain");
        }

        Graph.Edge edge = chain.getEdge(edgeIndex);

        // second step (check if point is left or right to edge)
        if(GeometricUtils.isInSegment(
                edge.getStart().getCoords(), edge.getEnd().getCoords(), p)){
            return 0; // point is on chain
        }
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

    /**
     * Localize point p in projections of vertices on OY axis
     * @param vertices is array of vertices (vertices should be in correct order (from bottom to top))
     * @param p
     * @return index of interval where p is located (-1 means point is under the bottom vertex)
     */
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