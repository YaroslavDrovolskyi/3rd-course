package ua.drovolskyi.cg.lab1.geometry;

public class Point {
    private Double x;
    private Double y;

    public Point(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public Double getX(){
        return x;
    }

    public Double getY(){
        return y;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
