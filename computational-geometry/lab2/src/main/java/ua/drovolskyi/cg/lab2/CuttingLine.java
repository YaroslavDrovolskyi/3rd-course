package ua.drovolskyi.cg.lab2;

public class CuttingLine {
    private final Double coordinate;
    private final Type type;

    public CuttingLine(Double coordinate, Type type) {
        this.coordinate = coordinate;
        this.type = type;
    }

    public Double getCoordinate() {
        return coordinate;
    }

    public Type getType() {
        return type;
    }

    public static enum Type{
        HORIZONTAL,
        VERTICAL;

        public static Type getOpposite(Type type){
            return switch (type) {
                case VERTICAL -> HORIZONTAL;
                case HORIZONTAL -> VERTICAL;
            };
        }

        public static String toString(Type type){
            return switch (type) {
                case VERTICAL -> "VERTICAL";
                case HORIZONTAL -> "HORIZONTAL";
            };
        }
    }
}
