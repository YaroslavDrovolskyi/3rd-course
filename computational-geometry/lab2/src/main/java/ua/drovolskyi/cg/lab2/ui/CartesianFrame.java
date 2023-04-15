package ua.drovolskyi.cg.lab2.ui;

import ua.drovolskyi.cg.lab2.Point;
import ua.drovolskyi.cg.lab2.Rectangle;
import ua.drovolskyi.cg.lab2.TwoDTree;

import javax.swing.*;
import java.util.List;

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

    public static void displayPoints(List<Point> points, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawPoints(points);
        frame.showUI(title);
    }

    public static void displayResultOfRegionalSearch(List<Point> inputPoints, Rectangle rect,
                                                     List<Point> result, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.getPanel().drawPoints(inputPoints);
        frame.getPanel().drawRectangle(rect);
        frame.getPanel().drawSpecialPoints(result);
        frame.showUI(title);
    }

    public static void displayTwoDTreeFromImage(String filename, String title){
        CartesianFrame frame = new CartesianFrame();
        frame.add(new JLabel(new ImageIcon(filename)));

        frame.showUI(title);
        frame.setSize(1000, 900);
    }
}

/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java


    How to display image from file in JFrame: https://stackoverflow.com/questions/20098124/displaying-an-image-in-a-jframe
 */
