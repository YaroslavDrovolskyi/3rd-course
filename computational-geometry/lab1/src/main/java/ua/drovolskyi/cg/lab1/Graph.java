package ua.drovolskyi.cg.lab1;

import java.util.*;

public class Graph {
    private Vertex[] vertices = null;
    private Edge[] edges = null;


    public Graph(DoublyConnectedEdgeList list){
        vertices = new Vertex[list.getNumberOfVertices()];
        edges = new Edge[list.getNumberOfEdges()];

        List<DoublyConnectedEdgeList.Edge> rawEdges = list.getEdges();
        for(DoublyConnectedEdgeList.Edge rawEdge : rawEdges){
            DoublyConnectedEdgeList.Vertex rawStartVertex = rawEdge.getStart();
            DoublyConnectedEdgeList.Vertex rawEndVertex = rawEdge.getEnd();

            Boolean startVertexExisted = true;
            Boolean endVertexExisted = true;

            // insert vertices in array if they haven't been inserted before
            if(vertices[rawStartVertex.getId()] == null) {
                startVertexExisted = false;
                vertices[rawStartVertex.getId()] = new Vertex(
                        rawStartVertex.getId(), rawStartVertex.getCoords());
            }
            if(vertices[rawEndVertex.getId()] == null) {
                endVertexExisted = false;
                vertices[rawEndVertex.getId()] = new Vertex(
                        rawEndVertex.getId(), rawEndVertex.getCoords());
            }

            Vertex start = vertices[rawStartVertex.getId()];
            Vertex end = vertices[rawEndVertex.getId()];

            // initialize edge
            edges[rawEdge.getId()] = new Edge(rawEdge.getId(), start, end);

            // add input and output edges for vertices
            if(!startVertexExisted){
                start.addOutputEdge(edges[rawEdge.getId()]);
            }
            if(!endVertexExisted){
                end.addInputEdge(edges[rawEdge.getId()]);
            }
        }
    }

    private static Double calcLength(Edge e){
        return calcDistance(e.start.coords, e.end.coords);
    }

    private static double calcDistance(Point start, Point end){
        return Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) +
                (end.getY() - start.getY()) * (end.getY() - start.getY()));

    }


    public void regularise(){

    }

    public static class Vertex{
        private final Integer id;
        private Point coords;
        private InputEdgeSet inputEdges; // counterclockwise
        private OutputEdgeSet outputEdges; // clockwise

        public Vertex(Integer id, Point coords){
            this.id = id;
            this.coords = coords;
            inputEdges = new InputEdgeSet();
            outputEdges = new OutputEdgeSet();
        }

        public Integer getId() {
            return id;
        }

        public Point getCoords(){
            return coords;
        }

        public void addInputEdge(Edge e){
            inputEdges.addEdge(e);
        }

        public void addOutputEdge(Edge e){
            outputEdges.addEdge(e);
        }

        public Collection<Edge> getInputEdges(){
            return inputEdges.getEdges();
        }

        public Collection<Edge> getOutputEdges(){
            return outputEdges.getEdges();
        }
    }

    public static class Edge{
        private final Integer id;
        private Vertex start;
        private Vertex end;
        private Integer weight = 1;

        public Edge(Integer id, Vertex start, Vertex end){
            this.id = id;
            this.start = start;
            this.end = end;
        }

        public Vertex getStart(){
            return start;
        }

        public Vertex getEnd(){
            return end;
        }

        public Integer getWeight(){
            return this.weight;
        }

        public void setWeight(Integer weight){
            this.weight = weight;
        }
    }

    private static abstract class EdgeSet{
        private TreeMap<Double, Edge> edges = new TreeMap<>();

        public void addEdge(Edge e){
            Double angle = calcAngle(e);
            if(edges.containsKey(angle)){
                throw new RuntimeException("EdgeSet.addEdge(): Tried to insert edge with same angle");
            }
            edges.put(angle, e);
        }

        public Boolean isEmpty(){
            return edges.isEmpty();
        }

        public Collection<Edge> getEdges(){
            return edges.values();
        }

        protected abstract Double calcAngle(Edge e);
    }

    /* Assumed that start point of edges can be only under end point or with equal y coords but to the left */
    private static class InputEdgeSet extends EdgeSet{
        /*
            Returns angle between negative Ox and edge.
            returned angle is in interval [0, 180)
        */
        @Override
        protected Double calcAngle(Edge e){
            Double denom = calcLength(e);
            Double nom = calcDistance(e.getStart().getCoords(),
                    new Point(e.getStart().getCoords().getX(), e.getEnd().getCoords().getY()));

            Double angle = Math.asin(nom / denom);

            if(Double.compare(e.getStart().getCoords().getX(), e.getEnd().getCoords().getX()) > 0){
                angle = 180 - angle;
            }

            return angle;
        }
    }

    private static class OutputEdgeSet extends EdgeSet{
        /*
            Returns angle between positive Ox and edge.
            returned angle is in interval (0, 180]
        */
        @Override
        protected Double calcAngle(Edge e){
            Double denom = calcLength(e);
            Double nom = calcDistance(e.getStart().getCoords(),
                    new Point(e.getEnd().getCoords().getX(), e.getStart().getCoords().getY()));

            Double angle = Math.asin(nom / denom);

            if(Double.compare(e.getStart().getCoords().getX(), e.getEnd().getCoords().getX()) < 0){
                angle = 180 - angle;
            }

            return angle;
        }
    }
}
