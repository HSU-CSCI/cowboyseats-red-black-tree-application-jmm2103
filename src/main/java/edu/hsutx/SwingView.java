package edu.hsutx;

import javax.swing.*;
import java.awt.*;

/**
 * SwingView is the graphical user interface (GUI) for the Stadium Seat Reservations system.
 * It extends JFrame and provides a window with components to visualize and interact with the system.
 */
public class SwingView extends JFrame {

    // Constructor to set up the JFrame
    /**
     * Initializes the JFrame with a title, size, layout, and basic UI components.
     * The constructor also sets up a panel for visualizing the Red-Black tree and a control panel for user interactions.
     */
    public SwingView() {
        setTitle("Stadium Seat Reservations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add components for visualization (e.g., tree and stadium layout)
        JPanel treePanel = new JPanel();
        treePanel.setBackground(Color.WHITE); 
        add(treePanel, BorderLayout.CENTER);

        // Add buttons or controls for user interactions (if needed)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        JButton refreshButton = new JButton("Refresh Tree");
        controlPanel.add(refreshButton);
        
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Method to show the GUI
    /**
     * Creates and shows the Swing-based GUI. Initializes the window and makes it visible.
     */
    public void createAndShowGUI() {
        SwingView view = new SwingView();
        view.setVisible(true);
    }

    // Methods to update the GUI based on the state of the model (e.g., refresh the tree display)

    /**
     * Updates the visualization of the Red-Black Tree in the GUI.
     * This method repaints the tree panel to reflect any changes in the tree structure.
     * 
     * @param tree The Red-Black Tree model object that needs to be visualized
     */
    public void updateTreeVisualization(CowboySeatTree tree) {

        JPanel treePanel = (JPanel) getContentPane().getComponent(0); // Assuming the first panel is for the tree visualization
        treePanel.removeAll(); // Remove existing components

        // Re-render the tree (for simplicity, we just use a label here, but you can enhance with more complex rendering)
        JLabel treeLabel = new JLabel("Updated Tree: " + tree.toString()); // Update the display with the tree's string representation
        treePanel.add(treeLabel);

        treePanel.revalidate();
        treePanel.repaint(); // Repaint the panel to reflect the new tree state
    }

    /**
     * Updates the visualization of the stadium seating arrangement.
     * This method can change the colors or layout of seats in the stadium view.
     * 
     * @param start The starting position of the seats to be updated
     * @param length The length of the seat block to be updated
     * @param colored A boolean indicating whether the seats should be colored or not
     */
    public void updateStadiumVisualization(int start, int length, boolean colored) {        
        // Assuming you have a panel for stadium seating (similar to the tree visualization panel)
        JPanel stadiumPanel = new JPanel();
        stadiumPanel.setLayout(new GridLayout(10, 10)); 
        
        // Example: Update a range of seats starting at 'start' for 'length' seats
        for (int i = start; i < start + length; i++) {
            JButton seatButton = new JButton("Seat " + i);
            if (colored) {
                seatButton.setBackground(Color.RED); 
            } else {
                seatButton.setBackground(Color.GREEN);
            }
            stadiumPanel.add(seatButton);
        }

        // Update the main window with the new stadium layout
        getContentPane().removeAll(); // Clear the existing content
        add(stadiumPanel, BorderLayout.CENTER);
        revalidate(); // Refresh the layout
        repaint(); // Repaint the updated window
    }
}