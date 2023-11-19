
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;

    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 1;

    private int playerPosition = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDirection = -1;
    private int ballYDirection = -2;

    private MapGenerator map;

    public Game(){
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics graphics){
        graphics.setColor(Color.black); //backgrounds
        graphics.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) graphics); //drawing map

        graphics.setColor(Color.yellow); //borders
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(691, 0, 3, 592);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif", Font.BOLD, 25));
        graphics.drawString("" + score, 590, 30);

        graphics.setColor(Color.green); //bar
        graphics.fillRect(playerPosition, 550, 100, 8);

        graphics.setColor(Color.yellow); //ball
        graphics.fillOval(ballPositionX, ballPositionY, 20, 20);

        if(totalBricks <= 0){
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            graphics.setColor(Color.green);
            graphics.setFont(new Font("serif", Font.BOLD, 80));
            graphics.drawString("YOU WON!", 145, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Final Score: " + score, 250, 340);

            graphics.setFont(new Font("serif", Font.BOLD, 15));
            graphics.drawString("Press Enter to Restart", 270, 360);
        }

        if(ballPositionY > 570){
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif", Font.BOLD, 80));
            graphics.drawString("GAME OVER", 100, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Final Score: " + score, 250, 340);

            graphics.setFont(new Font("serif", Font.BOLD, 15));
            graphics.drawString("Press Enter to Restart", 270, 360);
        }
        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerPosition, 550, 100, 8))){
                ballYDirection = -ballYDirection;
            }

            A: for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width){
                                ballXDirection = -ballXDirection;
                            } else {
                                ballYDirection = -ballYDirection;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;
            if(ballPositionX < 0){
                ballXDirection = -ballXDirection;
            }
            if(ballPositionY < 0){
                ballYDirection = -ballYDirection;
            }
            if(ballPositionX > 670){
                ballXDirection = -ballXDirection;
            }
        }




        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerPosition >= 600){
                playerPosition = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerPosition < 10){
                playerPosition = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXDirection = -1;
                ballYDirection = -2;
                playerPosition = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerPosition += 20;
    }

    public void moveLeft(){
        play = true;
        playerPosition -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
