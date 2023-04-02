package ua.drovolskyi.cg.lab1;

import java.util.ArrayList;
import java.util.List;

public class FullSetOfChainsBuilder {
    private Graph.Vertex[] vertices;

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
        Graph.Edge leftOutputEdge = v.getLeftOutputEdge();

        // if it is last vertex, or no available edges to build chain
        if(leftOutputEdge == null){
            return;
        }

        // launch recursive adding new edge to chain
        chain.addEdge(leftOutputEdge);
        leftOutputEdge.setWeight(leftOutputEdge.getWeight() - 1);
        buildChainImpl(chain, leftOutputEdge.getEnd());
    }
}
