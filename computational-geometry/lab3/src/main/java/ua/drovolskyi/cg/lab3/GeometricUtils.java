package ua.drovolskyi.cg.lab3;

import java.util.Comparator;

public class GeometricUtils {
    private static final Comparator<Point> pointComparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if(MathUtils.areEqual(p1.getX(), p2.getX())){
                if(MathUtils.areEqual(p1.getY(), p2.getY())){
                    return 0;
                }
                else if (p1.getY() < p2.getY()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else if(p1.getX() < p2.getX()){
                return -1;
            }
            else {
                return 1;
            }
        }
    };


    public static Comparator<Point> getPointComparator(){
        return pointComparator;
    }

    // returns t points where line segments sq and s2 intersect
    // returns null when segments do not intersect or when they fully or partially overlap
    public static Point getIntersectionPoint(LineSegment s1, LineSegment s2){
        Point startS1 = s1.getStart();
        Point endS1 = s1.getEnd();
        Point startS2 = s2.getStart();
        Point endS2 = s2.getEnd();

        if(isVertical(s1) && isVertical(s2)){
            if(endS2.equals(startS1)){
                return endS2;
            }
            if(endS1.equals(startS2)){
                return endS1;
            }
        }

        Double a1 = endS1.getX() - startS1.getX();
        Double b1 = endS2.getX() - startS2.getX();
        Double c1 = startS2.getX() - startS1.getX();

        Double a2 = endS1.getY() - startS1.getY();
        Double b2 = endS2.getY() - startS2.getY();
        Double c2 = startS2.getY() - startS1.getY();

        if(!MathUtils.areEqual(a2, 0.0) &&
            !MathUtils.areEqual((a1*b2)/a2 - b1, 0.0)){
            Double t = (c1 - (a1*c2)/a2)/((a1*b2)/a2 - b1);
            Double s = (b2*t + c2)/a2;

            if(MathUtils.areEqual(t, 0.0) || MathUtils.areEqual(t, 1.0)
                || (t > 0 && t < 1)){
                if(MathUtils.areEqual(s, 0.0) || MathUtils.areEqual(s, 1.0)
                        || (s > 0 && s < 1)){
                    return new Point(
                            startS1.getX() + s * (endS1.getX() - startS1.getX()),
                            startS1.getY() + s * (endS1.getY() - startS1.getY())
                    );
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    // x is coordinate of vertical line
    // if s is vertical segment, its start point returned
    // returns null when x-vertical line does to intersect given segment
    public static Point getIntersectionWithVerticalLine(LineSegment s, Double x){
        Double x1 = s.getStart().getX();
        Double x2 = s.getEnd().getX();
        Double y1 = s.getStart().getY();
        Double y2 = s.getEnd().getY();

        if(MathUtils.areEqual(x1, x) || MathUtils.areEqual(x2, x) ||
                (x1 < x && x < x2)){ // if x-vertical segment intersects segment s
            if(MathUtils.areEqual(x1, x2)){ // if s is vertical segment
                return s.getStart();
            }
            else{
                Double y = x * (y2 - y1)/(x2 - x1) + (x2*y1 - x1*y2)/(x2 - x1);
                return new Point(x, y);
            }
        }
        else{
            return null;
        }
    }

    // calculates point c that lies between a and b, and ac=bc
    public static Point calcMedianPoint(Point a, Point b){
        return new Point((a.getX() + b.getX())/2, (a.getY() + b.getY())/2);
    }

    public static Boolean isVertical(LineSegment s){
        return MathUtils.areEqual(s.getStart().getX(), s.getEnd().getX());
    }

    public static Double calcLength(LineSegment s){
        return calcDistance(s.getStart(), s.getEnd());
    }

    /**
     * Calculate distance between two points
     * @param start
     * @param end
     * @return distance between points start and end
     */
    public static Double calcDistance(Point start, Point end){
        return Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) +
                (end.getY() - start.getY()) * (end.getY() - start.getY()));

    }

    /**
     * Check if point c lies on segment ab
     * <p>SOURCE: https://stackoverflow.com/a/17693146<p/>
     * @param ab is segment
     * @param c is point that is need to be checked
     * @return true if point c lies on segment ab, false otherwise
     */
    public static Boolean isInSegment(LineSegment ab, Point c){
        Point a = ab.getStart();
        Point b = ab.getEnd();
        return MathUtils.areEqual(calcDistance(a, c) + calcDistance(c, b), calcDistance(a,b), 1e-6);
    }

    public static Boolean areOverlap(LineSegment s1, LineSegment s2){
        if(getIntersectionPoint(s1, s2) != null){
            return false;
        }

        if(isInSegment(s1, s2.getStart()) && isInSegment(s2, s1.getEnd())){
            return true;
        }
        if(isInSegment(s2, s1.getStart()) && isInSegment(s1, s2.getEnd())){
            return true;
        }
        if(isInSegment(s2, s1.getStart()) && isInSegment(s2, s1.getEnd())){
            return true;
        }
        if(isInSegment(s1, s2.getStart()) && isInSegment(s1, s2.getEnd())){
            return true;
        }

        return false;
    }

}
