package statistics;

import mapelements.Genotype;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class WriteStatsToFile {
    public static final String filePath = "stats";
    public static final String fileExtension = ".txt";

    public static void writeStatsToFile(StatisticsCounter statisticsCounter, short number) throws IOException {
        String fullFilePath = filePath + number + fileExtension;

        File file = new File(fullFilePath);
        FileWriter writer = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(writer);

        Genotype mostCommonGenotype = statisticsCounter.getMostCommonGenotype();
        int mostCommonGenotypeCount =  statisticsCounter.getGenotypeCount(mostCommonGenotype);

        printWriter.println("Days: "+StatisticsCounter.GEN_STATS_DAY);
        printWriter.println("Total placed animals: "+ statisticsCounter.getTotalAnimals());
        printWriter.println("Total planted Grass: "+ statisticsCounter.getTotalGrass());
        printWriter.println("Most common genotype was: "+ mostCommonGenotype.toString());
        printWriter.println("Most common genotype count was: "+ mostCommonGenotypeCount);
        printWriter.close();
    }
}
