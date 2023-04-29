package ua.drovolskyi.cg.lab3.intersections_finder;

import ua.drovolskyi.cg.lab3.GeometricUtils;
import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.MathUtils;
import ua.drovolskyi.cg.lab3.Point;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class Status {

    // returns -1 when s1 is under the s2
    // returns 1 when s1 is above the s2
    // returns 0 when segments are equal or are overlapping
    private final Comparator<LineSegment> lineSegmentComparator = new Comparator<LineSegment>() {
        @Override
        public int compare(LineSegment s1, LineSegment s2) {
            if(s1.equals(s2)){
                return 0;
            }
            if(GeometricUtils.areOverlap(s1, s2)){
                return 0;
            }

            Point intersectionS1 = GeometricUtils.getIntersectionWithVerticalLine(s1, statusLineX);
            Point intersectionS2 = GeometricUtils.getIntersectionWithVerticalLine(s2, statusLineX);

            if(intersectionS1 == null || intersectionS2 == null){
                throw new RuntimeException("s1 and s2 are not comparable");
            }

            Double y11 = intersectionS1.getY();
            Double y21 = intersectionS2.getY();

            if(MathUtils.areEqual(y11, y21)){ // y11=y21=y means that s1 and s2 have common point (x, yCommon)
                Double yCommon = y11;
                Double y12 = s1.getEnd().getY();
                Double y22 = s2.getEnd().getY();

                if(MathUtils.areEqual(y12, y22)){
                    Double x12 = s1.getEnd().getX();
                    Double x22 = s2.getEnd().getX();

                    if(MathUtils.areEqual(x12, x22)){ // segments have common end
                        return 0;
                    }
                    else {
                        if(y12 >= yCommon){ // segments are upwards-directed after point (x,yCommon)
                            if(x12 <= x22){ // s1 is above the s2
                                return 1;
                            }
                            else{ // s1 is under the s2
                                return -1;
                            }
                        }
                        else{ // segments are downwards-directed after point (x,yCommon)
                            if(x12 <= x22){ // s1 is under the s2
                                return -1;
                            }
                            else{ // s1 is above the s2
                                return 1;
                            }
                        }
                    }
                }
                else if(y12 < y22){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else if(y11 < y21){
                return -1;
            }
            else{
                return 1;
            }
        }
    };

    private Double statusLineX;
    private final TreeSet<LineSegment> segments = new TreeSet<>(lineSegmentComparator);

    public void insert(LineSegment segment){
        segments.add(segment);
    }

    public void remove(LineSegment segment){
        segments.remove(segment);
    }

    public LineSegment above(LineSegment segment){
        return segments.ceiling(segment);
    }

    public LineSegment under(LineSegment segment){
        return segments.floor(segment);
    }

    public void setStatusLineX(Double statusLineX) {
        this.statusLineX = statusLineX;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Status {\n");
        sb.append("status line X = ");
        sb.append(statusLineX);
        sb.append("\n");

        if(!segments.isEmpty()){
            for(LineSegment s : segments){
                sb.append(s.toString());
                sb.append("\n");
            }
        }
        else{
            sb.append("[NO SEGMENTS]\n");
        }
        sb.append("}");

        return sb.toString();
    }

    // swap items = delete segments and insert them again
}
