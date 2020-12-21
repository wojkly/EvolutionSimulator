package statistics;

import map.JungleWorldMap;
import mapelements.Genotype;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatisticsCounter {
    public static final int GEN_STATS_DAY = 604;
    private int totalAnimals;
    private int totalGrass;
    private Map<Genotype,Integer> totalGenotypesCount = new HashMap<>();

    public StatisticsCounter() {
    }
    //when animal is placed
    public void addAnimal(){
        totalAnimals += 1;
    }
    //when grass is placed
    public void addGrass() {
        totalGrass += 1;
    }
    //every day
    public void addThisDayGenotypes(Map<Genotype,Integer> map){
        for(Genotype genotype: map.keySet()){
            if(map.get(genotype) > 0){
                if(!this.totalGenotypesCount.containsKey(genotype))
                    this.totalGenotypesCount.put(genotype,map.get(genotype));
                else
                    this.totalGenotypesCount.put(genotype,map.get(genotype) + this.totalGenotypesCount.get(genotype));
            }
        }
    }
    public void writeToFile(short number) throws IOException {
        WriteStatsToFile.writeStatsToFile(this,number);
    }

    public int getTotalAnimals() {
        return totalAnimals;
    }

    public int getTotalGrass() {
        return totalGrass;
    }
    public Map<Genotype, Integer> getTotalGenotypesCount() {
        return totalGenotypesCount;
    }
    public Genotype getMostCommonGenotype() {
        if (totalGenotypesCount.size() < 1)
            throw new IllegalArgumentException("Error, no genotypes to generate stats from");
        int num = -1;
        Genotype maxCountGenotype = (Genotype)(totalGenotypesCount.keySet().toArray())[0];
        for(Genotype genotype: totalGenotypesCount.keySet()){
            if(  totalGenotypesCount.get(genotype) > num){
                num = totalGenotypesCount.get(genotype);
                maxCountGenotype = genotype;
            }
        }
        return maxCountGenotype;
    }
    public int getGenotypeCount(Genotype genotype){
        if(totalGenotypesCount.containsKey(genotype))
            return totalGenotypesCount.get(genotype);
        return -1;
    }
}
