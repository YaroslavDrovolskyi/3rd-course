package ua.drovolskyi.cg.lab1.localizer;

import ua.drovolskyi.cg.lab1.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class FullSetOfChainsBuilder {
    private Graph.Vertex[] vertices;

    /**
     * Builds full set of chains for planar REGULAR graph
     * @param graph is REGULAR graph
     * @return full set of chains as list of chains sorted from leftmost chain to rightmost chain
     */
    public List<Chain> build(Graph graph){
        this.vertices = graph.getVertices().toArray(new Graph.Vertex[0]);

        List<Chain> chains = new ArrayList<>();
        while(true){
            Chain newChain = buildChain();
            if(newChain != null){
                chains.add(newChain);
            }
            else{
                return chains;
            }
        }
    }

    private Chain buildChain(){
        Chain chain = new Chain();
        buildChainImpl(chain, vertices[0]);

        if(!chain.isEmpty()){
            return chain;
        }
        return null;
    }

    private void buildChainImpl(Chain chain, Graph.Vertex v){
        if(isLastVertex(v)){ // if it is last vertex
            return;
        }

        Graph.Edge leftOutputEdge = v.getLeftOutputWeightEdge();

        // if no available edges to build chain
        if(leftOutputEdge == null){
            return;
        }

        // launch recursive adding new edge to chain
        chain.addEdge(leftOutputEdge);
        leftOutputEdge.setWeight(leftOutputEdge.getWeight() - 1);
        buildChainImpl(chain, leftOutputEdge.getEnd());
    }

    private boolean isLastVertex(Graph.Vertex v){
        return v.getId().equals(vertices[vertices.length - 1].getId());
    }
}
