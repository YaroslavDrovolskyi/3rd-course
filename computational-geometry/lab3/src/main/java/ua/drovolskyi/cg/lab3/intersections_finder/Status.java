package ua.drovolskyi.cg.lab3.intersections_finder;

import ua.drovolskyi.cg.lab3.GeometricUtils;
import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.MathUtils;
import ua.drovolskyi.cg.lab3.Point;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Status {

    // returns -1 when s1 is under the s2
    // returns 1 when s1 is above the s2
    // returns 0 when segments are equal or are overlapping
    private final Comparator<LineSegment> notVerticalLineSegmentComparator = new Comparator<LineSegment>() {
        @Override
        public int compare(LineSegment s1, LineSegment s2) {
            if(s1.equals(s2)){
                return 0;
            }
//            if(GeometricUtils.areOverlap(s1, s2)){
//                return 0;
//            }

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
                        return Integer.compare(s1.getId(), s2.getId());
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
    private final Comparator<LineSegment> verticalLineSegmentComparator = new Comparator<LineSegment>() {
        @Override
        public int compare(LineSegment s1, LineSegment s2) {
            if(s1.equals(s2)){
                return 0;
            }
            if(s1.getStart().equals(s2.getStart())){
                return Double.compare(GeometricUtils.calcLength(s1), GeometricUtils.calcLength(s2));
            }
            else{
                return GeometricUtils.getPointComparator().compare(s1.getStart(), s2.getStart());
            }
        }
    };

    private Double statusLineX;
    private final TreeSet<LineSegment> notVerticalSegments = new TreeSet<>(notVerticalLineSegmentComparator);
    private final TreeSet<LineSegment> verticalSegments = new TreeSet<>(verticalLineSegmentComparator);

    public void insert(LineSegment segment){
        if(GeometricUtils.isVertical(segment)){
            verticalSegments.add(segment);
        }
        else{
            notVerticalSegments.add(segment);
        }
    }

    public void remove(LineSegment segment){
        if(GeometricUtils.isVertical(segment)){
            verticalSegments.remove(segment);
        }
        else{
            notVerticalSegments.remove(segment);
        }
    }

    public LineSegment above(LineSegment segment){
        notVerticalSegments.remove(segment);
        LineSegment aboveSegment = notVerticalSegments.ceiling(segment);
        notVerticalSegments.add(segment);

        return aboveSegment;
    }

    public LineSegment under(LineSegment segment){
        notVerticalSegments.remove(segment);
        LineSegment underSegment = notVerticalSegments.floor(segment);
        notVerticalSegments.add(segment);

        return underSegment;
    }

    public void setStatusLineX(Double statusLineX) {
//        if(this.statusLineX == null || !this.statusLineX.equals(statusLineX)){
//            verticalSegments.clear();
//        }
        this.statusLineX = statusLineX;
    }

    public List<LineSegment> getVerticalSegments() {
        return verticalSegments.stream().toList();
    }

    public List<LineSegment> getNotVerticalSegments() {
        return notVerticalSegments.stream().toList();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Status {\n");
        sb.append("status line X = ");
        sb.append(statusLineX);
        sb.append("\n");

        sb.append("\nNot vertical segments:\n");
        if(!notVerticalSegments.isEmpty()){
            for(LineSegment s : notVerticalSegments){
                sb.append(s.toString());
                sb.append("\n");
            }
        }
        else{
            sb.append("[NO NOT-VERTICAL SEGMENTS]\n");
        }

        sb.append("\nVertical segments:\n");
        if(!verticalSegments.isEmpty()){
            for(LineSegment s : verticalSegments){
                sb.append(s.toString());
                sb.append("\n");
            }
        }
        else{
            sb.append("[NO VERTICAL SEGMENTS]\n");
        }

        sb.append("}");

        return sb.toString();
    }

    // swap items = delete segments and insert them again
}
