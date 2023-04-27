package ua.drovolskyi.cg.lab3;

import javax.sound.sampled.Line;
import java.util.*;

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
                return true;
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


    public static enum Type{
        SEGMENT_START,
        SEGMENT_END,
        SEGMENTS_INTERSECTION
    }
}
