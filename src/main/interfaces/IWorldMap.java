package interfaces;

import movement.Vector2d;

public interface IWorldMap {
    //simulation functions

    void placeAnimals(int num);
    void placeGrassPhase();

    void movePhase();
    void eatingPhase();
    void reproductionPhase();

    //simpler functions
    boolean place(IMapElement element);

    boolean isOccupied(Vector2d position);

    boolean canMoveTo(Vector2d position);

    IMapElement objectAt(Vector2d position);


}
