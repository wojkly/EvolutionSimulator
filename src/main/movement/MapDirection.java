package movement;

import java.util.List;
import java.util.Random;

public enum MapDirection {
    NORTH, NORTHEAST, NORTHWEST,
    SOUTH, SOUTHEAST, SOUTHWEST,
    WEST,
    EAST;
    private static final List<MapDirection> posVal = List.of(values());

    public String toString() {
        switch(this){
            case NORTHWEST: return "NW";
            case NORTH: return "N";
            case NORTHEAST: return "NE";
            case EAST: return "E";
            case WEST: return "W";
            case SOUTHWEST: return "SW";
            case SOUTH: return "S";
            case SOUTHEAST: return "SE";
        }
        return null;
    }
    public static MapDirection getRandom(){
        Random rand = new Random();
        return posVal.get(rand.nextInt(8));
    }
    public MapDirection turn(int number){
        int next = 0;
        switch(this){
            case NORTH:
            case NORTHEAST: next += 1;
            case EAST: next += 2;
            case SOUTHEAST: next += 3;
            case SOUTH: next += 4;
            case SOUTHWEST: next += 5;
            case WEST: next += 6;
            case NORTHWEST: next += 7;
        }
        next += number;
        next = next%8;
        switch(next){
            case 1: return NORTHEAST;
            case 2: return EAST;
            case 3: return SOUTHEAST;
            case 4: return SOUTH;
            case 5: return SOUTHWEST;
            case 6: return WEST;
            case 7: return NORTHWEST;
            default: return NORTH;
        }
    }

    public Vector2d toUnitVector() {
        switch(this){
            case EAST: return new Vector2d(1,0);
            case WEST: return new Vector2d(-1,0);
            case NORTH: return new Vector2d(0,1);
            case SOUTH: return new Vector2d(0,-1);
            case NORTHEAST: return new Vector2d(1,1);
            case SOUTHEAST: return new Vector2d(1,-1);
            case NORTHWEST: return new Vector2d(-1,1);
            case SOUTHWEST: return new Vector2d(-1,-1);
        }
        return null;
    }
}
