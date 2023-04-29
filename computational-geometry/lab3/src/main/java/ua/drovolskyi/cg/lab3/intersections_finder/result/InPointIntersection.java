package ua.drovolskyi.cg.lab3.intersections_finder.result;

import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.MathUtils;
import ua.drovolskyi.cg.lab3.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InPointIntersection extends Intersection{
    private final Point point;
    private final Set<LineSegment> segments = new HashSet<>(); // segments that intersect in this point

    public InPointIntersection(Point point, LineSegment s1, LineSegment s2){
        this.point = point;
        addSegments(Arrays.asList(s1, s2));
    }

    public void addSegments(List<LineSegment> newSegments){
        segments.addAll(newSegments);
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public List<LineSegment> getLineSegments() {
        return segments.stream().toList();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof InPointIntersection intersection)){
            return false;
        }
        return this.getPoint().equals(intersection.getPoint());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("InPointIntersection {\n");
        sb.append("point: ");
        sb.append(point);
        sb.append("\n");

        if(!segments.isEmpty()){
            for(LineSegment s : segments){
                sb.append(s.toString());
                sb.append("\n");
            }
        }
        else{
            sb.append("[NO SEGMENTS]\n");
            throw new RuntimeException("No segments in InPointIntersection");
        }

        sb.append("}");

        return sb.toString();
    }
}
