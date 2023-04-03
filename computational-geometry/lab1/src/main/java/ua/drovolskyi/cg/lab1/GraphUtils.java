package ua.drovolskyi.cg.lab1;

import java.util.Collection;
import java.util.Comparator;

public class GraphUtils {

    private static Comparator<Point> pointComparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(p1.getY() < p2.getY() ||
                    (MathUtils.areEqual(p1.getY(), p2.getY(), 1e-6) &&
                            p1.getX() < p2.getX())){
                return -1;
            }
            else if(MathUtils.areEqual(p1.getX(), p2.getX(), 1e-6) &&
                    MathUtils.areEqual(p1.getY(), p2.getY(), 1e-6)){
                return 0;
            }
            else{
                return 1;
            }
        }
    };

    // check order of indexes of vertices
    // check that edge (i->j) can exist only when i<j
    public static boolean isCorrect(Graph graph){
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);
        for(int i = 0; i < vertices.length - 1; i++){
            if(pointComparator.compare(vertices[i].getCoords(),
                    vertices[i+1].getCoords()) != -1){
                return false;
            }
        }

        Graph.Edge[] edges = graph.getEdges().toArray(new Graph.Edge[0]);
        for(int i = 0; i < edges.length; i++){
            Graph.Edge e = edges[i];
            if(e.getStart().getId() > e.getEnd().getId()){
                return false;
            }
        }

        return true;
    }

    // check if for each vertex (except first and last) v_j exist edges (v_i,v_j) and (v_j, v_k) such as i < j < k
    public static boolean isRegular(Graph graph){
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);
        for(int i = 1; i < vertices.length - 1; i++){
            if(!isRegular(vertices[i])){
                return false;
            }
        }
        return true;
    }

    private static boolean isRegular(Graph.Vertex v){
        boolean validInputEdgeExists = false;
        boolean validOutputEdgeExists = false;

        for(Graph.Edge inputEdge : v.getInputEdges()){
            if(inputEdge.getStart().getId() < v.getId()){
                validInputEdgeExists = true;
                break;
            }
        }

        for(Graph.Edge outputEdge : v.getOutputEdges()){
            if(outputEdge.getEnd().getId() > v.getId()){
                validOutputEdgeExists = true;
                break;
            }
        }

        return validInputEdgeExists && validOutputEdgeExists;
    }



    public static boolean isBalancedByWeight(Graph graph){
        Graph.Vertex[] vertices = graph.getVertices().toArray(new Graph.Vertex[0]);
        for(int i = 1; i < vertices.length - 1; i++){
            if(vertices[i].getInputEdgesWeight() !=
            vertices[i].getOutputEdgesWeight()){
                return false;
            }
        }
        return true;
    }
}
