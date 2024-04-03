import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int BoardWidth = 600;
        int BoardHeight = BoardWidth; 

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(BoardWidth, BoardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // creates the window that the game will be run in (above code)

        SnakeGame snakeGame = new SnakeGame(BoardWidth, BoardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
