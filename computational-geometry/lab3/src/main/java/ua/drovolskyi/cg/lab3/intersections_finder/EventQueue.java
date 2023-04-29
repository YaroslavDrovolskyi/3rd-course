package ua.drovolskyi.cg.lab3.intersections_finder;

import ua.drovolskyi.cg.lab3.GeometricUtils;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventQueue {
    private static final Comparator<Event.Type> eventTypeComparator = new Comparator<Event.Type>() {
        @Override
        public int compare(Event.Type t1, Event.Type t2) {
            if(t1 == Event.Type.SEGMENT_START){
                if(t2 == Event.Type.SEGMENT_START){
                    return 0;
                }
                else {
                    return -1;
                }
            }
            else if(t1 == Event.Type.SEGMENT_END){
                if(t2 == Event.Type.SEGMENT_END){
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else{
                if(t2 == Event.Type.SEGMENTS_INTERSECTION){
                    return 0;
                }
                else if (t2 == Event.Type.SEGMENT_START){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        }
    };
    private static final Comparator<Event> eventComparator = new Comparator<Event>() {
        @Override
        public int compare(Event e1, Event e2) {
            if(!e1.getPoint().equals(e2.getPoint())){
                return GeometricUtils.getPointComparator().compare(e1.getPoint(), e2.getPoint());
            }
            else{
                return eventTypeComparator.compare(e1.getType(), e2.getType());
            }
        }
    };
    private final SortedSet<Event> events = new TreeSet<>(eventComparator);

    public void addEvent(Event e){
        if(events.contains(e)){
    //        throw new RuntimeException("Event already exist"); //////////////////////////////////////////////
        }
        events.add(e);
    }

    public Event getNextEvent(){
        Event e = events.first();
        events.remove(e);
        return e;
    }

    public Boolean isEmpty(){
        return events.isEmpty();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("EventQueue {\n");

        if(!events.isEmpty()){
            for(Event e : events){
                sb.append(e.toString());
                sb.append("\n");
            }
        }
        else{
            sb.append("[NO EVENTS]\n");
        }

        sb.append("}");

        return sb.toString();
    }


}
