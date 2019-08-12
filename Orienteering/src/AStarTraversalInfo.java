import java.util.*;

public class AStarTraversalInfo {
    public HashMap<Pixel, Pixel> visitedHashMap;
    public double cost;

    public AStarTraversalInfo(HashMap<Pixel, Pixel> visitedHashMap, double cost) {
        this.visitedHashMap = visitedHashMap;
        this.cost = cost;
    }
}