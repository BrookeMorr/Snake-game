import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int BoardWidth;
    int BoardHeight;
    int tilesize = 25;
    
    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    public SnakeGame(int BoardWidth, int BoardHeight) {
        this.BoardWidth = BoardWidth;
        this.BoardHeight = BoardHeight;
        setPreferredSize(new Dimension(this.BoardWidth, this.BoardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5); // 5,5 default starting position
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10); // 10,10 default starting position
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        gameLoop = new Timer(100, this);
        gameLoop.start() ;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Grid
        for (int i=0; i < BoardWidth/tilesize; i++) {
            g.drawLine(i*tilesize, 0, i * tilesize, BoardHeight);
            g.drawLine(0, i*tilesize, BoardWidth, i*tilesize);
        }

        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tilesize, snakeHead.y * tilesize, tilesize, tilesize, true);

        //Snake Body
        for (int i=0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize, true);
        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize);

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tilesize - 16, tilesize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tilesize - 16, tilesize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(BoardWidth/tilesize); //600/25 = 24, so there is 0 - 24 positions the food will randomly show up in.
        food.y = random.nextInt(BoardHeight/tilesize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        
        //Snake Body
        for (int i = snakeBody.size()-1; i >= 0; i-- ) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snakehead
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i=0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tilesize < 0 || snakeHead.x * tilesize > BoardWidth ||
            snakeHead.y * tilesize < 0 || snakeHead.x * tilesize > BoardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }
    
    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
