package ua.drovolskyi.cg.lab1.ui;


import ua.drovolskyi.cg.lab1.Graph;
import ua.drovolskyi.cg.lab1.Graph.Edge;
import ua.drovolskyi.cg.lab1.Point;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cartesian {
    public static void main(String[] args) {
        CartesianFrame frame = new CartesianFrame();
        frame.showUI();

        frame.panel.drawPoint(new Point(1.5, 1.5));
        frame.panel.drawEdge(new Edge(1,
                new Graph.Vertex(1, new Point(1.0, 1.0)),
                new Graph.Vertex(2, new Point(1.0, 5.0))
                ));
    }
}

class CartesianFrame extends JFrame {
    CartesianPanel panel;

    public CartesianFrame() {
        panel = new CartesianPanel();
        add(panel);
    }

    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Cartesian");
        setSize(700, 700);
        setVisible(true);
    }
}

class CartesianPanel extends JPanel {
    public final PixelPoint X_AXIS_START = new PixelPoint(50, 600);
    public final PixelPoint X_AXIS_END = new PixelPoint(600, 600);
    public final PixelPoint Y_AXIS_START = new PixelPoint(50, 600);
    public final PixelPoint Y_AXIS_END = new PixelPoint(50, 50);

    public final int X_AXIS_NUMBER_OF_COORDS = 10; // number of int coords on X axis
    public final int Y_AXIS_NUMBER_OF_COORDS = 20; // number of int coords on X axis

    //arrows of axis are represented with "hipotenuse" of
    //triangle
    // now we are define length of cathetas of that triangle
    public static final int FIRST_LENGHT = 10;
    public static final int SECOND_LENGHT = 5;

    // size of start coordinate lenght
    public static final int ORIGIN_COORDINATE_LENGHT = 6;

    // distance of coordinate strings from axis
    public static final int AXIS_STRING_DISTANCE = 20;

    private final int LENGTH; // is number of pixels between coordinates i and (i+1) on Cartesian plane

    private List<Point> points = new ArrayList<>();
    private List<Graph.Edge> edges = new ArrayList<>();

    public CartesianPanel(){
        int xLength = Math.abs(X_AXIS_END.getX() - X_AXIS_START.getX()) / X_AXIS_NUMBER_OF_COORDS;
        int yLength = Math.abs(Y_AXIS_START.getY() - Y_AXIS_START.getX()) / Y_AXIS_NUMBER_OF_COORDS;
        LENGTH = Math.min(xLength, yLength);
    }
    public void drawPoint(Point point) {
        points.add(point);
        repaint();
    }

    public void drawEdge(Edge edge) {
        edges.add(edge);
        repaint();
    }

    private void drawPointImpl(Point point, Graphics g) {
        final int pointDiameter = 5;
        PixelPoint p = toPixelPoint(point, pointDiameter);
        g.fillOval(p.getX(), p.getY(), pointDiameter, pointDiameter);
    }

    private void drawEdgeImpl(Edge edge, Graphics g) {
        PixelPoint start = toPixelPoint(edge.getStart().getCoords(), 0);
        PixelPoint end = toPixelPoint(edge.getEnd().getCoords(), 0);
        g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    private PixelPoint toPixelPoint(Point p, int pointDiameter){
        final int x = (int)Math.round(X_AXIS_START.getX() + (p.getX() * LENGTH) - (double)pointDiameter / 2);
        final int y = (int)Math.round(Y_AXIS_START.getY() - (p.getY() * LENGTH) - (double)pointDiameter / 2);

        return new PixelPoint(x, y);
    }



    /**
     Method to (re)paint all that panel contains
     * */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // draw X axis
        g2.drawLine(X_AXIS_START.getX(), X_AXIS_START.getY(),
                X_AXIS_END.getX(), X_AXIS_END.getY());
        // draw Y axis
        g2.drawLine(Y_AXIS_START.getX(), Y_AXIS_START.getY(),
                Y_AXIS_END.getX(), Y_AXIS_END.getY());

        // draw origin (0, 0) point
        g2.fillOval(
                X_AXIS_START.getX() - (ORIGIN_COORDINATE_LENGHT / 2),
                X_AXIS_START.getY() - (ORIGIN_COORDINATE_LENGHT / 2),
                ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT);


        // draw numbers and numbers notch's on X axis
        for(int i = 1; i < X_AXIS_NUMBER_OF_COORDS; i++) {
            g2.drawLine(X_AXIS_START.getX() + (i * LENGTH),
                    X_AXIS_START.getY() - SECOND_LENGHT,
                    X_AXIS_START.getX() + (i * LENGTH),
                    X_AXIS_START.getY() + SECOND_LENGHT);
            g2.drawString(Integer.toString(i),
                    X_AXIS_START.getX() + (i * LENGTH), ///// -3
                    X_AXIS_START.getY() + AXIS_STRING_DISTANCE);
        }

        // draw numbers and numbers notch's on Y axis
        for(int i = 1; i < Y_AXIS_NUMBER_OF_COORDS; i++) {
            g2.drawLine(Y_AXIS_START.getX() - SECOND_LENGHT,
                    Y_AXIS_START.getY() - (i * LENGTH),
                    X_AXIS_START.getX() + SECOND_LENGHT,
                    Y_AXIS_START.getY() - (i * LENGTH));
            g2.drawString(Integer.toString(i),
                    X_AXIS_START.getX() - AXIS_STRING_DISTANCE,
                    X_AXIS_START.getY() - (i * LENGTH));
        }

        // draw points
        for(Point p : points){
            drawPointImpl(p, g);
        }

        for(Edge e : edges){
            drawEdgeImpl(e, g);
        }
    }
}


class PixelPoint {
    public Integer x;
    public Integer y;

    public PixelPoint(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }
}


/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java
 */
