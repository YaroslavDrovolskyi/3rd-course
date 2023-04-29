package ua.drovolskyi.cg.lab3.intersections_finder.result;

import ua.drovolskyi.cg.lab3.LineSegment;

import javax.sound.sampled.Line;
import java.util.Arrays;
import java.util.List;

// class for intersection that is segments overlapping
public class OverlappingIntersection extends Intersection{
    private final LineSegment segment1; // first overlapping segment
    private final LineSegment segment2; // second overlapping segment

    public OverlappingIntersection(LineSegment segment1, LineSegment segment2){
        this.segment1 = segment1;
        this.segment2 = segment2;
    }

    @Override
    public List<LineSegment> getLineSegments() {
        return Arrays.asList(segment1, segment2);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof OverlappingIntersection intersection)){
            return false;
        }
        return (this.segment1.equals(intersection.segment1) && this.segment2.equals(intersection.segment2)) ||
                (this.segment1.equals(intersection.segment2) && this.segment2.equals(intersection.segment1));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("OverlappingIntersection {\n");
        sb.append(segment1);
        sb.append("\n");
        sb.append(segment2);
        sb.append("\n");
        sb.append("}");

        return sb.toString();
    }
}
