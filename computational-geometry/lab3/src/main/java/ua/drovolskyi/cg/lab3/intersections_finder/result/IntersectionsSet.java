package ua.drovolskyi.cg.lab3.intersections_finder.result;

import ua.drovolskyi.cg.lab3.GeometricUtils;
import ua.drovolskyi.cg.lab3.Point;

import java.util.*;

public class IntersectionsSet {
    private final Set<OverlappingIntersection> overlappingIntersections = new HashSet<>();
    private final Map<Point, InPointIntersection> inPointIntersections = new TreeMap<>(GeometricUtils.getPointComparator());

    public void addIntersection(Intersection intersection){
        if(intersection instanceof OverlappingIntersection overlappingIntersection){
            overlappingIntersections.add(overlappingIntersection);
        }
        else if(intersection instanceof InPointIntersection inPointIntersection){
            if (inPointIntersections.containsKey(inPointIntersection.getPoint())) { // is such intersection exists
                inPointIntersections.get(inPointIntersection.getPoint())
                        .addSegments(inPointIntersection.getLineSegments());
            }
            else{
                inPointIntersections.put(inPointIntersection.getPoint(), inPointIntersection);
            }
        }
        else{
            throw new RuntimeException("Unknown intersection type");
        }
    }

    public Set<OverlappingIntersection> getOverlappingIntersections() {
        return overlappingIntersections;
    }

    public Map<Point, InPointIntersection> getInPointIntersections() {
        return inPointIntersections;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Intersections set {\n\n");
        if(overlappingIntersections.isEmpty() && inPointIntersections.isEmpty()){
            sb.append("[NO INTERSECTIONS]\n\n");
        }
        else{
            if(!overlappingIntersections.isEmpty()){
                for(Intersection i : overlappingIntersections){
                    sb.append(i);
                    sb.append("\n\n");
                }
            }
            if(!inPointIntersections.isEmpty()){
                for(Intersection i : inPointIntersections.values()){
                    sb.append(i);
                    sb.append("\n\n");
                }
            }
        }

        sb.append("}");

        return sb.toString();
    }
}
