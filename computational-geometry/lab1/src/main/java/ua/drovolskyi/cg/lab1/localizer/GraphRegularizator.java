package ua.drovolskyi.cg.lab1.localizer;

import java.util.*;

import ua.drovolskyi.cg.lab1.geometry.GeometricUtils;
import ua.drovolskyi.cg.lab1.geometry.Point;
import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.graph.Graph.Vertex;
import ua.drovolskyi.cg.lab1.graph.Graph.Edge;


/*
Знизу вверх (регуляризовуємо вершини, в яких немає вихідних)
    - Якщо вершина v регулярна, то: прибираємо зі списку прямих прямі з OUT(v) (ці прямі в списку завжди сусідні)
    і додаємо прямі з IN(v) (вони також будуть сусідні). Якщо додані прямі є, то між ними і зліва і права від них
    ставимо поточну вершину v. Якщо доданих прямих немає, то ті вершини, які були зліва і справа від прибраних
    також замінюємо на v.
    - Якщо вершина нерегулярна, то локалізовуємо вершину (знаходимо дві прямі. між якими вона лежить),
    з'єднуємо з вершиною в тому інтервалі. Далі
        - якщо в цій вершині немає вхідних, то в статусі між тими прямими де v, поставити v
        - якщо в цій вершині є вхідні, то між прямими, між якими знаходиться v, додати ці ребра, здіва і між ними поставити v

Якщо вершина нерегулярна, а статус пустий, то це означає, що під попередньою вершиною
 немає жодних прямих і тому  проводимо ребро до попередньої вершини

 */

public class GraphRegularizator {
//    private Status status;

    // regularize graph
    public static void regularize(Graph graph){
        Status status = new Status();
        // when top-down remove from status all edges that start in current vertex
        // localize it and add all edges
        // if edges was the last in status and no edges is in IN(v) than we should store this vertex v;
        Vertex[] vertices = graph.getVertices().toArray(new Vertex[0]);

        System.out.println("REGULARIZATION FROM TOP TO DOWN");
        // from top to down
        for(int i = vertices.length - 1; i >= 0; i--){
            Vertex v = vertices[i];

            System.out.println("VERTEX " + v.getId());
            System.out.println("Before: " + status);

            // remove OUT(v) edges from status
            if(v.getNumberOfOutputEdges() > 0){
                status.removeEdges(v, v.getOutputEdges().toArray(new Edge[0]));
            }

            // localize
            int leftNodeIndex = status.localize(v);

            // add new edge (if OUT(v) is empty and v is not last vertex)
            if(v.getNumberOfOutputEdges() == 0 && i != vertices.length - 1){
                Vertex newEdgeEnd = status.getNearestVertex(leftNodeIndex);
                graph.addEdge(v, newEdgeEnd);
                System.out.println("Added edge: " + v.getId() + " -> " + newEdgeEnd.getId());
            }

            // add IN(v) nodes to status
            // if IN(v) is empty then this function set v as the nearest in its interval
            // case where leftNodeIndex = -2 is handled by choosing max(.., 0)
            status.addEdges(v, v.getInputEdges().toArray(new Edge[0]), Math.max(leftNodeIndex + 1, 0));

            System.out.println("After: " + status + "\n");
        }

        status.reset();
        System.out.println("\nREGULARIZATION FROM DOWN TO TOP");

        // from down to top
        for(int i = 0; i < vertices.length; i++){
            Vertex v = vertices[i];

            System.out.println("VERTEX " + v.getId());
            System.out.println("Before: " + status);

            // remove IN(v) edges from status
            if(v.getNumberOfInputEdges() > 0){
                status.removeEdges(v, v.getInputEdges().toArray(new Edge[0]));
            }

            // localize
            int leftNodeIndex = status.localize(v);

            // add new edge (if IN(v) is empty and v is not first vertex)
            if(v.getNumberOfInputEdges() == 0 && i != 0){
                Vertex newEdgeStart = status.getNearestVertex(leftNodeIndex);
                graph.addEdge(newEdgeStart, v);
                System.out.println("Added edge: " + newEdgeStart.getId() + " -> " + v.getId());
            }

            // add OUT(v) nodes to status
            // if OUT(v) is empty then this function set v as the nearest in its interval
            // case where leftNodeIndex = -2 is handled by choosing max(.., 0)
            status.addEdges(v, v.getOutputEdges().toArray(new Edge[0]), Math.max(leftNodeIndex + 1, 0));

            System.out.println("After: " + status + "\n");
        }



        // remove edges from OUT(v)
        // localize
        // if vertex is have not OUT(v) add new edge
        // add edges from IN(v)
    }
//
  //  private void deleteAllOutputEdges(List<StateNode> state, );


    private static class Status{
        private final List<Node> nodes = new ArrayList<>();
        private Vertex lastVertex = null;

        public void reset(){
            lastVertex = null;
            nodes.clear();
        }

        // return index of edge, that is on left from the point
        // if returned value is -1 point is on the left of the leftmost edge
        // if point is on one of edges, RuntimeException is thrown
        // -2 means that status is empty and no edges to localize
        public int localize(Vertex v){
            if(nodes.isEmpty()){
                return -2;
            }

            Point p = v.getCoords();

            // handle cases where point is on left/right from all edges
            if(relativePosition(nodes.get(0).edge, p) < 0){
                return -1;
            }
            else if (relativePosition(nodes.get(nodes.size() - 1).edge, p) > 0){
                return nodes.size() - 1;
            }

            // binary search
            int low = 0;
            int high = nodes.size() - 1;
            int mid = 0;
            while(high > low + 1){
                mid = (low + high) / 2;
                Integer side = relativePosition(nodes.get(mid).edge, p);
                if (side < 0){ // point is on left of mid-edge
                    high = mid;
                }
                else if (side > 0){ // point is on right to mid-edge
                    low = mid;
                }
                else{ // we can throw exception, because it is guaranteed that all OUT(v) are removed from status
                    throw new RuntimeException("Vertex can't be on edge");
                }
            }

            if(relativePosition(nodes.get(low).edge, p) > 0 &&
                    relativePosition(nodes.get(high).edge, p) < 0){
                return low;
            }
            else{
                throw new RuntimeException("Vertex can't be on edge");
            }
        }

        public Vertex getNearestVertex(int leftEdgeIndex){
            if(leftEdgeIndex == -2){
                return lastVertex;
            }
            else if(leftEdgeIndex == -1){
                return nodes.get(0).leftVertex;
            }
            else{
                return nodes.get(leftEdgeIndex).rightVertex;
            }
        }

        // wrapper for GeometricUtils.relativePosition()
        public Integer relativePosition(Edge e, Point p){
            return GeometricUtils.relativePosition(e.getStart().getCoords(), e.getEnd().getCoords(), p);
        }

        // inserts nodes with given edges into position pos
        public void addEdges(Vertex currentVertex, Edge[] newEdges, int pos){
            // create new nodes from edges
            Node[] newNodes = new Node[newEdges.length];
            for(int i = 0; i < newEdges.length; i++){
                newNodes[i] = new Node(newEdges[i], currentVertex, currentVertex);
            }

            // insert new nodes
            nodes.addAll(pos, Arrays.asList(newNodes));

            // set the nearest vertices at left and right side of inserted sequence of nodes (edges)
            if(pos > 0){
                nodes.get(pos - 1).rightVertex = currentVertex;
            }
            if(pos + newNodes.length < nodes.size()){
                nodes.get(pos + newNodes.length).leftVertex = currentVertex;
            }
            lastVertex = currentVertex;
        }

        // edges are edges in correct order, so they will always be placed together in status array
        // edgesToRemove must be non-empty
        public void removeEdges(Vertex currentVertex, Edge[] edgesToRemove){
            lastVertex = currentVertex;
            if(nodes.isEmpty()){
                return;
            }

            // remove target nodes from list
            Integer startIndex = getIndexOfNodeWithEdge(edgesToRemove[0]);
            List<Node> subList = nodes.subList(startIndex, startIndex + edgesToRemove.length);
            subList.clear();

            // change the nearest points between left node and right node of removed sequence of edges
            if(startIndex > 0){
                nodes.get(startIndex - 1).rightVertex = currentVertex;
            }
            if(startIndex < nodes.size()){
                nodes.get(startIndex).leftVertex = currentVertex;
            }
        }

        private Integer getIndexOfNodeWithEdge(Edge e){
            ListIterator<Node> it = nodes.listIterator();
            while(it.hasNext()){
                Node node = it.next();
                if(e.equals(node.edge)){
                    return it.previousIndex();
                }
            }

            return -1;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();

            sb.append("{Last vertex: ");
            if(lastVertex != null){
                sb.append(lastVertex.getId());
            }
            else{
                sb.append("NULL");
            }
            sb.append(" | ");


            if(nodes.isEmpty()){
                sb.append("[STATUS IS EMPTY]");
            }
            else{
                ListIterator<Node> it = nodes.listIterator();
                while(it.hasNext()){
                    Node node = it.next();

                    // check if the nearest vertex in interval the same left edge and right edge
                    it.previous();
                    if(it.hasPrevious()) {
                        Node prevNode = it.previous();
                        if (!prevNode.rightVertex.getId().equals(node.leftVertex.getId())) {
                            throw new RuntimeException("Nearest vertex in one interval is not the same for left and right edges");
                        }
                        it.next();
                    }
                    node = it.next();

                    sb.append(node.leftVertex.getId());
                    sb.append(" <-[" + node.edge.getId() + "]-> ");
                    if(!it.hasNext()){
                        sb.append(node.rightVertex.getId());
                    }
                }
            }

            sb.append("}");

            return sb.toString();
        }

        private static class Node{
            private Edge edge;
            private Vertex leftVertex;
            private Vertex rightVertex;

            public Node(Edge edge, Vertex leftVertex, Vertex rightVertex){
                this.edge = edge;
                this.leftVertex = leftVertex;
                this.rightVertex = rightVertex;
            }
        }
    }

}
