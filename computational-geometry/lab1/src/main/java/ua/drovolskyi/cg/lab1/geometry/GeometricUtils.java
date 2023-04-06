package ua.drovolskyi.cg.lab1.geometry;

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

    /**
     * Calculate distance between two points
     * @param start
     * @param end
     * @return distance between points start and end
     */
    public static double calcDistance(Point start, Point end){
        return Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) +
                (end.getY() - start.getY()) * (end.getY() - start.getY()));

    }

    /**
     * Check if point c lies on segment ab
     * <p>SOURCE: https://stackoverflow.com/a/17693146<p/>
     * @param a
     * @param b
     * @param c
     * @return true if point c lies on segment ab, false otherwise
     */
    public static boolean isInSegment(Point a, Point b, Point c){
        return MathUtils.areEqual(calcDistance(a, c) + calcDistance(c, b), calcDistance(a,b), 1e-6);
    }
}
