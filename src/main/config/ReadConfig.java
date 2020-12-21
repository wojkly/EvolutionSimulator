package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ReadConfig {
    private int  width;
    private int height;
    private double startEnergy;
    private double moveEnergy;
    private int numAnimals;
    private double plantEnergy;
    private double jungleRatio;

    public static ReadConfig read(String fileName){
        ObjectMapper mapper = new ObjectMapper();

        ReadConfig config;
        try {
            config = mapper.readValue(new File(fileName), ReadConfig.class);
        } catch (IOException e) {
            config = null;
        }

        return config;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public double getStartEnergy() {
        return startEnergy;
    }
    public double getMoveEnergy() {
        return moveEnergy;
    }
    public int getNumAnimals() {
        return numAnimals;
    }
    public double getPlantEnergy() {
        return plantEnergy;
    }
    public double getJungleRatio() {
        return jungleRatio;
    }
}

