package ua.drovolskyi.cg.lab1.ui;



import ua.drovolskyi.cg.lab1.localizer.Chain;
import ua.drovolskyi.cg.lab1.graph.Graph;
import ua.drovolskyi.cg.lab1.geometry.Point;

import javax.swing.JFrame;

public class CartesianFrame extends JFrame {
    private CartesianPanel panel;

    public CartesianFrame() {
        panel = new CartesianPanel();
        add(panel);
    }

    public void showUI(String title) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title);
        setSize(700, 700);
        setVisible(true);
    }

    public CartesianPanel getPanel(){
        return panel;
    }

    public static void displayGraph(Graph graph, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawGraph(graph);
        frame.showUI(title);
    }

    public static void displayChainsAndPoint(Chain[] chains, Point[] points, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawChains(chains);
        for(Point p : points){
            frame.getPanel().drawSpecialPoint(p);
        }
        frame.showUI(title);
    }
}

/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java
 */
