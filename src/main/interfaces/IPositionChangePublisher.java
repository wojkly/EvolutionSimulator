package interfaces;

import movement.Vector2d;

public interface IPositionChangePublisher {
    void addObserver(IPositionChangeObserver observer);

    void removeObserver(IPositionChangeObserver observer);

    void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition);

    void endOfEnergy();
}
