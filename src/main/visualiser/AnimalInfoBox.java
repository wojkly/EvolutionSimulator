package visualiser;

import mapelements.Animal;

import javax.swing.*;

import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class AnimalInfoBox {
    public static final Color BACKGROUND_COLOR = new Color(90, 189, 241);
    public static final short PREFERED_WIDTH = 550;
    public static final short PREFERED_HEIGHT = 250;
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
//        JButton followThisButton = new JButton("STOP FOLLOWING THIS");
        followButton.addActionListener(e ->{
            animal.setFollowedAlphaParent(!animal.isFollowedAlphaParent());
            followButton.setText( (!animal.isFollowedAlphaParent() && !animal.isFollowedAlphaChild() && !animal.isFollowed() )   ? "FOLLOW" : "STOP FOLLOWING ALL");
        });
//        followThisButton.addActionListener(e ->{
//            animal.setOtherFollowed(false);
//            System.out.println(animal.isFollowed() ? " " : "STOP FOLLOWING THIS");
//            followThisButton.setText("STOP FOLLOWING THIS");
//            followButton.setText("FOLLOW");
//        });

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        panel.add(followButton,c);
//        c.gridwidth = 2;
//        c.gridx = 0;
//        c.gridy = 7;
//        panel.add(followThisButton,c);

        panel.setBackground(BACKGROUND_COLOR);
        frame.add(panel);
        frame.setSize(new Dimension(PREFERED_WIDTH,PREFERED_HEIGHT));
        frame.setLocation(x - PREFERED_WIDTH/2, y);
        frame.setVisible(true);
    }
//    public static void display(String title, Animal animal, int x, int y){
//        JFrame frame = new JFrame(title);
//        panel = new JPanel();
//        followedAnimal = animal;
//        panel.setLayout(new GridBagLayout());
//
//        GridBagConstraints c = new GridBagConstraints();
//
//        JLabel label0 = new JLabel("Quick info about clicked animal");
//        label0.setFont(new Font("Verdana", Font.BOLD, 20));
//
//        JLabel posLabel = new JLabel("Animal at coords:   X = "+animal.getPosition().getX()+" and Y = "+animal.getPosition().getY());
//        posLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
//
//        JLabel label1 = new JLabel("Days Alive:");
//        JLabel label2 = new JLabel(String.valueOf(animal.getDaysAlive()));
//        JLabel label3 = new JLabel("Genotype:");
//        JLabel label4 = new JLabel(animal.getGenotype().toString());
//        JLabel label5 = new JLabel("Current energy:");
//        JLabel label6 = new JLabel(String.valueOf(animal.getEnergy()));
//        JLabel label7 = new JLabel("Children count:");
//        JLabel label8 = new JLabel(String.valueOf(animal.getKids()));
//
//
//        c.gridwidth = 2;
//        c.gridx = 0;
//        c.gridy = 0;
//        panel.add(label0,c);
//        c.gridx = 0;
//        c.gridy = 1;
//        panel.add(posLabel,c);
//
//        c.gridwidth = 1;
//        addLabelToPane(panel,label1,c,0,2);
//        addLabelToPane(panel,label2,c,1,2);
//        addLabelToPane(panel,label3,c,0,3);
//        addLabelToPane(panel,label4,c,1,3);
//        addLabelToPane(panel,label5,c,0,4);
//        addLabelToPane(panel,label6,c,1,4);
//        addLabelToPane(panel,label7,c,0,5);
//        addLabelToPane(panel,label8,c,1,5);
//
//        JButton followButton = new JButton(!animal.isFollowed() ? "FOLLOW" : "STOP FOLLOWING");
//        followButton.addActionListener(e ->{
//            animal.setFollowed(!animal.isFollowed());
//            followButton.setText(!animal.isFollowed() ? "FOLLOW" : "STOP FOLLOWING");
//        });
//
//        c.gridwidth = 2;
//        c.gridx = 0;
//        c.gridy = 6;
//        panel.add(followButton,c);
//
//        panel.setBackground(BACKGROUND_COLOR);
//        frame.add(panel);
//        frame.setSize(new Dimension(PREFERED_WIDTH,PREFERED_HEIGHT));
//        frame.setLocation(x - PREFERED_WIDTH/2, y);
//        frame.setVisible(true);
//    }
    public void refresh(){
        JLabel kidsLabel = new JLabel(String.valueOf(followedAnimal.getKids()));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        panel.add(kidsLabel,c);
    }

    public static void addLabelToPane(JPanel panel,JLabel label, GridBagConstraints c, int x, int y){
        c.gridx = x;
        c.gridy = y;
        panel.add(label,c);
    }
}
