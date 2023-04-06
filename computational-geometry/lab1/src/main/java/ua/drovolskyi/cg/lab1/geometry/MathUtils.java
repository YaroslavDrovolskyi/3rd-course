package ua.drovolskyi.cg.lab1.geometry;

public class MathUtils {
    public static boolean areEqual(double a, double b, double eps){
        return Math.abs(a-b) < eps;
    }
    public static boolean areEqual(double a, double b){
        return areEqual(a,b,1e-6);
    }
}
