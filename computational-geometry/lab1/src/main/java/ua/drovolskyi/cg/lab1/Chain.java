package ua.drovolskyi.cg.lab1;

import java.util.*;

public class Chain {
    private final AbstractMap<Integer, Graph.Vertex> vertices = new TreeMap<>();
    private final List<Graph.Edge> edges = new ArrayList<>();

    // algorithm of adding edges guarantees that vertices will be added from bottom to top (in correct order)
    public void addEdge(Graph.Edge e){
        edges.add(e);
        vertices.put(e.getStart().getId(), e.getStart());
        vertices.put(e.getEnd().getId(), e.getEnd());
    }

    public boolean isEmpty(){
        return vertices.isEmpty();
    }

    // returns vertices in correct order (from down to top)
    public Graph.Vertex[] getVertices(){
        return vertices.values().toArray(new Graph.Vertex[0]);
    }
    public List<Graph.Edge> getEdges(){
        return edges;
    }

    public Graph.Edge getEdge(int index){
        return edges.get(index);
    }

    @Override
    public String toString(){
        Graph.Vertex[] vertices = getVertices();
        if(vertices.length == 0){
            return "{EMPTY}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Graph.Vertex v : vertices){
            sb.append(v.getId());
            sb.append(" -> ");
        }
        sb.delete(sb.length() - 4, sb.length());
        sb.append("}");

        return sb.toString();
    }

}