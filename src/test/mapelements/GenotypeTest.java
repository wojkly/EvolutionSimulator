package mapelements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {

    @Test
    void fixGenes() {
        int[] arr = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,4,4,4,4,4,4,4,2,2,2,2,2,2,2,2};
        int[] arr2 = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] arr3 = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0};
        int[] arr4 = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] arr5 = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Genotype gen = new Genotype(arr);
        gen.fixGenes();
        Genotype gen2 = new Genotype(arr2);
        gen2.fixGenes();
        Genotype gen3 = new Genotype(arr3);
        gen3.fixGenes();
        Genotype gen4 = new Genotype(arr4);
        gen4.fixGenes();
    }
}