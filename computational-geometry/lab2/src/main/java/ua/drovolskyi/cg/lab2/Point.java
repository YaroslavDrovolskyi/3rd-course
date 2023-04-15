package ua.drovolskyi.cg.lab2;

public class Point {
    private final Integer id;
    private final Double x;
    private final Double y;

    public Point(Integer id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Point(Double x, Double y){
        this(null, x, y);
    }

    public Integer getId(){
        return this.id;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Point)){
            return false;
        }
        Point p = (Point)obj;
        return(MathUtils.areEqual(getX(), p.getX()) &&
                MathUtils.areEqual(getY(), p.getY()));
    }

    @Override
    public String toString(){
        String coordinates = "(" + x + ", " + y + ")";
        if(id != null){
            return "{" + id + " " + coordinates + "}";
        }
        else{
            return coordinates;
        }
    }
}
