package map;

import interfaces.IGrassPosition;
import movement.Vector2d;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class GrassPosition implements IGrassPosition {
    //sets contain spaces WITHOUT grass
    private Set<Vector2d> jungleGrass;
    private Set<Vector2d> stepGrass;
    private JungleWorldMap map;

    private Vector2d lLjungle;
    private Vector2d uRjungle;

    GrassPosition(JungleWorldMap map, Vector2d lL, Vector2d uR, Vector2d lLjungle, Vector2d uRjungle){
        jungleGrass = new HashSet<>();
        stepGrass = new HashSet<>();

        this.lLjungle = lLjungle;
        this.uRjungle = uRjungle;

        this.map = map;
        initialFreeSpaces(lL, uR, lLjungle, uRjungle);
    }

    private void initialFreeSpaces(Vector2d lL, Vector2d uR, Vector2d lLjungle, Vector2d uRjungle){
        //firstly we initialize jungle set
        for(int i=lLjungle.getX(); i<= uRjungle.getX(); i++)
            for(int j= lLjungle.getY(); j<= uRjungle.getY(); j++)
                jungleGrass.add(new Vector2d(i,j));
        //secondly step set
        for(int i= lL.getX(); i<= uR.getX(); i++)
            for(int j= lL.getY(); j<= uR.getY(); j++) {
                Vector2d v = new Vector2d(i, j);
                if(!jungleGrass.contains(v))
                    stepGrass.add(v);
            }
    }

    @Override
    public Optional<Vector2d> getStepPosition() {
        if(!stepGrass.isEmpty()){
            int item = new Random().nextInt(stepGrass.size());
            Object[] positions = stepGrass.toArray();
            Vector2d v = (Vector2d) positions[item];
            return Optional.of(v);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vector2d> getJunglePosition() {
        if(!jungleGrass.isEmpty()){
            int item = new Random().nextInt(jungleGrass.size());
            Object[] positions = jungleGrass.toArray();
            Vector2d v = (Vector2d) positions[item];
            return Optional.of(v);
        }
        return Optional.empty();
    }

    @Override
    public void delFreeStepSpace(Vector2d pos) {
        stepGrass.remove(pos);
    }
    @Override
    public void delFreeJungleSpace(Vector2d pos) {
        jungleGrass.remove(pos);
    }
    @Override
    public void addFreeStepSpace(Vector2d pos) {
        stepGrass.add(pos);
    }
    @Override
    public void addFreeJungleSpace(Vector2d pos) {
        jungleGrass.add(pos);
    }

    public void addFreeSpace(Vector2d pos){
        if(pos.follows(lLjungle) && pos.precedes(uRjungle)){
            addFreeJungleSpace(pos);
        }else{
            addFreeStepSpace(pos);
        }
    }
}
