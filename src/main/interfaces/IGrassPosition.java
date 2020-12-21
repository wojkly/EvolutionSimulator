package interfaces;

import movement.Vector2d;

import java.util.Optional;

public interface IGrassPosition {

    Optional<Vector2d> getStepPosition();
    Optional<Vector2d> getJunglePosition();

    void delFreeStepSpace(Vector2d pos);
    void delFreeJungleSpace(Vector2d pos);

    void addFreeStepSpace(Vector2d pos);
    void addFreeJungleSpace(Vector2d pos);

}
