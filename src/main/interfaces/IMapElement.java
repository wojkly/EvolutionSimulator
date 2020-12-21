package interfaces;


import movement.Vector2d;

/**
 * The interface responsible of interaction of main.map elements
 */

public interface IMapElement {

    Vector2d getPosition();

    boolean isFood();
}
