package visualiser;

import map.JungleWorldMap;
import mapelements.Animal;
import mapelements.Grass;
import movement.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapPanel extends JPanel{
    static final short MAP_SIZE = 500;

    static int CELL_HEIGHT;
    static int CELL_WIDTH;

    static final short PADDING_X = 10;
    static final short PADDING_Y = 10;

    static short ANIMAL_PADDING;
    static short GRASS_PADDING;

    JungleWorldMap map;
    Graphics2D graphics2D;

    static final Color STEPPE_COLOR = new Color(255, 222, 82, 255);
    static final Color JUNGLE_COLOR = new Color(18, 92, 26, 255);
    static final Color GRASS_COLOR = new Color(83, 219, 42, 255);

    static final Color DOMINANT_GENOTYPE_COLOR = new Color(255, 103, 2, 255);

    public MapPanel(JungleWorldMap map){
        this.map = map;
        CELL_WIDTH = MAP_SIZE / this.map.getWidth();
        CELL_HEIGHT = MAP_SIZE / this.map.getHeight();

        ANIMAL_PADDING = (short) (Math.min(CELL_HEIGHT,CELL_WIDTH)/8);
        GRASS_PADDING = (short) (Math.min(CELL_HEIGHT,CELL_WIDTH)/8);


        setPreferredSize(new Dimension(map.getWidth() * CELL_WIDTH + 2 * PADDING_X, map.getHeight() * CELL_HEIGHT + 2 * PADDING_Y));
    }

    //main function, which colors everything
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        graphics2D = (Graphics2D)g;

        drawMap();
        drawAnimals();
        drawGrass();
    }

    //functions helpful in finding coords of rectangles to color
    private double xCoord(int x){
        return PADDING_X + x * CELL_WIDTH;
    }
    private double xDifference(int width){
        return CELL_WIDTH * width;
    }
    private double yCoord(int y){
        return PADDING_Y + y * CELL_HEIGHT;
    }
    private double yDifference(int height){
        return CELL_HEIGHT * height;
    }


    private void drawMap(){
//        Rectangle2D steppe = new Rectangle2D.Double(xCoord(0), yCoord(0), wd(main.map.getWidth()), hd(main.map.getHeight()));
        Rectangle2D steppe = new Rectangle2D.Double(xCoord(0), yCoord(0), MAP_SIZE, MAP_SIZE);
        graphics2D.setColor(STEPPE_COLOR);
        graphics2D.fill(steppe);

        Rectangle2D jungle = new Rectangle2D.Double(
                xCoord((map.getWidth() - map.getJungleWidth()) / 2),
                yCoord((map.getHeight() - map.getJungleHeight()) / 2),
                xDifference(map.getJungleWidth()),
                yDifference(map.getJungleHeight()));

        graphics2D.setColor(JUNGLE_COLOR);
        graphics2D.fill(jungle);
    }

    private void drawAnimals(){ for (Animal animal : map.getAnimalsList()) drawAnimal(animal); }

    private void drawGrass(){ for (Grass grass : map.getGrassList()) drawGrass(grass); }

    public Object getAnimalAtCoords(int x, int y){
        Vector2d v = new Vector2d(x,y);
        return map.getAnimalMaxNRGAtPosition(v);
    }

    private void drawAnimal(Animal animal){
        //counting where to draw the animal
        Rectangle2D rectangle = new Rectangle2D.Double(xCoord(animal.getPosition().getX()) + ANIMAL_PADDING,
                yCoord(animal.getPosition().getY()) + ANIMAL_PADDING,
                CELL_WIDTH - ANIMAL_PADDING,
                CELL_HEIGHT - ANIMAL_PADDING);

        //the more moves animal can make in the future, the darker it becomes
        double e = animal.getEnergy();
        double m = map.getMoveEnergy();
        int x = (int) ((int)e / m); // how many moves left
        x *= 5; // scale up
        x = Math.min(x,255);


        if(animal.isFollowedAlphaParent())
            graphics2D.setColor(new Color(67, 0, 139));
        else if(animal.isFollowedAlphaChild())
            graphics2D.setColor(new Color(102, 25, 220));
        else if(animal.isFollowed())
            graphics2D.setColor(new Color(255 - x,255 - x,140));
        else if(map.isFollowDominantGenotype() && map.getDominantGenotype().equals(animal.getGenotype()))
            graphics2D.setColor(DOMINANT_GENOTYPE_COLOR);
        else
            graphics2D.setColor(new Color(255 - x,255 - x,255 - x));
        graphics2D.fill(rectangle);
    }

    private void drawGrass(Grass grass){
        Rectangle2D rectangle = new Rectangle2D.Double(xCoord(grass.getPosition().getX()) + GRASS_PADDING,
                yCoord(grass.getPosition().getY()) + GRASS_PADDING,
                CELL_WIDTH - GRASS_PADDING,
                CELL_HEIGHT - GRASS_PADDING);
        graphics2D.setColor(GRASS_COLOR);
        graphics2D.fill(rectangle);
    }

}