package visualiser;

import map.JungleWorldMap;
import mapelements.Animal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimulationFrame extends JFrame {
    public static final int PREFERED_WIDTH = 700;
    public static final int PREFERED_HEIGHT = 760;

    public static final Color BACKGROUND_COLOR = new Color(96, 183, 246);
    public static final Color PANEL_BACKGROUND_COLOR = new Color(38, 138, 198);

    private boolean stopped = true;


    private final JPanel mapPanel;

    private final JLabel eraLabel = new JLabel();
    private final JLabel animalLabel = new JLabel();
    private final JLabel grassLabel = new JLabel();
    private final JLabel avEnergyLabel = new JLabel();
    private final JLabel avLifeDaysLabel = new JLabel();
    private final JLabel avChildrenLabel = new JLabel();
    private final JLabel domGenLabel = new JLabel();
    private final JLabel domGenNumLabel = new JLabel();

    public SimulationFrame(JungleWorldMap engine, int number, int x, int y) {
        super("Evolution Simulator - Window number "+ number);
        setPreferredSize(new Dimension(PREFERED_WIDTH,PREFERED_HEIGHT));
        setBackground(BACKGROUND_COLOR);


        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        topPanel.setBackground(PANEL_BACKGROUND_COLOR);

        //start / stop button
        JButton startButton = new JButton();
        startButton.setText(stopped ? "START" : "STOP");
        startButton.addActionListener(e -> {
            stopped = !stopped;
            startButton.setText(stopped ? "START" : "STOP");
        });
        topPanel.add(startButton);
        //gen stats button
//        JButton statsButton = new JButton();
//        statsButton.setText("GENERATE STATS");
//        statsButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int asd = new InputBox("AAA",x + PREFERED_WIDTH/2, y + PREFERED_HEIGHT/2).getDate();
//                System.out.println(asd);
//            }
//        });
//        topPanel.add(statsButton);
        JButton followDomGenButton = new JButton();
        followDomGenButton.setText(!engine.isFollowDominantGenotype() ? "FOLLOW DOM GENOTYPE" : "STOP FOLLOWING");
        followDomGenButton.addActionListener(e -> {
            engine.setFollowDominantGenotype(!engine.isFollowDominantGenotype());
            followDomGenButton.setText(!engine.isFollowDominantGenotype() ? "FOLLOW DOM GENOTYPE" : "STOP FOLLOWING");
        });
        topPanel.add(followDomGenButton);

        getContentPane().add(topPanel, BorderLayout.PAGE_START);

        JPanel statsPanel = new JPanel();
        statsPanel.setPreferredSize(new Dimension(700,150));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        statsPanel.setBackground(PANEL_BACKGROUND_COLOR);
        statsPanel.setLayout(new GridBagLayout());
        JLabel eraText = new JLabel("Current Day: ");
        JLabel animalText = new JLabel("Alive Animals: ");
        JLabel grassText = new JLabel("Grass on Map: ");
        JLabel avEnergyText = new JLabel("Average Energy: ");
        JLabel avLifeDaysText = new JLabel("Average Age: ");
        JLabel avChildrenText = new JLabel("Average Children per Animal: ");
        JLabel domGenText = new JLabel("Dominant genotype: ");
        JLabel domGenNumText = new JLabel("Animals with this genotype: ");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        addLabelToPane(statsPanel,eraText,c,0,0,0);
        addLabelToPane(statsPanel,animalText,c,0,1,0);
        addLabelToPane(statsPanel,grassText,c,0,2,0);
        addLabelToPane(statsPanel,avEnergyText,c,0,3,0);
        addLabelToPane(statsPanel,avLifeDaysText,c,0,4,0);
        addLabelToPane(statsPanel,avChildrenText,c,0,5,0);
        addLabelToPane(statsPanel,domGenText,c,0,6,0);
        addLabelToPane(statsPanel,domGenNumText,c,0,7,0);

        addLabelToPane(statsPanel,eraLabel,c,1,0,430);
        addLabelToPane(statsPanel,animalLabel,c,1,1,0);
        addLabelToPane(statsPanel,grassLabel,c,1,2,0);
        addLabelToPane(statsPanel,avEnergyLabel,c,1,3,0);
        addLabelToPane(statsPanel,avLifeDaysLabel,c,1,4,0);
        addLabelToPane(statsPanel,avChildrenLabel,c,1,5,0);
        addLabelToPane(statsPanel,domGenLabel,c,1,6,0);
        addLabelToPane(statsPanel,domGenNumLabel,c,1,7,0);

        mapPanel = new MapPanel(engine);

        JPanel content = new JPanel();
        content.add(mapPanel);
        content.add(statsPanel);
        content.setBackground(BACKGROUND_COLOR);
        add(content, BorderLayout.CENTER);


        mapPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(stopped) {
                    int x = getXfromClick( e.getX() );
                    int y = getYfromClick( e.getY() );
                    Object obj = ((MapPanel) mapPanel).getAnimalAtCoords(x, y);
                    if (obj instanceof Animal) {
                        AnimalInfoBox.display("Animal",(Animal)obj, (int)getLocation().getX() + PREFERED_WIDTH/2, (int)getLocation().getY() + PREFERED_HEIGHT);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(x, y);
        setVisible(true);
    }

    public JPanel getPanel() {
        return mapPanel;
    }


    public void updateStatistics(String x1, String x2, String x3, String x4, String x5, String x6, String x7, String x8) {
        eraLabel.setText(x1);
        animalLabel.setText(x2);
        grassLabel.setText(x3);
        avEnergyLabel.setText(x4);
        avLifeDaysLabel.setText(x5);
        avChildrenLabel.setText(x6);
        domGenLabel.setText(x7);
        domGenNumLabel.setText(x8);
    }

    public boolean getStopped() {
        return stopped;
    }
    public void setStopped(boolean t) {
        stopped = t;
    }

    private void addLabelToPane(JPanel panel, JLabel label, GridBagConstraints c, int x, int y, int ipadx){
        c.gridx = x;
        c.gridy = y;
        c.ipadx = ipadx;
        panel.add(label,c);
    }

    public int getXfromClick(int x){
        return (x - MapPanel.PADDING_X) / MapPanel.CELL_WIDTH;
    }
    public int getYfromClick(int y){
        return (y - MapPanel.PADDING_Y) / MapPanel.CELL_HEIGHT;
    }
}
