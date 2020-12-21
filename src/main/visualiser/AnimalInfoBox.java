package visualiser;

import map.JungleWorldMap;
import mapelements.Animal;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class AnimalInfoBox {
    public static final Color BACKGROUND_COLOR = new Color(90, 189, 241);
    public static final short PREFERED_WIDTH = 550;
    public static final short PREFERED_HEIGHT = 280;
    private static JPanel panel;
    private static Animal followedAnimal;

    public static void display(String title, Animal animal, int x, int y){
        JFrame frame = new JFrame(title);
        panel = new JPanel();
        followedAnimal = animal;
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JLabel label0 = new JLabel("Quick info about clicked animal");
        label0.setFont(new Font("Verdana", Font.BOLD, 20));

        JLabel posLabel = new JLabel("Animal at coords:   X = "+animal.getPosition().getX()+" and Y = "+animal.getPosition().getY());
        posLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        JLabel label1 = new JLabel("Days Alive:");
        JLabel label2 = new JLabel(String.valueOf(animal.getDaysAlive()));
        JLabel label3 = new JLabel("Genotype:");
        JLabel label4 = new JLabel(animal.getGenotype().toString());
        JLabel label5 = new JLabel("Current energy:");
        JLabel label6 = new JLabel(String.valueOf(animal.getEnergy()));
        JLabel label7 = new JLabel("Children count:");
        JLabel label8 = new JLabel(String.valueOf(animal.getKids()));


        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label0,c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(posLabel,c);

        c.gridwidth = 1;
        addLabelToPane(panel,label1,c,0,2);
        addLabelToPane(panel,label2,c,1,2);
        addLabelToPane(panel,label3,c,0,3);
        addLabelToPane(panel,label4,c,1,3);
        addLabelToPane(panel,label5,c,0,4);
        addLabelToPane(panel,label6,c,1,4);
        addLabelToPane(panel,label7,c,0,5);
        addLabelToPane(panel,label8,c,1,5);

        JButton followButton = new JButton( (!animal.isFollowedAlphaParent() && !animal.isFollowedAlphaChild() && !animal.isFollowed() )  ? "FOLLOW" : "STOP FOLLOWING ALL");
        followButton.addActionListener(e ->{
            if(animal.isAlive()) {
                animal.setFollowedAlphaParent(!animal.isFollowedAlphaParent());
                followButton.setText((!animal.isFollowedAlphaParent() && !animal.isFollowedAlphaChild() && !animal.isFollowed()) ? "FOLLOW" : "STOP FOLLOWING ALL");
            }else if(!animal.isAlive() && (animal.isFollowedAlphaParent() || animal.isFollowedAlphaChild() || animal.isFollowed())){
                animal.resetAllFollowed(false);
                followButton.setText((!animal.isFollowedAlphaParent() && !animal.isFollowedAlphaChild() && !animal.isFollowed()) ? "FOLLOW" : "STOP FOLLOWING ALL");
            }else
                AlertBox.display("You cant follow this animal");

        });
        JButton currentInfoButton = new JButton("CURRENT INFO");
        currentInfoButton.addActionListener(e ->{
            AlertBox.display(animal.isAlive() ?
                    "This animal is currently at pos:\n"+"X: "+animal.getPosition().getX()+"  Y: "+animal.getPosition().getY()+"\n"
                            +"It is "+animal.getDaysAlive()+" days old\n"
                            +"Has currently "+animal.getEnergy()+" energy\n"
                            +"During the time it was followed it had "+((JungleWorldMap)animal.getMap()).getFollowedAlphaChildren()+" children\n"
                            +"And "+((JungleWorldMap)animal.getMap()).getFollowedRelatives()+" other relatives"
                    : "This animal is already dead");
        });
        //add buttons
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        panel.add(followButton,c);
        c.gridy = 7;
        panel.add(currentInfoButton,c);
        JLabel helpLabel1 = new JLabel("*To stop following animals you have to click on any of them");
        JLabel helpLabel2 = new JLabel("and press the \"STOP FOLLOWING\" button");
        helpLabel1.setFont(new Font("Verdana", Font.ITALIC, 12));
        helpLabel2.setFont(new Font("Verdana", Font.ITALIC, 12));
        c.gridy = 8;
        panel.add(helpLabel1,c);
        c.gridy = 9;
        panel.add(helpLabel2,c);

        panel.setBackground(BACKGROUND_COLOR);
        frame.add(panel);
        frame.setSize(new Dimension(PREFERED_WIDTH,PREFERED_HEIGHT));
        frame.setLocation(x - PREFERED_WIDTH/2, y);
        frame.setVisible(true);
    }

    public static void addLabelToPane(JPanel panel,JLabel label, GridBagConstraints c, int x, int y){
        c.gridx = x;
        c.gridy = y;
        panel.add(label,c);
    }
}
