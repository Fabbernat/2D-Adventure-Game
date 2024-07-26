package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile.tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile.tile
    final int maxScreenCol = 16;
    final int maxScreendRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreendRow; // 576 pixels

    // FPS
    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpreed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    /** non-delta method
     *
     */
    /*
    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            // UPDATE: update information such as character positions
            update();
            // 2 DRAW: draw the screen based on the uptaded info
            repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; // 1 million

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }*/

    /** Delta method run()
     *
     */
    public void run() {
        double drawInterval = (double) 1000000000 / FPS; // one billion
        double delta = 0;
        long lastTIme = System.nanoTime();
        long currentTIme;
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {

            currentTIme = System.nanoTime();

            delta += (currentTIme - lastTIme) / drawInterval;

            timer += (currentTIme - lastTIme);

            lastTIme = currentTIme;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer>=1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponenet(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2);
        player.draw(g2);
        g2.dispose();
    }
}
