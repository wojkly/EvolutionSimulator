package map;

import config.ReadConfig;
import interfaces.IMapElement;
import interfaces.IPositionChangeObserver;
import interfaces.IWorldMap;
import mapelements.Animal;
import mapelements.Genotype;
import mapelements.Grass;
import movement.Vector2d;
import statistics.StatisticsCounter;
import visualiser.AlertBox;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class JungleWorldMap implements IWorldMap, IPositionChangeObserver {
    private final int width;
    private final int height;
    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Vector2d upperRight;

    private final int jungleWidth;
    private final int jungleHeight;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;

    private final double startEnergy;
    private final double moveEnergy;
    private final double plantEnergy;
    private final double reproduceEnergy;

    private final int startAnimals;

    private final short number;
    //for stats
    private int currentDay;
    private double totalEnergy;
    private int deadAnimals = 0;
    private long deadAnimalsDays = 0;
    private int totalChildren = 0;

    //statisctis counter
    private StatisticsCounter statisticsCounter = new StatisticsCounter();

    private boolean followDominantGenotype = false;

    private Map<Genotype,Integer> currentGenotypes = new HashMap<>();
    private Genotype dominantGenotype;
    private int dominantGenNumber;

    private final GrassPosition grassPositions;

    private final List<Animal> animalsList = new LinkedList<>();
    private final Map<Vector2d, LinkedList<Animal>> animalHashMap = new HashMap<>();
    private final List<Grass> grassList = new LinkedList<>();
    private final Map<Vector2d, Grass> grassHashMap = new HashMap<>();
    public JungleWorldMap(ReadConfig cfg, short number){
        this.number = number;
        this.width = cfg.getWidth();
        this.height = cfg.getHeight();
        this.jungleWidth = (int)(cfg.getJungleRatio() * width);
        this.jungleHeight = (int)(cfg.getJungleRatio() * height);
        this.startEnergy = cfg.getStartEnergy();
        this.moveEnergy = cfg.getMoveEnergy();
        this.plantEnergy = cfg.getPlantEnergy();
        this.reproduceEnergy = startEnergy / 2;

        this.startAnimals = cfg.getNumAnimals();

        this.upperRight = new Vector2d(width - 1, height - 1);

        setJungleBoundaries();

        grassPositions = new GrassPosition(this, lowerLeft, upperRight, lowerLeftJungle, upperRightJungle);

        this.totalEnergy = (startAnimals * startEnergy);

    }

    private void setJungleBoundaries(){
        int minX,minY,maxX,maxY;
        minX = (width/2) - (jungleWidth/2);
        maxX = minX + jungleWidth - 1;

        minY = (height/2) - (jungleHeight/2);
        maxY = minY + jungleHeight - 1;

        lowerLeftJungle = new Vector2d(minX,minY);
        upperRightJungle = new Vector2d(maxX,maxY);

    }
    public void initialize(){
        this.currentDay = 0;
        this.placeAnimals(this.startAnimals);
    }
    public boolean step() throws IOException {
        if(animalsList.size() == 0){
            dominantGenotype = new Genotype(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
            dominantGenNumber = 0;
            return true;
        }

        placeGrassPhase();
        movePhase();
        if(animalsList.size() == 0){
            dominantGenotype = new Genotype(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
            dominantGenNumber = 0;
            return true;
        }
        eatingPhase();
        reproductionPhase();
        setDominantGenotype();
        if(currentDay < StatisticsCounter.GEN_STATS_DAY){
            statisticsCounter.addThisDayGenotypes(this.currentGenotypes);
        }else if(currentDay == StatisticsCounter.GEN_STATS_DAY) {
            statisticsCounter.writeToFile(number);
            AlertBox.display("Stats succesfully generated");
        }
        currentDay += 1;
        return  false;
    }


    @Override
    public String toString() {
        return "Width: "+width
                +" Height: "+height
                +" JungleWidth: "+jungleWidth
                +" JungleHeight "+jungleHeight
                + "LowerLeftJGL: "+lowerLeftJungle
                + "UpperRJGL: "+upperRightJungle;
    }

    public void printAnimals(){
        for(Animal animal: animalsList)
            System.out.println(animal.allInfo());
    }
    public void printGrass(){
        for(Grass grass: grassList)
            System.out.println(grass.toString());
    }


    private Vector2d randomPosition(){
            return new Vector2d(ThreadLocalRandom.current().nextInt(0,width), ThreadLocalRandom.current().nextInt(0,height));
    }

    //PHASES:

    //animals initialization phase:
    @Override
    public void placeAnimals(int num) {
        int i=0;
        int maxCapacity = width * height;
        if(num > maxCapacity){
            AlertBox.display("Too many initial animals");
            throw new IllegalArgumentException("Too many initial animals");
        }

        while(i < num){
            Vector2d pos = randomPosition();
            if(isOccupied(pos))
                continue;
            Animal animal = new Animal(this, pos, startEnergy);
            place(animal);
            i += 1;
        }
    }
    //other phases:
    @Override
    public void placeGrassPhase() {
        if(grassPositions.getStepPosition().isPresent()){
            Vector2d stepPos = grassPositions.getStepPosition().get() ;
            Grass newGrass = new Grass(stepPos);
            place(newGrass);
            grassPositions.delFreeStepSpace(stepPos);
        }
        if(grassPositions.getJunglePosition().isPresent()){
            Vector2d junglePos = grassPositions.getJunglePosition().get() ;
            Grass newGrass = new Grass(junglePos);
            place(newGrass);
            grassPositions.delFreeJungleSpace(junglePos);
        }
    }
    @Override
    public void movePhase() {
        List<Animal> currentAnimals = new ArrayList<>(animalsList);
        for(Animal animal: currentAnimals){
            animal.rotate();
            animal.move();
            animal.changeEnergy( - moveEnergy);
        }
    }

    @Override
    public void eatingPhase() {
        List<Grass> tmpGrasses = new ArrayList<>(grassList);
        for(Grass grass: tmpGrasses){
            Vector2d position = grass.getPosition();

            if(animalHashMap.containsKey(position)) {
                List<Animal> animalsAtPosition = new ArrayList<>(animalHashMap.get(position));
                if (animalsAtPosition.size() > 0) {
                    List<Animal> animalsWithHighestNRG = new ArrayList<>();
                    double highestNRG = 0;
                    for (Animal animal : animalsAtPosition) {
                        if (animal.getEnergy() > highestNRG) {
                            highestNRG = animal.getEnergy();
                            animalsWithHighestNRG.clear();
                            animalsWithHighestNRG.add(animal);
                        } else if (animal.getEnergy() == highestNRG) {
                            animalsWithHighestNRG.add(animal);
                        }
                    }
                    double changeNRG = plantEnergy / animalsWithHighestNRG.size();
                    for (Animal animal : animalsWithHighestNRG) {
                        animal.changeEnergy(changeNRG);
                    }
                    //remove grass from sets, hashmaps etc.
                    grassList.remove(grass);
                    grassHashMap.remove(position);
                    grassPositions.addFreeSpace(position);
                }
            }
        }
    }

    @Override
    public void reproductionPhase() {
        LinkedList<LinkedList<Animal>> listsOfListsOfAnimals = new LinkedList<>(animalHashMap.values());
        for(LinkedList<Animal> listAtPos: listsOfListsOfAnimals){
            if(listAtPos != null && listAtPos.size() > 1){

                //now we are at some coordinates and we have
                //a list with at least 2 animals
                double highestNRG = 0, secondNRG = -1;
                int readyToReproduce = 0;
                LinkedList<Animal> animalsHighestNRG = new LinkedList<>();
                LinkedList<Animal> animalsSecondNRG = new LinkedList<>();
                for(Animal animal: listAtPos){
                    if(animal.getEnergy() >= reproduceEnergy){
                        readyToReproduce += 1;
                        // animal.getEnergy() > highestNRG
                        // clear 2-nd list, 2-nd list = 1-st list
                        // and add animal to first list
                        if(animal.getEnergy() > highestNRG){
                            secondNRG = highestNRG;
                            highestNRG = animal.getEnergy();

                            animalsSecondNRG.clear();
                            animalsSecondNRG.addAll(animalsHighestNRG);
                            animalsHighestNRG.clear();
                            animalsHighestNRG.add(animal);
                        }else if(animal.getEnergy() == highestNRG){
                            animalsHighestNRG.add(animal);
                        }else if(animal.getEnergy() > secondNRG){
                            secondNRG = animal.getEnergy();

                            animalsSecondNRG.clear();
                            animalsSecondNRG.add(animal);
                        }else if(animal.getEnergy() == secondNRG){
                            animalsSecondNRG.add(animal);
                        }
                    }
                }
                if(readyToReproduce > 1){
                    //reproducing now:
                    if(animalsHighestNRG.size() > 1){
                        int idx1 = ThreadLocalRandom.current().nextInt(0,animalsHighestNRG.size());
                        int idx2 = ThreadLocalRandom.current().nextInt(0,animalsHighestNRG.size());
                        while (idx1 == idx2)
                            idx2 = ThreadLocalRandom.current().nextInt(0,animalsHighestNRG.size());
                        Animal parent1 = animalsHighestNRG.get(idx1);
                        Animal parent2 = animalsHighestNRG.get(idx2);

                        Vector2d kidPos = getKidPos(parent1.getPosition());
                        Animal kid = parent1.reproduction(parent2, kidPos);

                        if(parent1.isFollowedAlphaParent() || parent2.isFollowedAlphaParent())
                            kid.setFollowedAlphaChild(true);
                        if(parent1.isFollowedAlphaChild() || parent2.isFollowedAlphaChild() || parent1.isFollowed() || parent2.isFollowed())
                            kid.setFollowed(true);
                        place(kid);
                    }else{
                        Animal parent1 = animalsHighestNRG.get(0);
                        int idx2 = ThreadLocalRandom.current().nextInt(0,animalsSecondNRG.size());
                        Animal parent2 = animalsSecondNRG.get(idx2);

                        Vector2d kidPos = getKidPos(parent1.getPosition());
                        Animal kid = parent1.reproduction(parent2, kidPos);
                        if(parent1.isFollowedAlphaParent() || parent2.isFollowedAlphaParent())
                            kid.setFollowedAlphaChild(true);
                        if(parent1.isFollowedAlphaChild() || parent2.isFollowedAlphaChild() || parent1.isFollowed() || parent2.isFollowed())
                            kid.setFollowed(true);
                        place(kid);
                    }

                }
            }
        }
    }

    public void addKid(){
        totalChildren += 1;
    }

    private Vector2d getKidPos(Vector2d position){
        int xCoord,yCoord;
        int parentX = position.getX();
        int parentY = position.getY();
        List<Vector2d> freePos = new LinkedList<>();
        List<Vector2d> allPos = new LinkedList<>();
        for(int i = parentX - 1; i < parentX + 2; i++){
            for(int j = parentY - 1; j < parentY+ 2; j++){
                if(i != parentX && j != parentY) {
                    xCoord = i;
                    yCoord = j;
                    if (i < 0)
                        xCoord = i + width;
                    xCoord = xCoord % width;
                    if (j < 0)
                        yCoord = j + height;
                    yCoord = yCoord % height;

                    Vector2d newPos = new Vector2d(xCoord,yCoord);
                    if(!isOccupied(newPos)){
                        freePos.add(newPos);
                    }
                    allPos.add(newPos);
                }
            }
        }
        if(freePos.size() > 0){
            int a = ThreadLocalRandom.current().nextInt(0, freePos.size());
            return freePos.get(a);
        }else{
            int a = ThreadLocalRandom.current().nextInt(0, allPos.size());
            return allPos.get(a);
        }
    }

    @Override
    public boolean place(IMapElement element) {
        Vector2d position = element.getPosition();
        if(element.isFood()){
            if(!isOccupied(position)){
                grassHashMap.put(position, (Grass)element);
                grassList.add((Grass) element);
                statisticsCounter.addGrass();
                return true;
            }else
                return false;
        } else{
            animalsList.add((Animal) element);
            addAnimalToMap((Animal) element, position);
            ((Animal)(element)).addObserver(this);
            statisticsCounter.addAnimal();
            if(currentGenotypes.containsKey(((Animal) element).getGenotype())) {
                currentGenotypes.put(((Animal) element).getGenotype(), currentGenotypes.get(((Animal) element).getGenotype()) + 1);
            }
            else
                currentGenotypes.put( ((Animal) element).getGenotype(), 1);
            return true;
        }
    }

    private void addAnimalToMap(Animal animal, Vector2d position){
        LinkedList<Animal> onPos = animalHashMap.get(position);
        if(onPos == null){
            LinkedList<Animal> newList = new LinkedList<>();
            newList.add(animal);
            animalHashMap.put(position, newList);
        }else
            onPos.add(animal);
    }
    private void deleteAnimalFromMap(Animal animal, Vector2d position){
        if(animalHashMap.get(position) != null){
            animalHashMap.get(position).remove(animal);
            if(animalHashMap.get(position).size() == 0){
                animalHashMap.remove(position);
            }
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public IMapElement objectAt(Vector2d position) {
        if(animalHashMap.containsKey(position))
            return animalHashMap.get(position).getFirst();
        if(grassHashMap.containsKey(position))
            return grassHashMap.get(position);
        return null;
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        deleteAnimalFromMap((Animal)element, oldPosition);
        addAnimalToMap((Animal)(element), newPosition);
    }

    @Override
    public void amimalDied(Animal animal) {
        deadAnimals += 1;
        deadAnimalsDays += animal.getDaysAlive();

        currentGenotypes.put(animal.getGenotype() ,currentGenotypes.get(animal.getGenotype()) - 1);
        if(currentGenotypes.get(animal.getGenotype()) == 0){
            currentGenotypes.remove(animal.getGenotype());
        }

        deleteAnimalFromMap(animal, animal.getPosition());
        animalsList.remove(animal);
        animal.removeObserver(this);
    }
    public void changeTotalEnergy(double x){
        this.totalEnergy += x;
    }
    public void resetFollowing(){
        if(animalsList.size() > 0){
            for (Animal animal: animalsList){
                animal.resetAllFollowed(false);
            }
        }
    }

    public void setFollowDominantGenotype(boolean followDominantGenotype) {
        this.followDominantGenotype = followDominantGenotype;
    }

    //getters
    public boolean isFollowDominantGenotype() {
        return followDominantGenotype;
    }

    public Object getAnimalMaxNRGAtPosition(Vector2d v){
        if(animalsList.size() > 0) {
            List<Animal> listAtPos = animalHashMap.get(v);
            if(listAtPos != null) {
                double maxEnergy = -1;
                Animal maxEnergyAnimal = listAtPos.get(0);
                for (Animal animal : listAtPos) {
                    if (animal.getEnergy() > maxEnergy) {
                        maxEnergy = animal.getEnergy();
                        maxEnergyAnimal = animal;
                    }
                }
                return maxEnergyAnimal;
            }
        }
        return null;
    }

    public int getAliveAnimals(){
        return this.animalsList.size();
    }
    public int getAliveGrass(){
        return this.grassList.size();
    }

    public List<Animal> getAnimalsList(){
        return this.animalsList;
    }
    public List<Grass> getGrassList(){
        return this.grassList;
    }

    public List<Vector2d> getAnimalsPositions(){
        List<Vector2d> positions = new ArrayList<>();
        for(Animal animal: this.animalsList)
            positions.add(animal.getPosition());
        return positions;
    }
    public List<Vector2d> getGrassPositions(){
        List<Vector2d> positions = new ArrayList<>();
        for(Grass grass: this.grassList)
            positions.add(grass.getPosition());
        return positions;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public double getMoveEnergy() {
        return moveEnergy;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public double getAverageEnergy(){
        if(getAliveAnimals() != 0)
            return totalEnergy/getAliveAnimals();
        return 0;
    }
    public double getAverageLifetime(){
        if(deadAnimals != 0)
            return ((double) deadAnimalsDays)/((double) deadAnimals);
        return 0;
    }
    public double getAverageAge(){
        long sum = 0;
        for(Animal animal: this.animalsList){
            sum += animal.getDaysAlive();
        }
        return ((double) sum)/((double) getAliveAnimals());
    }
    public double getAverageChildren(){
        return ((double) totalChildren)   /  ((double)(deadAnimals+getAliveAnimals()));
    }
    public double getAverageChildrenThisDay(){
        long sum = 0;
        for(Animal animal: this.animalsList){
            sum += animal.getKids();
        }
        return ((double) sum)/((double) getAliveAnimals());
    }

    public Genotype getDominantGenotype() {
//        System.out.println("DOMGEN: "+dominantGenotype.toString());
        return dominantGenotype;
    }
    public int getDominantGenNumber() {
//        System.out.println("NUM: "+dominantGenNumber);
        return dominantGenNumber;
    }

    private void setDominantGenotype(){
        int maxCount = 0;
        Genotype maxGenotype = animalsList.get(0).getGenotype();
        for(Genotype g: currentGenotypes.keySet()){
            if( currentGenotypes.get(g) > maxCount){
                maxCount = currentGenotypes.get(g);
                maxGenotype = g;
            }
        }
        this.dominantGenotype = maxGenotype;
        this.dominantGenNumber = maxCount;
    }


    public Vector2d getLL(){
        return this.lowerLeft;
    }
    public Vector2d getLLjungle(){
        return this.lowerLeftJungle;
    }
    public Vector2d getUR(){
        return this.upperRight;
    }
    public Vector2d getURjungle(){
        return this.upperRightJungle;
    }

    public short getNumber() {
        return number;
    }
    public int getFollowedAlphaChildren(){
        int sum = 0;
        for(Animal animal: animalsList){
            if(animal.isFollowedAlphaChild())
                sum += 1;
        }
        return sum;
    }
    public int getFollowedRelatives(){
        int sum = 0;
        for(Animal animal: animalsList){
            if(animal.isFollowed())
                sum += 1;
        }
        return sum;
    }
}
