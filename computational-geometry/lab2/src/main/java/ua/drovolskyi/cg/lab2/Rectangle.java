package ua.drovolskyi.cg.lab2;

public class Rectangle {
    private final Double startX;
    private final Double endX;
    private final Double startY;
    private final Double endY;

    public Rectangle(Double startX, Double endX, Double startY, Double endY) {
        if(MathUtils.areEqual(startX, endX) || MathUtils.areEqual(startY, endY)){
            throw new IllegalArgumentException("Following condition is violated:" +
                    " (startX != endX) and (startY != endY)");
        }
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public Double getStartX() {
        return startX;
    }

    public Double getEndX() {
        return endX;
    }

    public Double getStartY() {
        return startY;
    }

    public Double getEndY() {
        return endY;
    }

    public Point getBottomLeftPoint(){
        return new Point(startX, startY);
    }

    public Point getTopLeftPoint(){
        return new Point(startX, endY);
    }

    public Point getTopRightPoint(){
        return new Point(endX, endY);
    }

    public Point getBottomRightPoint(){
        return new Point(endX, startY);
    }

    @Override
    public String toString(){
       return "{Rectangle [" + startX + ", " + endX + "] x [" +
                startY + ", " + endY + "]}";
    }
}
