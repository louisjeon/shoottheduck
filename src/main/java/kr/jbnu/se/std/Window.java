package kr.jbnu.se.std;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 */

public class Window extends JFrame{
        
    private Window() throws IOException, ExecutionException, InterruptedException {
        // Sets the title for this frame.
        this.setTitle("Shoot the duck");
        
        // Sets size of the frame.
        if(false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(MAXIMIZED_BOTH);
        }
        else // kr.jbnu.se.std.Window mode
        {
            // Size of the frame.
            this.setSize(1200, 900);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }
        
        // Exit the application when user close frame.
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        this.setContentPane(new Framework());
        
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Window();
                } catch (IOException | ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

//new