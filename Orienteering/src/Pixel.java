import java.util.HashMap;

public class Pixel {

    private int xCoordinate;
    private int yCoordinate;
    private Terrain terrain;
    private double elevation;

    public Pixel(int xCoordinate, int yCoordinate, double colorInformation, double elevation) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.elevation = elevation;
        initializeColorInformation((int)colorInformation);
    }

    private static HashMap<Integer, Terrain> color2Terrain;
    private static HashMap<Terrain, Integer> terrain2Color;

    static {
        color2Terrain = new HashMap<>();
        color2Terrain.put(-486382, Terrain.OpenLand);
        color2Terrain.put(-16384, Terrain.RoughMeadow);
        color2Terrain.put(-1, Terrain.EasyMovementForest);
        color2Terrain.put(-16592836, Terrain.SlowRunForest);
        color2Terrain.put(-16611288, Terrain.WalkForest);
        color2Terrain.put(-16430824, Terrain.ImpassibleVegetation);
        color2Terrain.put(-16776961, Terrain.Lake);
        color2Terrain.put(-12111101, Terrain.PavedRoad);
        color2Terrain.put(-16777216, Terrain.FootPath);
        color2Terrain.put(-3342235, Terrain.OutOfBound);


        terrain2Color = new HashMap<>();
        terrain2Color.put(Terrain.OpenLand,-486382);
        terrain2Color.put(Terrain.RoughMeadow,-16384);
        terrain2Color.put(Terrain.EasyMovementForest,-1);
        terrain2Color.put(Terrain.SlowRunForest,-16592836);
        terrain2Color.put(Terrain.WalkForest,-16611288);
        terrain2Color.put(Terrain.ImpassibleVegetation,-16430824);
        terrain2Color.put(Terrain.Lake,-16776961);
        terrain2Color.put(Terrain.PavedRoad,-12111101);
        terrain2Color.put(Terrain.FootPath,-16777216);
        terrain2Color.put(Terrain.OutOfBound,-3342235);
        terrain2Color.put(Terrain.FinalPath,-3342336);
    }

    private void initializeColorInformation(int colorInformation) {
        if (!color2Terrain.containsKey(colorInformation)) {
            this.terrain = Terrain.OutOfBound;
            return;
        }

        this.terrain = color2Terrain.get(colorInformation);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain newTerrain) {
        this.terrain = newTerrain;
    }

    public int getTerrainColor() {
        return terrain2Color.get(this.getTerrain());
    }

    public double getElevation() {
        return elevation;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void markPath() {
        this.terrain = Terrain.FinalPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pixel))
            return false;

        Pixel pixel = (Pixel) o;

        return xCoordinate == pixel.xCoordinate && yCoordinate == pixel.yCoordinate;
    }

    @Override
    public int hashCode() {
        int result = xCoordinate;
        result = 31 * result + yCoordinate;
        return result;
    }

    @Override
    public String toString() {
        return "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate;
    }
}

enum Terrain {
    OpenLand,
    RoughMeadow,
    EasyMovementForest,
    SlowRunForest,
    WalkForest,
    ImpassibleVegetation,
    Lake,
    PavedRoad,
    FootPath,
    OutOfBound,
    FinalPath
}