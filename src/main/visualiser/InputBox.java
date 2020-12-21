package visualiser;

import java.awt.event.*;
import javax.swing.*;

class InputBox extends JFrame implements ActionListener{
    static JTextField textField;
    static JFrame frame;
    static JButton button;

    private int output = 0;

    public static final short PREFERED_WIDTH = 250;
    public static final short PREFERED_HEIGHT = 100;

    public InputBox(String title, int x , int y){
        frame = new JFrame(title);
        button = new JButton("Enter");
        textField = new JTextField(10);
        button.addActionListener(this);

        JPanel panel = new JPanel();

        panel.add(textField);
        panel.add(button);

        frame.add(panel);

        frame.setSize(x - PREFERED_WIDTH/2,y - PREFERED_HEIGHT/2);
        frame.setVisible(true);
        frame.setLocation(x,y);
    }
    public int getDate(){
        return output;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(isInt(textField,"aa"))
            output = Integer.parseInt(textField.getText());
    }


    public boolean isInt(JTextField input, String message){
        try{
            int number = Integer.parseInt(input.getText());
            return true;
        }catch (NumberFormatException e){
            AlertBox.display("Wrong data");
            return false;
        }

    }
}