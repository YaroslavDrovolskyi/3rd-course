package ua.drovolskyi.cg.lab3;

public class Main {
    public static void main(String[] args) {
        LineSegment s1 = new LineSegment(new Point(1.0,1.0), new Point(5.0,5.0));
        LineSegment s2 = new LineSegment(new Point(1.0,5.0), new Point(10.0,1.0));

        System.out.println( GeometricUtils.getIntersection(s1, s2));

    }
}