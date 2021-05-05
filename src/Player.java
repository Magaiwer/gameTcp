import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Runnable {
	private static final int SQUARE = 30;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int UP = 3;
	private static final int DOWN = 4;
	private static final int STOP = 5;
	private static final int SPEED = 3;
	int x = 0;
	int y = 0;
	int vx = 0;
	int vy = 0;
	int direction = 2;


	private final Color color;
	private final Game game;
	private List<Bullet> bullets;
	private Bullet bullet;

	public Player(Game game) {
		this.game= game;

		x = (int) (Math.random() * game.getWidth());
		y = (int) (Math.random() * game.getHeight());
		color = randomColour();
		bullets = new ArrayList<>();
	}

	void move() {
		if (x + vx > 0 && x + vx < game.getWidth() - SQUARE)
			x = x + vx;
		if (y + vy > 0 && y + vy < game.getHeight() - SQUARE)
			y = y + vy;
	}

	public void keyReleased(KeyEvent e) {
		stop();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			vx = -SPEED;
			direction = LEFT;
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			vx = SPEED;
			direction = RIGHT;
		}
		if (keyCode == KeyEvent.VK_UP) {
			vy = -SPEED;
			direction = UP;
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			vy = SPEED;
			direction = DOWN;
		}
		if (keyCode == KeyEvent.VK_SPACE){
			bullets.add(new Bullet(this.game, x + 13, y + 13, direction));
			//bullet = new Bullet(this.game, x + 13, y + 13, direction);
		}
	}

	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, SQUARE, SQUARE);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, SQUARE, SQUARE);
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public Bullet getBullet() {
		return bullet;
	}

	private void stop() {
		vx = 0;
		vy = 0;
	}

	private Color randomColour() {
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);
		return new Color(red,green,blue);
	}


	@Override
	public void run() {
		move();
	}
}