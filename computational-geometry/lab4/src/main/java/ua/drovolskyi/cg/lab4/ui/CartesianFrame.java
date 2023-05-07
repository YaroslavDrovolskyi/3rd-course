package ua.drovolskyi.cg.lab4.ui;

import ua.drovolskyi.cg.lab4.ConvexHull;
import ua.drovolskyi.cg.lab4.OneSideConvexHullTreeVisualizer;

import javax.swing.*;
import java.io.IOException;

public class CartesianFrame extends JFrame {
    private final CartesianPanel panel;

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

    public static void displayConvexHull(ConvexHull u, String title) throws IOException {
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawConvexHull(u);

        // display top and bottom CH-trees
        OneSideConvexHullTreeVisualizer.visualize(u.getTopConvexHull(), "topCH " + title + ".png",
                "Top tree - " + title);
        displayImage("topCH " + title + ".png", "Top convex hull");

        OneSideConvexHullTreeVisualizer.visualize(u.getBottomConvexHull(), "bottomCH " + title + ".png",
                "Bottom tree - " + title);
        displayImage("bottomCH " + title + ".png", "Bottom convex hull");

        frame.showUI(title);
    }


    public static void displayImage(String filename, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.add(new JLabel(new ImageIcon(filename)));

        frame.showUI(title);
        frame.setSize(1400, 800);
    }
}