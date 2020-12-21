package mapelements;

import interfaces.IMapElement;
import interfaces.IPositionChangeObserver;
import interfaces.IPositionChangePublisher;
import interfaces.IWorldMap;
import map.JungleWorldMap;
import movement.MapDirection;
import movement.Vector2d;
import visualiser.AlertBox;

import java.util.ArrayList;

public class Animal extends AbstractMapElement implements IMapElement, IPositionChangePublisher {
    private final IWorldMap map;
    private MapDirection orientation;
    private final Genotype genotype;
    private final double initialEnergy;
    private  double energy;

    private int daysAlive = 0;
    private int kids = 0;

    private boolean followedAlphaParent = false;
    private boolean followedAlphaChild = false;
    private boolean followed = false;

    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map, Vector2d initialPosition, double initialEnergy){
        this.map = map;
        this.position = initialPosition;
        this.orientation = MapDirection.getRandom();
        this.genotype = new Genotype();
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
    }
    public Animal(IWorldMap map, Vector2d initialPosition, double initialEnergy, Genotype genes){
        this.map = map;
        this.orientation = MapDirection.getRandom();
        this.genotype = genes;
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.position = initialPosition;
    }

    public String toString(){
        return "A";
//        return orientation.toString();
    }
    public String allInfo(){
        return "Animal "
                + "at: "+ position
                + " orient: "+ orientation
                + " NRG: " + energy;
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }


    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver observer: observers)
            observer.positionChanged(element, oldPosition, newPosition);
    }

    @Override
    public void endOfEnergy() {
        if(this.isFollowedAlphaParent())
            AlertBox.display("The alpha parent, you were following at " + (((JungleWorldMap)this.map).getNumber() == 1 ? "left": "right") + " map died at age: "+this.daysAlive);
        ArrayList<IPositionChangeObserver> tmpObservers = new ArrayList<>(observers);
        for(IPositionChangeObserver observer: tmpObservers){
            observer.amimalDied(this);
        }
        this.observers = tmpObservers;
    }

    public void rotate(){
        this.orientation = this.orientation.turn(this.genotype.getRandomGene());
    }
    public void move(){
        Vector2d oldPosition = this.position;
        Vector2d move = orientation.toUnitVector();
        int xCoord,yCoord;
        xCoord = ( position.getX() + move.getX() )% ((JungleWorldMap)(map)).getWidth();
        yCoord = ( position.getY() + move.getY() )% ((JungleWorldMap)(map)).getHeight();
        if(xCoord < 0)
            xCoord += ((JungleWorldMap)(map)).getWidth();
        if(yCoord < 0)
            yCoord += ((JungleWorldMap)(map)).getHeight();
        Vector2d newPosition = new Vector2d(xCoord,yCoord);
        position = newPosition;
        positionChanged(this, oldPosition, newPosition);
        daysAlive += 1;

    }
    public void changeEnergy(double value){
        energy += value;
        ((JungleWorldMap)map).changeTotalEnergy(value);
        if(energy < 0) {
            ((JungleWorldMap)map).changeTotalEnergy(- energy);
            energy = 0;
            endOfEnergy();
        }
    }

    public Animal reproduction(Animal parent2, Vector2d childPos){
        float childEnergy = (float) ((float) (int) (0.25 * parent2.getEnergy()) +(0.25 * this.energy));
        ((JungleWorldMap)map).changeTotalEnergy(childEnergy);
        this.changeEnergy( -(0.25 * this.energy));
        parent2.changeEnergy( -(0.25 * parent2.energy));

        this.addKid();
        parent2.addKid();
        ((JungleWorldMap) map).addKid();

        return new Animal(map, childPos, childEnergy, new Genotype(this.genotype, parent2.genotype));
    }

    public double getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    @Override
    public boolean isFood(){
        return false;
    }

    public boolean isFollowed() {
        return followed;
    }

    public boolean isFollowedAlphaChild() {
        return followedAlphaChild;
    }

    public boolean isFollowedAlphaParent() {
        return followedAlphaParent;
    }

    public void setFollowedAlphaParent(boolean followedAlphaParent) {
        this.followedAlphaParent = followedAlphaParent;
        if(!followedAlphaParent)
            ((JungleWorldMap)this.map).resetFollowing();
    }
    public void setFollowedAlphaChild(boolean followedAlphaChild) {
        this.followedAlphaChild = followedAlphaChild;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
    public void resetAllFollowed(boolean followed) {
        this.followed = followed;
        this.followedAlphaChild = followed;
        this.followedAlphaParent = followed;
    }

    public int getDaysAlive() {
        return daysAlive;
    }
    public int getKids() {
        return kids;
    }
    public void addKid() {
        kids += 1;
    }
}
