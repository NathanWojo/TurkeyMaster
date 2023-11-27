import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import java.awt.*;

class View extends JPanel {
    JTextField weightField;
    JLabel directionsField;
    JLabel results;

    View(Controller controller) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel directionsPanel = new JPanel();
        directionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel directionsLabel = new JLabel("<html>Input the size in pounds of your turkey. The program will tell you how much oil to put in the fryer, as well as how long you should cook your bird.<html>");
        directionsLabel.setPreferredSize(new Dimension(290, 150));
        directionsPanel.add(directionsLabel);

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JLabel weightLabel = new JLabel("Enter Turkey Weight (lbs):");
        weightField = new JTextField(5);
        weightPanel.add(weightLabel);
        weightPanel.add(weightField);

        add(directionsPanel);
        add(weightPanel);
    }

    public void displayResults(double weight) {
        double oil = weight * 0.2177692308;
        double time = (weight * 3) + 5;

        oil = Double.parseDouble(String.format("%.2f", oil));
        time = Double.parseDouble(String.format("%.1f", time));

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel resultsLabel = new JLabel("<html>Your bird's weight is " + weight + " lbs. This means that (with a standard 30 quart fryer) you should add about " + oil + " gallons of oil, and that you should cook your bird for about " + time + " minutes. Make sure to heat up the oil to about 350 degrees F before dropping the bird, and keep it around that as best as you can while cooking. Always drop slowly, and have someone near just in case! Another easy way of knowing how much oil to add is to test with water. Place your wrapped bird in the fryer and fill it with water until the bird is submerged, and take note of how much water was added.<html>");
        resultsLabel.setPreferredSize(new Dimension(280, 490));
        resultsPanel.add(resultsLabel);

        add(resultsPanel);
        repaint();
        revalidate();
    }
}

class Controller implements ActionListener, KeyListener {
    View view;

    Controller() {}

    void setView(View view) {
        this.view = view;
    }

	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.weightField) {
            String weightText = view.weightField.getText();

            try {
                double rawWeight = Double.parseDouble(weightText);

                if (rawWeight < 0.0) {
                    JOptionPane.showMessageDialog(view, "Please enter a weight greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    view.weightField.setText("");
                    return;
                }
                if (rawWeight > 25.0) {
                    JOptionPane.showMessageDialog(view, "You should not be frying a turkey that big. What kind of fryer are you using?", "Turkey too big", JOptionPane.ERROR_MESSAGE);
                    view.weightField.setText("");
                    return;
                }
                if (rawWeight >= 18.0) {
                    JOptionPane.showMessageDialog(view, "Damn, that is a big ass bird! Feeding the neighborhood?");
                }

                view.removeAll();
                view.displayResults(rawWeight);
            } catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                view.weightField.setText("");
                return;
            }
        }
    }

	public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}

public class TurkeyMaster extends JFrame {
	Controller controller;
	View view;

	public TurkeyMaster() {
		controller = new Controller();
		view = new View(controller);

        controller.setView(view);

		this.setTitle("Turkey Master");
		this.setSize(300, 500);
		this.setFocusable(true);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(view, BorderLayout.CENTER);
		this.addKeyListener(controller);

		view.weightField.addActionListener(controller);

        this.setLayout(new BorderLayout());
        this.add(view, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}

    public static void main(String[] args) {
		TurkeyMaster tm = new TurkeyMaster();
		tm.run();
	}

	public void run() {
        Timer timer = new Timer(16, e -> {
            view.repaint();
            Toolkit.getDefaultToolkit().sync();
        });
        timer.start();
    }
}
