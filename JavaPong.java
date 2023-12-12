import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JavaPong extends JFrame implements Runnable {
    private static final int WIDTH = 984;
    private static final int HEIGHT = 728;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 120;
    private static final int BALL_SIZE = 15;
    private static final int BALL_SPEED = 1;
    private static final int TIMER_DELAY = 10;

    private Paddle paddle;
    private Ball ball;

    private Image bufferImage;
    private Graphics bufferGraphics;

    private boolean gameStarted = false;

    public JavaPong() {
        setTitle("Pong Game");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        paddle = new Paddle();
        ball = new Ball();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                paddle.setY(e.getY() - PADDLE_HEIGHT / 2);
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'p') {
                    if (!gameStarted) {
                        // Start a new game on 'p' press
                        resetGame();
                        gameStarted = true;
                    }
                }
            }
        });

        Timer timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted) {
                    update();
                    repaint();
                }
            }
        });
        timer.start();

        Thread gameThread = new Thread(this);
        gameThread.start();

        // Center the window after components are added
        centerWindow();
    }

    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - WIDTH) / 2;
        int y = (screenSize.height - HEIGHT) / 2;
        setLocation(x, y);
        setSize(WIDTH, HEIGHT);
    }

    private void update() {
        ball.move();

        // Ball collisions with walls
        if (ball.getY() <= 0 || ball.getY() >= HEIGHT - BALL_SIZE) {
            ball.reverseYDirection();
        }

        // Ball collisions with paddles
        if (ball.intersects(paddle) || ball.intersectsOpposite(paddle)) {
            ball.reverseXDirection();
        }

        // Game over condition
        if (ball.getX() <= 0 || ball.getX() >= WIDTH - BALL_SIZE) {
            gameStarted = false;
        }
    }

    @Override
    public void paint(Graphics g) {
        if (bufferImage == null) {
            bufferImage = createImage(getWidth(), getHeight());
            bufferGraphics = bufferImage.getGraphics();
        }

        // Draw to the off-screen buffer
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        paddle.draw(bufferGraphics);
        ball.draw(bufferGraphics);

        if (!gameStarted) {
            // Draw the message when the game has not started
            bufferGraphics.setColor(Color.BLUE);
            bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 24));
            String message = "Press 'P' to Play";
            int messageWidth = bufferGraphics.getFontMetrics().stringWidth(message);
            bufferGraphics.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 3);
        }

        // Draw the off-screen buffer to the screen
        g.drawImage(bufferImage, 0, 0, this);
    }

    private void resetGame() {
        paddle.setY(HEIGHT / 2 - PADDLE_HEIGHT / 2);
        ball = new Ball();
    }

    @Override
    public void run() {
        while (true) {
            if (gameStarted) {
                update();
                repaint();
            }
            try {
                Thread.sleep(TIMER_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JavaPong game = new JavaPong();
            game.setVisible(true);
        });
    }

    private static class Paddle {
        private int y;

        public void setY(int y) {
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(PADDLE_WIDTH, getY(), PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(WIDTH - PADDLE_WIDTH * 2, getY(), PADDLE_WIDTH, PADDLE_HEIGHT);
        }
    }

    private static class Ball {
        private int x;
        private int y;
        private int speedX = BALL_SPEED;
        private int speedY = BALL_SPEED;

        public Ball() {
            x = WIDTH / 2 - BALL_SIZE / 2;
            y = HEIGHT / 2 - BALL_SIZE / 2;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void move() {
            x += speedX;
            y += speedY;
        }

        public void reverseXDirection() {
            speedX = -speedX;
        }

        public void reverseYDirection() {
            speedY = -speedY;
        }

        public boolean intersects(Paddle paddle) {
            return (x <= PADDLE_WIDTH && y + BALL_SIZE >= paddle.getY() && y <= paddle.getY() + PADDLE_HEIGHT);
        }

        public boolean intersectsOpposite(Paddle paddle) {
            return (x >= WIDTH - PADDLE_WIDTH * 2 - BALL_SIZE && y + BALL_SIZE >= paddle.getY() && y <= paddle.getY() + PADDLE_HEIGHT);
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, BALL_SIZE, BALL_SIZE);
            g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        }
    }
}
