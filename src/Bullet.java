import java.awt.*;

/**
 * @author Magaiver Santos
 */
public class Bullet implements Runnable {
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;
    private static final int SPEED = 10;
    private static final int DIAMETER = 5;

    int x;
    int y;
    int direction;
    boolean stop = false;
    private final Game game;

    public Bullet(Game game, int x, int y, int direction) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    void move() {
        switch (this.direction) {
            case UP:
                this.y -= SPEED;
                break;
            case DOWN:
                this.y += SPEED;
                break;
            case RIGHT:
                this.x += SPEED;
                break;
            case LEFT:
                this.x -= SPEED;
                break;
        }
        stop = outScene();
    }

    @Override
    public void run() {
        while (!stop) {
            move();
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }

    public boolean outScene() {
        return ((this.x - DIAMETER > game.getWidth() || this.x + DIAMETER < 0)
                || (this.y - DIAMETER > game.getHeight() || this.y + DIAMETER < 0));
    }
}
