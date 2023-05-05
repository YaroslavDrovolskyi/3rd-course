package ua.drovolskyi.cg.lab4;

public class Point {
    private Integer id;
    private Double x;
    private Double y;

    public Point(Integer id, Double x, Double y){
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

    public Double getX(){
        return x;
    }

    public Double getY(){
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Point p)){
            return false;
        }
        return(MathUtils.areEqual(getX(), p.getX()) &&
                MathUtils.areEqual(getY(), p.getY()));
    }


    @Override
    public String toString(){
        String coordsStr = "(" + x + ", " + y + ")";

        if(id != null){
            return "Point{" + id + ", " + coordsStr + "}";
        }
        else{
            return coordsStr;
        }
    }
}
