package ua.drovolskyi.cg.lab3.intersections_finder;

import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.Point;

// if type is SEGMENTS_INTERSECTION, then segment1 is lower than segment2
public class Event {
    private final Type type;
    private final Point point;
    private final LineSegment segment1;
    private final LineSegment segment2;

    // use this constructor only when event is start or end of line segment
    public Event(Type type, Point point, LineSegment segment){
        if(type == Type.SEGMENTS_INTERSECTION){
            throw new RuntimeException("Use constructor with one line segments only when type is not SEGMENTS_INTERSECTION");
        }
        this.type = type;
        this.point = point;
        this.segment1 = segment;
        this.segment2 = null;
    }

    // use this constructor when event is intersection
    public Event(Type type, Point point, LineSegment segment1, LineSegment segment2){
        if(type != Type.SEGMENTS_INTERSECTION){
            throw new RuntimeException("Use constructor with two line segments only when type is SEGMENTS_INTERSECTION");
        }
        this.type = type;
        this.point = point;
        this.segment1 = segment1;
        this.segment2 = segment2;
    }

    public Type getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

    // use this only when event is start or end of line segment
    public LineSegment getLineSegment(){
        if(type == Type.SEGMENTS_INTERSECTION){
            throw new RuntimeException("Use this method only when type is not SEGMENTS_INTERSECTION");
        }
        return segment1;
    }

    // use this method only when event is intersection
    public LineSegment getLineSegment1(){
        if(type != Type.SEGMENTS_INTERSECTION){
            throw new RuntimeException("Use this method only when type is SEGMENTS_INTERSECTION");
        }
        return segment1;
    }

    // use this method only when event is intersection
    public LineSegment getLineSegment2(){
        if(type != Type.SEGMENTS_INTERSECTION){
            throw new RuntimeException("Use this method only when type is SEGMENTS_INTERSECTION");
        }
        return segment2;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Event)){
            return false;
        }
        Event e = (Event)obj;

        if(this.getType() != e.getType()){
            return false;
        }

        if(!this.getPoint().equals(e.getPoint())){
            return false;
        }

        if(this.type != Type.SEGMENTS_INTERSECTION){
            if(!this.getLineSegment().equals(e.getLineSegment())){
                return false;
            }
        }
        else{
            if(!this.getLineSegment1().equals(e.getLineSegment1())){
                return false;
            }

            if(!this.getLineSegment2().equals(e.getLineSegment2())){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{Event ");
        sb.append(type);
        sb.append(", ");
        sb.append(point);
        sb.append(", ");
        sb.append(segment1);

        if(segment2 != null){
            sb.append(", ");
            sb.append(segment2);
        }

        sb.append("}");

        return sb.toString();
    }


    public static enum Type{
        SEGMENT_START("SEGMENT_START"),
        SEGMENT_END("SEGMENT_END"),
        SEGMENTS_INTERSECTION("SEGMENTS_INTERSECTION");

        private final String label;

        private Type(String label){
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}
