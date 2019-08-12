import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OrienteeringMapInfo {

    private Pixel[][] pixels;
    private SpeedInformation speedInformation;

    public OrienteeringMapInfo(int imageHeight, int imageWidth, SpeedInformation speedInformation) {
        this.speedInformation = speedInformation;
        pixels = new Pixel[imageHeight][imageWidth];
    }

    public OrienteeringMapInfo(int imageHeight, int imageWidth) {
        this(imageHeight, imageWidth, new DefaultSpeedInformation());
    }

    public void InitializePixel(int x, int y, int terrain, double elevation) {
        if (pixels == null || pixels.length <= x || pixels[0].length <= y)
            return;

        pixels[x][y] = new Pixel(x, y, terrain, elevation);
    }

    public Terrain getTerrain(int x, int y) {
        if (pixels == null || pixels.length <= x || pixels[0].length <= y)
            return Terrain.OutOfBound;

        return pixels[x][y].getTerrain();
    }

    public double getElevation(int x, int y) {
        if (pixels == null || pixels.length <= x || pixels[0].length <= y)
            return Double.NaN;

        return pixels[x][y].getElevation();
    }

    public Pixel getPixel(int x, int y) {
        if (pixels == null || pixels.length <= x || pixels[0].length <= y)
            return null;

        return pixels[x][y];
    }

    public void drawPath(String imagePath){
        try {
            BufferedImage imageMap = ImageIO.read(new File(imagePath));

            try {
                for (int x = 0; x < pixels.length; x++) {
                    for (int y = 0; y < pixels[0].length; y++) {
                        imageMap.setRGB(y, x, pixels[x][y].getTerrainColor());
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Exception !!");
            }

            File output = new File("Path.png");
            ImageIO.write(imageMap, "png", output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}