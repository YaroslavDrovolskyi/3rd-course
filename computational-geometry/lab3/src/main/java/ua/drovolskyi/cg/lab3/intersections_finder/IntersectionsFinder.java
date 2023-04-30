package ua.drovolskyi.cg.lab3.intersections_finder;

import ua.drovolskyi.cg.lab3.GeometricUtils;
import ua.drovolskyi.cg.lab3.LineSegment;
import ua.drovolskyi.cg.lab3.Point;
import ua.drovolskyi.cg.lab3.intersections_finder.result.InPointIntersection;
import ua.drovolskyi.cg.lab3.intersections_finder.result.IntersectionsSet;
import ua.drovolskyi.cg.lab3.intersections_finder.result.OverlappingIntersection;

import java.util.List;

public class IntersectionsFinder {
    private Status status;
    private EventQueue eventQueue;
    private IntersectionsSet foundIntersections;
    Double sweepLineX;


    public IntersectionsSet findAllIntersections(List<LineSegment> segments){
        status = new Status();
        eventQueue = new EventQueue();
        initEventQueue(segments);
        foundIntersections = new IntersectionsSet();

        System.out.println("Initial event queue:\n" + eventQueue);

        while(!eventQueue.isEmpty()){
            Event event = eventQueue.getNextEvent();
            sweepLineX = event.getPoint().getX();
            status.setStatusLineX(sweepLineX);


            System.out.println("\n\n====================================== Current event: " + event);

            // if segment start:
            // if insert vertical line, need to check for intersections with not-vertical segments,
            // and for overlapping with vertical segments
            // if insert not vertical line, we should insert as usual + check for intersection with all vertical segments

            // if segment intersection: change order only if this segments are not-vertical

            // if we change sweepingLineX to ew value, we should clear all vertical lines form status


            if(event.getType() == Event.Type.SEGMENT_START){
                LineSegment segment = event.getLineSegment();

                if(segment.getId().equals(8)){
                    System.out.println("\nHere!\n"); /////////////////////////////////////////////////
                }


                status.insert(segment);

                if(!GeometricUtils.isVertical(segment)){
                    // process above segments
                    LineSegment aboveSegment = status.above(segment);
                    // go up and discover all above overlapping intersections (if they exist)
                    // remind: if segments are overlapping, getIntersectionPoint() returns null
                    while(aboveSegment != null && GeometricUtils.areOverlap(segment, aboveSegment)){
                        foundIntersections.addIntersection(new OverlappingIntersection(segment, aboveSegment));
                        aboveSegment = status.above(aboveSegment);
                    }

                    // here we have gone through all above overlapping segments (if they exist),
                    // and now we need to check for intersection of segment with first non-overlapping above segment
                    discoverIntersection(segment, aboveSegment);


                    // process under segments
                    LineSegment underSegment = status.under(segment);
                    // go down and discover all under overlapping intersections (if they exist)
                    // remind: if segments are overlapping, getIntersectionPoint() returns null
                    while(underSegment != null && GeometricUtils.areOverlap(segment, underSegment)){
                        foundIntersections.addIntersection(new OverlappingIntersection(underSegment, segment));
                        underSegment = status.under(underSegment);
                    }

                    // here we have gone through all under overlapping segments (if they exist),
                    // and now we need to check for intersection of segment with first non-overlapping under segment
                    discoverIntersection(underSegment, segment);

                    // check for intersection with vertical segments
                    for(LineSegment s : status.getVerticalSegments()){
                        discoverIntersection(segment, s);
                    }
                }
                else{ // segment is vertical
                    for(LineSegment s : status.getNotVerticalSegments()){
                        discoverIntersection(segment, s);
                    }

                    for(LineSegment s : status.getVerticalSegments()){
                        discoverIntersection(segment, s);
                    }

                    for(LineSegment s : status.getVerticalSegments()){
                        if(!segment.equals(s) && GeometricUtils.areOverlap(segment, s)){
                            foundIntersections.addIntersection(new OverlappingIntersection(segment, s));
                        }
                    }
                }


            }
            else if(event.getType() == Event.Type.SEGMENT_END){
                LineSegment segment = event.getLineSegment();

                if(!GeometricUtils.isVertical(segment)){
                    LineSegment aboveSegment = status.above(segment);
                    LineSegment underSegment = status.under(segment);

                    // check for intersection
                    discoverIntersection(underSegment, aboveSegment);
                }

                status.remove(segment);
            }
            else if(event.getType() == Event.Type.SEGMENTS_INTERSECTION){
                LineSegment aboveSegment = event.getLineSegment2();
                LineSegment underSegment = event.getLineSegment1();

                if(!GeometricUtils.isVertical(aboveSegment) && !GeometricUtils.isVertical(underSegment)){
                    // swap aboveSegment and underSegment
                    status.remove(aboveSegment);
                    status.remove(underSegment);
                    status.insert(aboveSegment);
                    status.insert(underSegment);

                    // discover intersections between new neighbours
                    LineSegment topSegment = status.above(underSegment);
                    LineSegment bottomSegment = status.under(aboveSegment);

                    // if segments needed swapping and were swapped really
                    if(topSegment != aboveSegment && bottomSegment != underSegment){
                        discoverIntersection(underSegment, topSegment);
                        discoverIntersection(bottomSegment, aboveSegment);
                    }
                }
            }
            else{
                throw new RuntimeException("Unknown type of event");
            }

            System.out.println("\nStatus: " + status);
            System.out.println("\nEvent queue: " + eventQueue);
        }

        return foundIntersections;
    }

    private void initEventQueue(List<LineSegment> segments){
        for(LineSegment s : segments){
            eventQueue.addEvent(new Event(Event.Type.SEGMENT_START, s.getStart(), s));
            eventQueue.addEvent(new Event(Event.Type.SEGMENT_END, s.getEnd(), s));
        }
    }

    // s1 must be under s2
    private Boolean discoverIntersection(LineSegment s1, LineSegment s2){
        if(s1 != null && s2 != null){
            Point intersectionPoint = GeometricUtils.getIntersectionPoint(s1, s2);
            if(intersectionPoint != null && intersectionPoint.getX() >= sweepLineX){
                eventQueue.addEvent(new Event(Event.Type.SEGMENTS_INTERSECTION, intersectionPoint,
                        s1, s2));
                foundIntersections.addIntersection(
                        new InPointIntersection(intersectionPoint, s1, s2));
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
