package ua.drovolskyi.cg.lab1;

public class MathUtils {
    public static boolean areEqual(double a, double b, double eps){
        return Math.abs(a-b) < eps;
    }
}
