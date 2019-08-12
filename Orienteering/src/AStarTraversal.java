import java.util.*;

public class AStarTraversal {

    private OrienteeringMapInfo mapInfo;
    private SpeedInformation speedMap;

    private Pixel endPixel;

    public AStarTraversal(OrienteeringMapInfo mapInfo, SpeedInformation speedMap) {
        this.mapInfo = mapInfo;
        this.speedMap = speedMap;
    }

    public AStarTraversalInfo Traverse(Pixel start, Pixel end) {

        this.endPixel = end;
        PriorityQueue<TravelNode> Q = new CustomPriorityQueue<>();
        HashMap<Pixel, Pixel> visited = new HashMap<>();
        TravelNode initialNode = new TravelNode(null, start);
        Q.add(initialNode);

        while (!Q.isEmpty()){

            TravelNode currentNode = Q.poll();
            visited.put(currentNode.getCurrentPixel(), currentNode.getPreviousPixel());

            if (currentNode.getCurrentPixel().equals(end)) {
//                updateTerrain(visited, end);
                return new AStarTraversalInfo(visited, currentNode.getG());
            }

            Q.addAll(getSuccessorList(currentNode, visited));
        }

        return null;
    }

    private ArrayList<TravelNode> getSuccessorList(TravelNode currentNode, HashMap<Pixel, Pixel> visited){

        ArrayList<TravelNode> neighbors = getNeighbors(currentNode);
        ArrayList<TravelNode> filteredNeighbors = filterNeighbours(neighbors, visited);

        // g is the path cost and h is the heuristic
        double g, h;

        for(TravelNode neighbor : filteredNeighbors) {
            g = calculateTraversalCost(currentNode.getCurrentPixel(), neighbor.getCurrentPixel());
            h = calculateTraversalCost(neighbor.getCurrentPixel(), endPixel);
            neighbor.setG(neighbor.getG() + g);
            neighbor.setH(h);
        }

        return filteredNeighbors;
    }

    private ArrayList<TravelNode> filterNeighbours(ArrayList<TravelNode> neighbors, HashMap<Pixel, Pixel> visited) {
        ArrayList<TravelNode> returnList = new ArrayList<>();

        for (TravelNode neighbor : neighbors) {
            if (!visited.containsKey(neighbor.getCurrentPixel()))
                returnList.add(neighbor);
        }
        return returnList;
    }

    private ArrayList<TravelNode> getNeighbors(TravelNode currentNode){

        ArrayList<TravelNode> neighbors = new ArrayList<>(8);
        int currentRow = currentNode.getCurrentPixel().getxCoordinate();
        int currentCol = currentNode.getCurrentPixel().getyCoordinate();

        for(int rowIndex = -1; rowIndex <= 1; rowIndex++){
            for(int colIndex = -1; colIndex<= 1; colIndex++){

                // if out of the map
                if(currentRow + rowIndex < 0 ||
                        currentRow + rowIndex > 499 ||
                        currentCol + colIndex < 0 ||
                        currentCol + colIndex > 394)
                    continue;

                // if same as the current pixel
                if (rowIndex == 0 && colIndex == 0)
                    continue;

                TravelNode node = new TravelNode(
                        mapInfo.getPixel(currentRow, currentCol),
                        mapInfo.getPixel(currentRow + rowIndex, currentCol + colIndex));
                node.setG(currentNode.getG());
                neighbors.add(node);
            }
        }

        return neighbors;
    }

    private double calculateTraversalCost(Pixel start, Pixel end) {

        double xDistance, yDistance;
        xDistance = Math.abs(start.getxCoordinate() - end.getxCoordinate()) * 10.29;
        yDistance = Math.abs(start.getyCoordinate() - end.getyCoordinate()) * 7.55;

        double xyDistance;
        xyDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

        double travelTime;
        travelTime = xyDistance * speedMap.getSpeed(end);

        double multiplyFactor = end.getElevation()/start.getElevation();
        travelTime *= Math.abs(multiplyFactor);

        return travelTime;
    }

    public void updateTerrain(HashMap<Pixel, Pixel> visited, Pixel endPixel){
        endPixel.setTerrain(Terrain.FinalPath);
        Pixel parentNode = visited.get(endPixel);

        while (parentNode != null){
            parentNode.setTerrain(Terrain.FinalPath);
            parentNode = visited.get(parentNode);
        }
    }
}