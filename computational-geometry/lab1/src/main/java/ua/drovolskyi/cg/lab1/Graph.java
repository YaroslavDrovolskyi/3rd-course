package ua.drovolskyi.cg.lab1;

import java.util.*;

public class Graph {
    private final AbstractMap<Integer, Vertex> vertices = new TreeMap<>();
    private final AbstractMap<Integer, Edge> edges = new TreeMap<>();

    public Graph(Vertex[] vertices, Edge[] edges){
        for(Vertex v : vertices){
            if(this.vertices.containsKey(v.getId())){
                throw new RuntimeException("Vertex with given ID is already exist");
            }
            this.vertices.put(v.getId(), v);
        }

        for(Edge e : edges){
            addEdgeImpl(e);
        }
    }


    public Graph(DoublyConnectedEdgeList list){
        List<DoublyConnectedEdgeList.Edge> rawEdges = list.getEdges();
        for(DoublyConnectedEdgeList.Edge rawEdge : rawEdges){
            DoublyConnectedEdgeList.Vertex rawStartVertex = rawEdge.getStart();
            DoublyConnectedEdgeList.Vertex rawEndVertex = rawEdge.getEnd();

            // insert vertices in array if they haven't been inserted before
            if(!vertices.containsKey(rawStartVertex.getId())) {
                vertices.put(rawStartVertex.getId(),
                        new Vertex(rawStartVertex.getId(), rawStartVertex.getCoords()));
            }
            if(!vertices.containsKey(rawEndVertex.getId())) {
                vertices.put(rawEndVertex.getId(),
                        new Vertex(rawEndVertex.getId(), rawEndVertex.getCoords()));
            }

            Vertex start = vertices.get(rawStartVertex.getId());
            Vertex end = vertices.get(rawEndVertex.getId());

            // initialize edge
            Edge edge = new Edge(rawEdge.getId(), start, end);

            addEdgeImpl(edge);
        }
    }

    public int numberOfVertices(){
        return vertices.size();
    }

    public void addEdge(Vertex start, Vertex end){
        int newEdgeId = Collections.max(edges.keySet()) + 1;
        Edge edge = new Edge(newEdgeId, start, end);

        addEdgeImpl(edge);
    }

    private void addEdgeImpl(Edge e){
        Vertex start = e.getStart();
        Vertex end = e.getEnd();

        // check if edge is exist
        if(edges.containsKey(e.getId())){
            throw new RuntimeException("Edge with given ID is already exist");
        }
        // check if start and end vertices exist
        if(!vertices.containsKey(start.getId())){
            throw new RuntimeException("Start vertex of this edge does not exist");
        }
        if(!vertices.containsKey(end.getId())){
            throw new RuntimeException("End vertex of this edge does not exist");
        }

        // insert new edge into set of edges
        edges.put(e.getId(), e);

        // insert new edge into sets OUT(start) and IN(end)
        start.addOutputEdge(e);
        end.addInputEdge(e);
    }

    /*
        Returns vertices in ascending order of their ID's
     */
    public Collection<Vertex> getVertices(){
        return vertices.values();
    }

    public Collection<Edge> getEdges(){
        return edges.values();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{\n");
        sb.append("Vertices:\n");
        for(Vertex v : getVertices()){
            sb.append(v);
            sb.append(" Input edges: ");
            sb.append(v.getInputEdges());
            sb.append(", ");
            sb.append(" Output edges: ");
            sb.append(v.getOutputEdges());
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Edges:\n");
        for(Edge e : getEdges()){
            sb.append(e);
            sb.append("\n");
        }
        sb.append("\n");


        sb.append("}");
        return sb.toString();
    }

    private static Double calcLength(Edge e){
        return GeometricUtils.calcDistance(e.start.coords, e.end.coords);
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

        public Integer getNumberOfInputEdges(){
            return inputEdges.size();
        }

        public Integer getNumberOfOutputEdges(){
            return outputEdges.size();
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

        public Edge getLeftInputEdge(){
            return inputEdges.getFirstEdge();
        }

        public Edge getLeftOutputEdge(){
            return outputEdges.getFirstEdge();
        }

        public int getInputEdgesWeight(){
            return inputEdges.getWeight();
        }

        public int getOutputEdgesWeight(){
            return outputEdges.getWeight();
        }

        @Override
        public String toString(){
            return "{" +
                    id +
                    ", " +
                    coords +
                    "}";
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

        public Integer getId(){
            return id;
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

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(id);
            sb.append(", ");
            sb.append(start.getId());
            sb.append(" -> ");
            sb.append(end.getId());
            sb.append(", ");
            sb.append(this.weight);
            sb.append("}");
            return sb.toString();
        }
    }

    private static abstract class EdgeSet{
        private final TreeMap<Double, Edge> edges = new TreeMap<>();

        public void addEdge(Edge e){
            Double angle = calcAngle(e);
            if(edges.containsKey(angle)){
                throw new RuntimeException("EdgeSet.addEdge(): Tried to insert edge with same angle");
            }
            edges.put(angle, e);
        }

        public Integer size(){
            return edges.size();
        }

        public Boolean isEmpty(){
            return edges.isEmpty();
        }

        public Collection<Edge> getEdges(){
            return edges.values();
        }

        public Edge getFirstEdge(){
            return edges.firstEntry().getValue();
        }

        public int getWeight(){
            int weight = 0;
            for(Edge e : edges.values()){
                weight += e.getWeight();
            }
            return weight;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            Collection<Edge> edgesList = getEdges();
            for(Edge e: edgesList){
                sb.append(e.toString());
                sb.append(", ");
            }
            if(edgesList.isEmpty()){
                sb.append("EMPTY");
            }
            else{
                sb.delete(sb.length()-2, sb.length()-1);
            }
            sb.append("}");

            return sb.toString();
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
            Double nom = GeometricUtils.calcDistance(e.getStart().getCoords(),
                    new Point(e.getStart().getCoords().getX(), e.getEnd().getCoords().getY()));

            Double angle = Math.toDegrees(Math.asin(nom / denom));

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
            Double nom = GeometricUtils.calcDistance(e.getEnd().getCoords(),
                    new Point(e.getEnd().getCoords().getX(), e.getStart().getCoords().getY()));

            Double angle = Math.toDegrees(Math.asin(nom / denom));

            if(Double.compare(e.getStart().getCoords().getX(), e.getEnd().getCoords().getX()) < 0){
                angle = 180 - angle;
            }

            return angle;
        }
    }
}
