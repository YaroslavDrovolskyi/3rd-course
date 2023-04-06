package ua.drovolskyi.cg.lab1.localizer;

import ua.drovolskyi.cg.lab1.geometry.GeometricUtils;
import ua.drovolskyi.cg.lab1.geometry.MathUtils;
import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.graph.GraphUtils;
import ua.drovolskyi.cg.lab1.geometry.Point;
import ua.drovolskyi.cg.lab1.localizer.result.*;
import ua.drovolskyi.cg.lab1.ui.CartesianFrame;
import ua.drovolskyi.cg.lab1.ui.CliUtils;

import java.util.List;
import java.util.Objects;

public class PointLocalizer {
    /**
     * Performs preliminary processing before chains localization algorithm.
     * Builds full set of chains for graph (planar subdivision).
     * <p>Time complexity: O(n*log(n)), where n is number of vertices in graph</p>
     * @param graph is a PLANAR graph
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
            return new PointOutsideChainsVertically(p, -1);
        }
        if(p.getY() > getEndVertex(chains).getCoords().getY()){
            return new PointOutsideChainsVertically(p, 1);
        }

        PointDiscriminationResult dr = null; // result of discrimination point to chain

        // if point is left or right to the graph
        dr = relativePositionToChain(chains[0], p);
        if(dr.side() == -1){
            return new PointsOutsideChainsHorizontally(p, dr.edge(), -1);
        }
        dr = relativePositionToChain(chains[chains.length - 1], p);
        if(dr.side() == 1){
            return new PointsOutsideChainsHorizontally(p, dr.edge(), 1);
        }


        // binary search (find two chains where p is between them)
        int low = 0;
        int high = chains.length - 1;
        int mid = 0;
        while(high > low + 1){
            mid = (low + high) / 2;
            dr = relativePositionToChain(chains[mid], p);

            if (dr.side() == -1){ // point is left to mid-chain
                high = mid;
            }
            else if (dr.side() == 1){ // point is right to mid-chain
                low = mid;
            }
            else{ // if p is on mid-chain
                return resultPointOnChain(p, dr.edge());
            }
        }
        // now we know that p is between chains[low] and chains[high] or on them

        PointDiscriminationResult drLow = relativePositionToChain(chains[low], p);
        PointDiscriminationResult drHigh = relativePositionToChain(chains[high], p);
        if(drLow.side() == 0){ // p is on chains[low]
            return resultPointOnChain(p, drLow.edge());
        }
        else if(drHigh.side() == 0){ // p is on chains[high]
            return resultPointOnChain(p, drHigh.edge());
        }
        else{ // point is exactly between chains
            return new PointBetweenChains(p, drLow.edge(), drHigh.edge());
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


    private static PointLocalizationResult resultPointOnChain(Point p, Graph.Edge edge){
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
     * @return PointDiscriminationResult of the best localizing edge (e) for given point p and one of following values:
     * -1 when point is left to e, 1 when point is right to e, and 0 when point is on e
     */
    private static PointDiscriminationResult relativePositionToChain(Chain chain, Point p){
        Graph.Vertex[] vertices = chain.getVertices();

        // first step (find best localizing edge)
        int edgeIndex = localizePointVertically(vertices, p);

        if(edgeIndex == -1 || edgeIndex == vertices.length - 1){
            throw new RuntimeException("Point shouldn't be above or under the chain");
        }

        Graph.Edge edge = getBestNeighbourLocalizingEdge(chain, edgeIndex, p);

        // second step (check if point is left or right to edge)
        if(GraphUtils.isOnEdge(edge, p)){
            return new PointDiscriminationResult(edge, 0); // point is on edge
        }
        if(GraphUtils.isHorizontal(edge)){ // if edge is horizontal
            if(p.getX() > edge.getEnd().getCoords().getX()){
                return new PointDiscriminationResult(edge, 1);
            }
            else{
                return new PointDiscriminationResult(edge, -1);
            }
        }
        else{
            return new PointDiscriminationResult(edge,
                    GeometricUtils.relativePosition(edge.getStart().getCoords(), edge.getEnd().getCoords(), p));
        }
    }

    /**
     * Localize point p in projections of vertices on OY axis
     * <p>WARNING: can produce not accurate result for point that is on one line with horizontal edge</p>
     * <p>If you have horizontal edges in chain, use method fixLocalizingEdge() after this method</p>
     * @param vertices is array of vertices (vertices should be in correct order (from bottom to top))
     * @param p
     * @return index of interval where p is located (-1 means point is under the bottom vertex)
     */
    private static Integer localizePointVertically(Graph.Vertex[] vertices, Point p){
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

    /**
     * Corrects localizing edge returned by localizePointVertically().
     * Method localizePointVertically() can give incorrect result
     * when point p is in on the line of some horizontal edges.
     * For example, method localizePointVertically() can return that point p is lying somewhere
     * in vertical region of Y-projection of edge e, but in reality p can be on e's adjacent horizontal edge.
     * <br><br>
     * Algorithm (e is input edge):
     * <p>1) check if p is on e. If yes, return it. Otherwise, go to next step.</p>
     * <p>2) If p has same y-coordinate with start of e, sequentially go to e's previous edges in chain while they are horizontal.
     * For each such edge e1 we check if p is on e1 and return e1 if it is true.
     * Otherwise we remember that e1 is the most precision localization of p on Y-axis.
     * If p is right to e1, break loop.</p>
     * <p>3) [Mirror to step 2] If p has same y-coordinate with end of e, sequentially go to e's
     * next edges in chain while they are horizontal.
     * For each such edge e2 we check if p is on e2 and return e2 if it is true.
     * Otherwise we remember that e1 is the most precision localization of p on Y-axis.
     * If p is left to e2, break loop.</p>
     * <p>4) Return result (if we haven't returned earlier).
     * In case when e is horizontal: if p is left to e return e1, else return e2.
     * In case when e is not horizontal we have iterated only through next or prev edges,
     * so return e2 or e2, respectively.</p>
     * @param chain
     * @param localizingEdgeIndex is index of edge returned by localizePointVertically() method;
     *                            it must not be -1 or chain().getEdges().size() - 1
     * @param p is point we need localize
     * @return the best localization edge for given point p chosen from horizontal edges,
     *          that neighboring to input edge
     */
    private static Graph.Edge getBestNeighbourLocalizingEdge(Chain chain, int localizingEdgeIndex, Point p){
        List<Graph.Edge> edges = chain.getEdges();
        Graph.Edge localizingEdge = edges.get(localizingEdgeIndex);

        if(GraphUtils.isOnEdge(localizingEdge, p)){
            return localizingEdge;
        }

        Graph.Edge bestPrevLocalizationEdge = null;
        if(MathUtils.areEqual(localizingEdge.getStart().getCoords().getY(), p.getY())){
            for (int i = localizingEdgeIndex - 1; i >= 0; i--){
                Graph.Edge e = edges.get(i);
                if(!GraphUtils.isHorizontal(e)){
                    break;
                }
                if(GraphUtils.isOnEdge(e, p)){
                    return e;
                }
                bestPrevLocalizationEdge = e;
                if(e.getEnd().getCoords().getX() < p.getX()){
                    break;
                }
            }
        }

        Graph.Edge bestNextLocalizationEdge = null;
        if(MathUtils.areEqual(localizingEdge.getEnd().getCoords().getY(), p.getY())){
            for(int i = localizingEdgeIndex + 1; i < edges.size(); i++){
                Graph.Edge e = edges.get(i);
                if(!GraphUtils.isHorizontal(e)){
                    break;
                }
                if(GraphUtils.isOnEdge(e, p)){
                    return e;
                }
                bestNextLocalizationEdge = e;
                if(e.getStart().getCoords().getX() > p.getX()){
                    break;
                }
            }
        }

        // return result
        if(GraphUtils.isHorizontal(localizingEdge)){
            if(localizingEdge.getStart().getCoords().getX() > p.getX()){
                return Objects.requireNonNullElse(bestPrevLocalizationEdge, localizingEdge);
            }
            else {
                return Objects.requireNonNullElse(bestNextLocalizationEdge, localizingEdge);
            }
        }
        else{ // if input edge isn't horizontal, we always have at least one of that variables = null
            if(bestPrevLocalizationEdge != null){
                return bestPrevLocalizationEdge;
            }
            else {
                return Objects.requireNonNullElse(bestNextLocalizationEdge, localizingEdge);
            }
        }
    }


    private record PointDiscriminationResult(Graph.Edge edge, int side) {
    }

}