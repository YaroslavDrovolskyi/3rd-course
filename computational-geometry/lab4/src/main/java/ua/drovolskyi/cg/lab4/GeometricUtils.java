package ua.drovolskyi.cg.lab4;

public class GeometricUtils {
    /**
     * Determines where point p is located relative to vector start -> end
     * <p>SOURCE: https://math.stackexchange.com/a/274728</p>
     * @param start start point of vector
     * @param end end point of vector
     * @param p
     * @return 0 when p is on line of vector, -1 when p is on the left side,
     * and 1 when p is on the right side
     */
    public static Integer relativePosition(Point start, Point end, Point p){
        double d = (p.getX() - start.getX()) * (end.getY() - start.getY()) -
                (p.getY() - start.getY()) * (end.getX() - start.getX());

        if(d < 0){
            return -1;
        }
        else if (d > 0){
            return 1;
        }
        else{
            return 0;
        }
    }


    // p1, p2, p3, p4 no pair from them can't be equal
    // p1, p2 - fist line
    // p3, p4 - second line
    // return null if lines are parallel or overlapping, intersection point otherwise
    public static Point findIntersectionOfLines(Point p1, Point p2, Point p3, Point p4){
        Double denominator = (p1.getX() - p2.getX()) * (p3.getY() - p4.getY()) -
                (p1.getY() - p2.getY()) * (p3.getX() - p4.getX());
        if(MathUtils.areEqual(denominator, 0.0)){
            return null;
        }

        Double nominatorX = (p1.getX()*p2.getY() - p1.getY()*p2.getX()) * (p3.getX() - p4.getX()) -
                (p1.getX() - p2.getX()) * (p3.getX()*p4.getY() - p3.getY()*p4.getX());

        Double nominatorY = (p1.getX()*p2.getY() - p1.getY()*p2.getX()) * (p3.getY() - p4.getY()) -
                (p1.getY() - p2.getY()) * (p3.getX()*p4.getY() - p3.getY()*p4.getX());

        return new Point(nominatorX/denominator, nominatorY/denominator);
    }
}
