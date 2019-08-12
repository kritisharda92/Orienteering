import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomSpeedInformation implements SpeedInformation {

    public HashMap<Terrain, Double> speedMap;

    public CustomSpeedInformation(ArrayList<Double> speed) {
        if (speed == null || speed.size() != 10) {
            speedMap = new DefaultSpeedInformation().speedMap;
            return;
        }

        speedMap = new HashMap<>();
        speedMap.put(Terrain.OpenLand, speed.get(0));
        speedMap.put(Terrain.RoughMeadow, speed.get(1));
        speedMap.put(Terrain.EasyMovementForest, speed.get(2));
        speedMap.put(Terrain.SlowRunForest, speed.get(3));
        speedMap.put(Terrain.WalkForest, speed.get(4));
        speedMap.put(Terrain.ImpassibleVegetation, speed.get(5));
        speedMap.put(Terrain.Lake, speed.get(6));
        speedMap.put(Terrain.PavedRoad, speed.get(7));
        speedMap.put(Terrain.FootPath, speed.get(8));
        speedMap.put(Terrain.OutOfBound, speed.get(9));
        speedMap.put(Terrain.FinalPath, 0.01);
    }

    @Override
    public double getSpeed(Pixel p) {
        if (!speedMap.containsKey(p.getTerrain()))
            return speedMap.get(Terrain.OutOfBound);

        return speedMap.get(p.getTerrain());
    }
}