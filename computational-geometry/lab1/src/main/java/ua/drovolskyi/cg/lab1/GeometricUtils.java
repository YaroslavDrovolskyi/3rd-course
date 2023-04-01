package ua.drovolskyi.cg.lab1;

public class GeometricUtils {

    // determines where point p is located relative to vector start -> end
    // 0 means point line of vector, -1 on the left side, 1 on the right side
    // source: https://math.stackexchange.com/a/274728
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
}
