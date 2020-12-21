package mapelements;

import movement.Vector2d;

public class Grass extends AbstractMapElement{

    public Grass (Vector2d position){
        this.position = position;
    }

    public String toString(){
        return "*";
    }

    @Override
    public boolean isFood() {
        return true;
    }
}
