package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;

public abstract class
Canvas extends JPanel implements KeyListener, MouseListener {

    private static boolean[] keyboardState = new boolean[525];

    private static boolean[] mouseState = new boolean[3];
        
    
    public Canvas()
    {
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);

        if(true)
        {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        }
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    public abstract void Draw(Graphics2D g2d) throws IOException, ExecutionException, InterruptedException;
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);
        try {
            Draw(g2d);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean keyboardKeyState(int key)
    {
        return keyboardState[key];
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        keyboardState[e.getKeyCode()] = true;
        keyPressedFramework(e);
    }

    public abstract void keyPressedFramework(KeyEvent e);
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboardState[e.getKeyCode()] = false;
        try {
            keyReleasedFramework(e);
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    public abstract void keyReleasedFramework(KeyEvent e) throws ExecutionException, InterruptedException;

    public static boolean mouseButtonState(int button)
    {
        return mouseState[button - 1];
    }

    private void mouseKeyStatus(MouseEvent e, boolean status)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
            mouseState[0] = status;
        else if(e.getButton() == MouseEvent.BUTTON2)
            mouseState[1] = status;
        else if(e.getButton() == MouseEvent.BUTTON3)
            mouseState[2] = status;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        mouseKeyStatus(e, true);
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        mouseKeyStatus(e, false);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
}
