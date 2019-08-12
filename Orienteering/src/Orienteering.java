import javafx.util.Pair;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class Orienteering {

    public static void main(String args[]) {

        OrienteeringMapInfo mapInfo = InitializeMapInfo(
                "./src/color.png",
                "./src/elevations.txt"
        );

        StartOrienteering("./src/white.txt", mapInfo, null);
//        StartOrienteering("./src/brown.txt", mapInfo, null);
//        StartOrienteering("./src/red.txt", mapInfo, null);
//        StartOrienteering("./src/eastesker.txt", mapInfo, null);
//        StartOrienteering("./src/allpark.txt", mapInfo, "./src/weights.txt");
//        StartOrienteering("./src/allpark.txt", mapInfo, null);


        mapInfo.drawPath("./src/color.png");
        System.out.println("Path Image Created");
    }

    private static OrienteeringMapInfo InitializeMapInfo(String colorMapFileName, String elevationFileName) {
        try {

            // Input file paths
            File elevationFile = new File(elevationFileName);
            File colorMapFile = new File(colorMapFileName);

            // Structures to store the files
            double elevation[][] = new double[500][400];

            // Initializing Scanner and BufferedImage which read the files
            // Reading the elevation file and storing the values in Elevation[][] matrix.
            Scanner sc = new Scanner(elevationFile);
            BufferedImage mapImage = ImageIO.read(colorMapFile);

            int row = 0, col;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String words[] = line.trim().split("   ");
                col = 0;
                for( String str : words ){
                    elevation[row][col] = Double.parseDouble(str);
                    col++;
                }
                row++;
            }
            sc.close();

            OrienteeringMapInfo mapInfo = new OrienteeringMapInfo(500, 395, new DefaultSpeedInformation());
            for (int xCoordinates = 0; xCoordinates < 500; xCoordinates++){
                for (int yCoordinates = 0; yCoordinates < 395; yCoordinates++){
                    mapInfo.InitializePixel(xCoordinates, yCoordinates, mapImage.getRGB(yCoordinates,xCoordinates), elevation[xCoordinates][yCoordinates]);
                }
            }
            return mapInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void StartOrienteering(String pathFileName, OrienteeringMapInfo mapInfo, String weightsFileName) {
        List<Pixel> Locations = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(pathFileName));

            SpeedInformation speedInformation = new DefaultSpeedInformation();
            if (weightsFileName != null) {
                ArrayList<Double> speeds = ReadCustomWeights(weightsFileName);
                speedInformation = new CustomSpeedInformation(speeds);
            }

            AStarTraversal aStar = new AStarTraversal(mapInfo, speedInformation);

            //Assuming the file is in correct format and not empty
            String type = sc.nextLine();
            String line;
            String words[];
            int xCoordinate, yCoordinate;
            if(type.equalsIgnoreCase("Classic")) {
                classicTraversal(sc, mapInfo, aStar);
            }
            else {
                scoreOTraversal(sc, mapInfo, aStar);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void classicTraversal(Scanner sc, OrienteeringMapInfo mapInfo, AStarTraversal aStar) {
        List<Pixel> Locations;

        Locations = classicFile(sc, mapInfo);
        HashMap<Pixel, AStarTraversalInfo> visitedHashMaps = new HashMap();

        for (int i = 1; i < Locations.size(); i++) {
            Pixel startPixel = Locations.get(i - 1);
            Pixel endPixel = Locations.get(i);
            visitedHashMaps.put(endPixel, aStar.Traverse(startPixel, endPixel));
        }

        for (Pixel endPixel : visitedHashMaps.keySet())
            aStar.updateTerrain(visitedHashMaps.get(endPixel).visitedHashMap, endPixel);
    }

    private static List<Pixel> classicFile(Scanner sc, OrienteeringMapInfo mapInfo){
        List<Pixel> Locations = new ArrayList<>();
        String line;
        String words[];
        int xCoordinate, yCoordinate;
        while (sc.hasNextLine()){
            line = sc.nextLine();
            words = line.trim().split(" ");
            xCoordinate = Integer.parseInt(words[1]);
            yCoordinate = Integer.parseInt(words[0]);
            Locations.add(mapInfo.getPixel(xCoordinate,yCoordinate));
        }
        return Locations;
    }

    private static void scoreOTraversal(Scanner sc, OrienteeringMapInfo mapInfo, AStarTraversal aStar){
        List<Pixel> Locations = new ArrayList<>();
        double givenTime = Double.parseDouble(sc.nextLine());
        String line;
        String words[];
        int xCoordinate, yCoordinate;

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            words = line.trim().split(" ");
            xCoordinate = Integer.parseInt(words[1]);
            yCoordinate = Integer.parseInt(words[0]);
            Locations.add(mapInfo.getPixel(xCoordinate,yCoordinate));
        }

        HashMap<Pixel, HashMap<Pixel, Double>> costMap = new HashMap<>();
        HashMap<Pair<Pixel, Pixel>, AStarTraversalInfo> traversalInfoHashMap = new HashMap<>();

        allLocationDistances(aStar, Locations, costMap, traversalInfoHashMap);

        Pixel startPixel = Locations.get(0);
        HashSet<S_TraversalPath> scoreList = new HashSet<>();
        List<Pixel> startNode = new ArrayList<>();
        startNode.add(startPixel);
        scoreList.add(new S_TraversalPath(startNode, 0));

        double leastCost = Double.POSITIVE_INFINITY;
        leastCost = getLeastCost(costMap, leastCost);

        traverseFromStart(Locations, givenTime, costMap, startPixel, scoreList, leastCost);

        S_TraversalPath highest = getFinalPath(scoreList);

        updateTerrain(aStar, traversalInfoHashMap, highest);
    }

    private static void updateTerrain(AStarTraversal aStar, HashMap<Pair<Pixel, Pixel>, AStarTraversalInfo> traversalInfoHashMap, S_TraversalPath highest) {
        for (int i = 1; i < highest.PathTaken.size(); i++) {
            Pixel previousPixel = highest.PathTaken.get(i - 1);
            Pixel currentPixel = highest.PathTaken.get(i);

            aStar.updateTerrain(traversalInfoHashMap.get(new Pair<>(previousPixel, currentPixel)).visitedHashMap, currentPixel);
        }
    }

    private static S_TraversalPath getFinalPath(HashSet<S_TraversalPath> scoreList) {
        S_TraversalPath highest = null;
        double highestCount = 0;
        for (S_TraversalPath path : scoreList) {
            if (path.PathTaken.size() <= 1)
                continue;

            if (path.getLastNode() != path.PathTaken.get(0))
                continue;

            if (path.PathTaken.size() > highestCount) {
                highest = path;
                highestCount = path.PathTaken.size();
            }
        }
        return highest;
    }

    private static void traverseFromStart(List<Pixel> locations, double givenTime, HashMap<Pixel, HashMap<Pixel, Double>> costMap, Pixel startPixel, HashSet<S_TraversalPath> scoreList, double leastCost) {
        for (int i = (int)Math.floor(leastCost); i <= givenTime; i++) {
            List<S_TraversalPath> newNodesToAdd = new ArrayList<S_TraversalPath>();
            for (S_TraversalPath itemInList : scoreList) {
                Pixel endNode = itemInList.getLastNode();

                if ((endNode == startPixel) && (itemInList.PathTaken.size() > 1))
                    continue;
                for (Pixel nextLocation : locations) {
                    if (itemInList.PathTaken.contains(nextLocation) && nextLocation != startPixel)
                        continue;

                    double newtimeTaken = costMap.get(endNode).get(nextLocation);
                    if ((itemInList.TimeTaken + newtimeTaken) > i)
                        continue;

                    S_TraversalPath newNode = new S_TraversalPath(itemInList);
                    newNode.PathTaken.add(nextLocation);
                    newNode.TimeTaken += newtimeTaken;

                    if (!itemInList.Children.contains(newNode)) {
                        itemInList.Children.add(newNode);
                        newNodesToAdd.add(newNode);
                    }
                }
            }
            scoreList.addAll(newNodesToAdd);
        }
    }

    private static double getLeastCost(HashMap<Pixel, HashMap<Pixel, Double>> costMap, double leastCost) {
        for (Pixel fromPixel: costMap.keySet()) {
            HashMap<Pixel, Double> fromPixelMap = costMap.get(fromPixel);
            for (Pixel toPixel: fromPixelMap.keySet()) {
                if (fromPixel == toPixel)
                    continue;

                double cost = fromPixelMap.get(toPixel);
                if (cost < leastCost)
                    leastCost = cost;
            }
        }
        return leastCost;
    }

    private static void allLocationDistances(AStarTraversal aStar, List<Pixel> locations, HashMap<Pixel, HashMap<Pixel, Double>> costMap, HashMap<Pair<Pixel, Pixel>, AStarTraversalInfo> traversalInfoHashMap) {
        for (Pixel fromPixel : locations) {
            for (Pixel toPixel : locations) {
                AStarTraversalInfo aStarTraversalInfo = aStar.Traverse(fromPixel, toPixel);
                if (!costMap.containsKey(fromPixel))
                    costMap.put(fromPixel, new HashMap<>());

                HashMap<Pixel, Double> fromPixelMap = costMap.get(fromPixel);
                fromPixelMap.put(toPixel, aStarTraversalInfo.cost);
                costMap.put(fromPixel, fromPixelMap);
                traversalInfoHashMap.put(new Pair<>(fromPixel, toPixel), aStarTraversalInfo);
            }
        }
    }

    private static ArrayList<Double> ReadCustomWeights(String weightsFileName) {
        ArrayList<Double> speeds = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(weightsFileName));
            String line;
            String words[];
            while (sc.hasNextLine()){
                line = sc.nextLine();
                words = line.trim().split(" ");
                speeds.add(Double.parseDouble(words[0]));
            }
            return speeds;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}