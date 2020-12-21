package interfaces;

import mapelements.Animal;
import movement.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition);

    void amimalDied(Animal animal);
}
