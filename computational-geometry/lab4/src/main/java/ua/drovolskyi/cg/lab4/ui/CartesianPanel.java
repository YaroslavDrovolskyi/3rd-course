package ua.drovolskyi.cg.lab4.ui;

import ua.drovolskyi.cg.lab4.ConvexHull;
import ua.drovolskyi.cg.lab4.Point;

import javax.swing.*;
// import java.awt.;
import java.awt.*;
import java.util.List;
import java.util.LinkedList;

public class CartesianPanel extends JPanel {
    private final PixelPoint X_AXIS_START = new PixelPoint(50, 600);
    private final PixelPoint X_AXIS_END = new PixelPoint(600, 600);
    private final PixelPoint Y_AXIS_START = new PixelPoint(50, 600);
    private final PixelPoint Y_AXIS_END = new PixelPoint(50, 50);

    private final int X_AXIS_NUMBER_OF_COORDS = 20; // number of int coords on X axis
    private final int Y_AXIS_NUMBER_OF_COORDS = 20; // number of int coords on X axis

    //arrows of axis are represented with "hipotenuse" of
    //triangle
    // now we are define length of cathetas of that triangle
    private static final int FIRST_LENGHT = 10;
    private static final int SECOND_LENGHT = 5;

    // size of start coordinate lenght
    private static final int ORIGIN_COORDINATE_LENGHT = 6;

    // distance of coordinate strings from axis
    private static final int AXIS_STRING_DISTANCE = 20;

    private final int LENGTH; // is number of pixels between coordinates i and (i+1) on Cartesian plane

    // colors
    private final Color[] availableColors = new Color[] {
            Color.PINK, Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.BLUE
    };
    private final Color defaultColor = Color.BLACK;

    // data to draw
    private final List<Point> allPoints = new LinkedList<Point>();
    private final List<Point> convexHullPoints = new LinkedList<>();


    public CartesianPanel(){
        int xLength = Math.abs(X_AXIS_END.getX() - X_AXIS_START.getX()) / X_AXIS_NUMBER_OF_COORDS;
        int yLength = Math.abs(Y_AXIS_START.getY() - Y_AXIS_START.getX()) / Y_AXIS_NUMBER_OF_COORDS;
        LENGTH = Math.min(xLength, yLength);
    }

    public void drawConvexHull(ConvexHull u){
        allPoints.addAll(u.getAllPoints());
        convexHullPoints.addAll(u.getPoints());
        repaint();
    }

    public void clear(){
        allPoints.clear();
        convexHullPoints.clear();
        repaint();
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
        g2.setColor(defaultColor);

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

        // draw segments
        for(Point p : allPoints){
            drawPointImpl(p, g);
        }

        // draw convex hull
        g.setColor(Color.RED);
        for(int i = 0; i < convexHullPoints.size(); i++){
            drawPointImpl(convexHullPoints.get(i), g);

            if(i < convexHullPoints.size() - 1){
                drawLineSegmentImpl(convexHullPoints.get(i), convexHullPoints.get(i+1), g);
            }
        }
        drawLineSegmentImpl(convexHullPoints.get(convexHullPoints.size() - 1), convexHullPoints.get(0), g);
        g.setColor(defaultColor);
    }

    private void drawPointImpl(Point point, Graphics g) {
        final int pointDiameter = 5;
        PixelPoint p = toPixelPoint(point, pointDiameter);
        g.fillOval(p.getX(), p.getY(), pointDiameter, pointDiameter);

        // draw ID of point
        if(point.getId() != null){
            g.drawString(point.getId().toString(), p.getX()-5, p.getY()-3);
        }
    }

    private void drawLineSegmentImpl(Point start, Point end, Graphics g){
        PixelPoint startPixel = toPixelPoint(start, 0);
        PixelPoint endPixel = toPixelPoint(end, 0);
        g.drawLine(startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());

        // draw ID of line segment
//        if(id != null){
//            PixelPoint medianPoint = toPixelPoint(GeometricUtils.calcMedianPoint(start, end), 5);
//            g.drawString(id.toString(), medianPoint.getX()-5, medianPoint.getY()+5);
//        }
    }



    public static class PixelPoint {
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

}
