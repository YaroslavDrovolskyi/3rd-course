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

    public static Point getIntersection(LineSegment s1, LineSegment s2){
        Point startS1 = s1.getStart();
        Point endS1 = s1.getEnd();
        Point startS2 = s2.getStart();
        Point endS2 = s2.getEnd();

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
}
