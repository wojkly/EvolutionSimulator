package visualiser;

import config.ReadConfig;
import map.JungleWorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    static final short DELAY_TIME = 0;

    public static void main(String[] args){
        if (args.length == 0) {
            AlertBox.display("Wrong file name\n Change it to \"parameters.json\"");
            throw new IllegalArgumentException("Wrong file name\n Change it to \"parameters.json\"");
        }
        ReadConfig cfg = ReadConfig.read(args[0]);
        if (cfg == null) {
            AlertBox.display("Wrong data");
            throw new IllegalArgumentException("Wrong data");
        }


        JungleWorldMap map1 = new JungleWorldMap(cfg, (short) 1);
        JungleWorldMap map2 = new JungleWorldMap(cfg, (short) 2);

        EventQueue.invokeLater(() -> {
            SimulationFrame simulationFrame1 = new SimulationFrame(map1, 1,100, 0);
            SimulationFrame simulationFrame2 = new SimulationFrame(map2, 2,800, 0);

            map1.initialize();
            map2.initialize();

            simulationFrame1.getPanel().repaint();
            simulationFrame2.getPanel().repaint();

            Timer timer = new Timer(DELAY_TIME, e -> {
                if (!simulationFrame1.getStopped()) {
                    try {
                        simulationFrame1.setStopped(map1.step());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if(simulationFrame1.getStopped())
                        AlertBox.display("All animals at simulation 1 are dead");
                    simulationFrame1.updateStatistics(
                            Integer.toString(map1.getCurrentDay()),
                            Integer.toString(map1.getAliveAnimals()),
                            Integer.toString(map1.getAliveGrass()),
                            Double.toString(map1.getAverageEnergy()),
                            Double.toString(map1.getAverageLifetime()),
                            Double.toString(map1.getAverageChildrenThisDay()),
                            map1.getDominantGenotype().toString(),
                            Integer.toString(map1.getDominantGenNumber())
                    );
                    simulationFrame1.getPanel().repaint();
                }
                if (!simulationFrame2.getStopped()) {
                    try {
                        simulationFrame2.setStopped(map2.step());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if(simulationFrame2.getStopped())
                        AlertBox.display("All animals at simulation 2 are dead");
                    simulationFrame2.updateStatistics(
                            Integer.toString(map2.getCurrentDay()),
                            Integer.toString(map2.getAliveAnimals()),
                            Integer.toString(map2.getAliveGrass()),
                            Double.toString(map2.getAverageEnergy()),
                            Double.toString(map2.getAverageLifetime()),
                            Double.toString(map2.getAverageChildrenThisDay()),
                            map2.getDominantGenotype().toString(),
                            Integer.toString(map2.getDominantGenNumber())
                    );
                    simulationFrame2.getPanel().repaint();
                }
            });
            timer.start();
        });
    }
}
