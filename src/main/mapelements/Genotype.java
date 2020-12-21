package mapelements;

import movement.Vector2d;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Genotype {
    private final int GEN_SIZE = 32;
    private int [] genes;

    public Genotype(){
        this.genes = new int[GEN_SIZE];
        this.genRandom();
        this.fixGenes();
    }
    public Genotype(int [] array){
        this.genes = new int[GEN_SIZE];
        System.arraycopy(array,0,genes,0,GEN_SIZE);
        this.fixGenes();
    }
    public Genotype(Genotype gene1, Genotype gene2){
        genes = new int[GEN_SIZE];
        getGenesFromParents(gene1, gene2);
        fixGenes();
    }

    @Override
    public int hashCode() {
        int hash=0;
        for (int i = 0; i < GEN_SIZE; i++) {
            hash += Objects.hash(genes[i]);
        }
        return hash;
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Genotype))
            return false;
        Genotype tmp = (Genotype) other;
        return Arrays.equals(this.genes, tmp.genes);
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }

    public int getRandomGene(){
        return genes[ThreadLocalRandom.current().nextInt(0, GEN_SIZE)];
    }

    private void genRandom(){
        for(int i = 0; i < GEN_SIZE; i++)
            genes[i] = ThreadLocalRandom.current().nextInt(0, 8);
        Arrays.sort(genes);
    }
    public void fixGenes(){
        SortedSet<Integer> zeros = new TreeSet<Integer>();
        for (int i = 0; i < 8; i++) {
            zeros.add(i);
        }
        SortedSet<Integer> ones = new TreeSet<Integer>();
        Map<Integer,Integer> additional = new HashMap<>();
        for (int i : genes) {
            if(zeros.contains(i))
                zeros.remove(i);
            if(!ones.contains(i))
                ones.add(i);
            else{
                if (additional.containsKey(i))
                    additional.put(i, additional.get(i) + 1);
                else
                    additional.put(i, 1);
            }
        }
        List<Integer> keysList = new ArrayList<>(additional.keySet());
        if(zeros.size() > 0){
            for(Integer i: zeros){
                int randomValue = ThreadLocalRandom.current().nextInt(0, keysList.size());
                additional.put( keysList.get(randomValue), additional.get(keysList.get(randomValue)) - 1 );
                if ( additional.get(keysList.get(randomValue)) == 0){
                    keysList.remove(randomValue);
                }
            }
        }
        List<Integer> resList = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            resList.add(i);
            while(additional.containsKey(i) && additional.get(i) > 0){
                resList.add(i);
                additional.put(i, additional.get(i)-1);
            }
        }
        for (int i = 0; i < 32; i++) {
            genes[i] = resList.get(0);
            resList.remove(0);
        }

        boolean[] count = new boolean[8];
        for (int i = 0; i < 32; i++) {
            count[genes[i]] = true;
        }
        for (int i = 0; i < 8; i++) {
            if(!count[i]){
                throw new IllegalArgumentException("Fix genes dont work");
            }
        }
    }
    public void  getGenesFromParents(Genotype gene1, Genotype gene2){
        int lowerLimit = ThreadLocalRandom.current().nextInt(1, GEN_SIZE - 1);
        int upperLimit = ThreadLocalRandom.current().nextInt(1, GEN_SIZE - 1);
        while(lowerLimit == upperLimit)
            upperLimit = ThreadLocalRandom.current().nextInt(1, GEN_SIZE - 1);
        //swapping so upper is > than lower
        if (lowerLimit> upperLimit) {
            int a = lowerLimit;
            lowerLimit = upperLimit;
            upperLimit = a;
        }

        System.arraycopy(gene1.getGenes(), 0, genes, 0, lowerLimit);
        System.arraycopy(gene2.getGenes(), lowerLimit, genes, lowerLimit, upperLimit - lowerLimit);
        System.arraycopy(gene1.getGenes(), upperLimit, genes, upperLimit, GEN_SIZE - upperLimit);

        Arrays.sort(genes);
    }

    public int[] getGenes(){
        return genes;
    }

    public int getGEN_SIZE() {
        return GEN_SIZE;
    }
}
