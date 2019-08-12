import java.util.HashMap;

public class DefaultSpeedInformation implements SpeedInformation {

    public HashMap<Terrain, Double> speedMap;

    public DefaultSpeedInformation() {
        speedMap = new HashMap<>();
        speedMap.put(Terrain.OpenLand, 1.0);
        speedMap.put(Terrain.RoughMeadow, 4.0);
        speedMap.put(Terrain.EasyMovementForest, 3.0);
        speedMap.put(Terrain.SlowRunForest, 5.0);
        speedMap.put(Terrain.WalkForest, 10.0);
        speedMap.put(Terrain.ImpassibleVegetation, 40.0);
        speedMap.put(Terrain.Lake, 5.0);
        speedMap.put(Terrain.PavedRoad, 1.0);
        speedMap.put(Terrain.FootPath, 1.0);
        speedMap.put(Terrain.OutOfBound, 1000.0);
        speedMap.put(Terrain.FinalPath, 1.0);
    }

    @Override
    public double getSpeed(Pixel p) {
        if (!speedMap.containsKey(p.getTerrain()))
            return speedMap.get(Terrain.OutOfBound);

        return speedMap.get(p.getTerrain());
    }
}