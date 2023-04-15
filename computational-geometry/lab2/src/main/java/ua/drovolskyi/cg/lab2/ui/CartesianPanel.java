package ua.drovolskyi.cg.lab2.ui;

import javax.swing.*;
import java.awt.*;

import ua.drovolskyi.cg.lab2.CuttingLine;
import ua.drovolskyi.cg.lab2.Rectangle;

import java.util.LinkedList;
import java.util.List;
import ua.drovolskyi.cg.lab2.Point;
import ua.drovolskyi.cg.lab2.TwoDTree;

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
    private Color[] availableColors = new Color[] {
            Color.PINK, Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.BLUE
    };
    private Color defaultColor = Color.BLACK;

    // data to draw
    private final List<Point> points = new LinkedList<>();
    private final List<Point> specialPoints = new LinkedList<>();
    private final List<Rectangle> rectangles = new LinkedList<>();
    private final List<TwoDTree> trees = new LinkedList<>();


    public CartesianPanel(){
        int xLength = Math.abs(X_AXIS_END.getX() - X_AXIS_START.getX()) / X_AXIS_NUMBER_OF_COORDS;
        int yLength = Math.abs(Y_AXIS_START.getY() - Y_AXIS_START.getX()) / Y_AXIS_NUMBER_OF_COORDS;
        LENGTH = Math.min(xLength, yLength);
    }


    public void drawPoints(List<Point> pointsList) {
        this.points.addAll(pointsList);
        repaint();
    }

    public void drawSpecialPoints(List<Point> pointsList){
        specialPoints.addAll(pointsList);
        repaint();
    }

    public void drawRectangle(Rectangle rect){
        rectangles.add(rect);
        repaint();
    }

    public void drawTwoDTree(TwoDTree tree){
        trees.add(tree);
        repaint();
    }

    public void clear(){
        points.clear();
        specialPoints.clear();
        rectangles.clear();
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

        // draw points
        for(Point p : points){
            drawPointImpl(p, g);
        }

        // draw trees
//        for(TwoDTree t : trees){
//            drawTwoDTreeImpl(t.getRoot(), null, null, g);
//        }

        // draw rectangles
        g.setColor(Color.BLUE);
        for(Rectangle rect : rectangles){
            drawRectangleImpl(rect, g);
        }
        g.setColor(defaultColor);

        // draw special points
        g.setColor(Color.RED);
        for(Point p : specialPoints){
            drawPointImpl(p, g);
        }
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

    private void drawTwoDTreeImpl(TwoDTree.Node node, TwoDTree.Node parent, Boolean isLeftChild, Graphics g){
        if(node == null){
            return;
        }

        // draw cutting line
        g.setColor(Color.ORANGE);
        if(parent != null){
            drawCuttingLineImpl(node.getCuttingLine(), parent.getCuttingLine(), isLeftChild, g);
        }
        else{
            drawCuttingLineImpl(node.getCuttingLine(), null, null, g);
        }
        g.setColor(defaultColor);

        // draw points on current cutting line
        for(Point p : node.getPointsOnLine()){
            drawPointImpl(p, g);
        }

        // draw subtrees
        drawTwoDTreeImpl(node.getLeft(), node, true, g);
        drawTwoDTreeImpl(node.getRight(), node, false, g);
    }

    private void drawRectangleImpl(Rectangle rect, Graphics g) {
        drawLineSegmentImpl(rect.getBottomLeftPoint(), rect.getTopLeftPoint(), g);
        drawLineSegmentImpl(rect.getTopLeftPoint(), rect.getTopRightPoint(), g);
        drawLineSegmentImpl(rect.getTopRightPoint(), rect.getBottomRightPoint(), g);
        drawLineSegmentImpl(rect.getBottomRightPoint(), rect.getBottomLeftPoint(), g);
    }

    private void drawLineSegmentImpl(Point start, Point end, Graphics g){
        PixelPoint startPixel = toPixelPoint(start, 0);
        PixelPoint endPixel = toPixelPoint(end, 0);
        g.drawLine(startPixel.getX(), startPixel.getY(), endPixel.getX(), endPixel.getY());
    }

    private void drawCuttingLineImpl(CuttingLine line, CuttingLine parentLine, Boolean isLeftChild, Graphics g){
        Point start = null;
        Point end = null;
        if(line.getType() == CuttingLine.Type.VERTICAL){
            start = new Point(line.getCoordinate(), 0.0);
            end = new Point(line.getCoordinate(), Double.valueOf(Y_AXIS_NUMBER_OF_COORDS));
            if(parentLine != null){
                if(isLeftChild){ // lower to parent line
                    end = new Point(line.getCoordinate(), parentLine.getCoordinate());
                }
                else{ // upper to parent line
                    start = new Point(line.getCoordinate(), parentLine.getCoordinate());
                }
            }
        }
        else{ // line is horizontal
            start = new Point(0.0, line.getCoordinate());
            end = new Point(Double.valueOf(X_AXIS_NUMBER_OF_COORDS), line.getCoordinate());
            if(parentLine != null){
                if(isLeftChild){ // left to parent line
                    end = new Point(parentLine.getCoordinate(), line.getCoordinate());
                }
                else{ // right to parent line
                    start = new Point(parentLine.getCoordinate(), line.getCoordinate());
                }
            }
        }

        drawLineSegmentImpl(start, end, g);
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


/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java
 */