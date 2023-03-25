package ua.drovolskyi.cg.lab1;

import java.util.ArrayList;
import java.util.List;

public class DoublyConnectedEdgeList {
    private List<Edge> edges;
    private Integer numberOfVertices = 0;

    public DoublyConnectedEdgeList(List<Edge> edges){
        this.edges = edges;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Integer getNumberOfVertices(){
        return numberOfVertices;
    }

    public Integer getNumberOfEdges(){
        return edges.size();
    }

    private static class Face{

    }
    public static class Vertex{
        private final Integer id;
        private Point coords;

        public Vertex(Integer id, Point coords){
            this.id = id;
            this.coords = coords;
        }

        public Integer getId() {
            return id;
        }

        public Point getCoords(){
            return coords;
        }
    }

    public static class Edge{
        private final Integer id;
        private Vertex start;
        private Vertex end;
        private Integer prevEdgeId;
        private Integer nextEdgeId;
        private Face left = null;
        private Face right = null;

        public Edge(Integer id, Vertex start, Vertex end, Integer prevEdgeId, Integer nextEdgeId){
            this.id = id;
            this.start = start;
            this.end = end;
            this.prevEdgeId = prevEdgeId;
            this.nextEdgeId = nextEdgeId;
        }

        public Integer getId() {
            return id;
        }

        public Vertex getStart() {
            return start;
        }

        public Vertex getEnd() {
            return end;
        }
    }
}
