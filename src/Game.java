import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;

public class Game extends JPanel {
    private static final String PASSWORD_HARDCODE = "freeForAll";
    Map<String, Player> playersMap;
    Player player = new Player(this);

    int speed = 1;

    public Game() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                player.keyPressed(event);
            }

            @Override
            public void keyReleased(KeyEvent event) {
                player.keyReleased(event);
            }
        });
        setFocusable(true);
    }

    private int getScore() {
        return speed - 1;
    }

    private void move() {
        new Thread(player).start();
    }

    private void paintScore(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Verdana", Font.BOLD, 30));
        g2d.drawString(String.valueOf(getScore()), 10, 30);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.setBackground(Color.black);
        player.paint(g2d);
        intiFire(g2d, player);
        paintScore(g2d);
    }

    public void intiFire(Graphics2D g, Player player) {
        if (player.getBullet() != null) {
            player.getBullet().paint(g);
            player.getBullet().move();
            //new Thread(player.getBullet()).start();
        }
/*        player.getBullets().forEach(bullet -> {
            bullet.paint(g);
            bullet.move();
            // new Thread(bullet).start();
        });*/
    }

    public void createPlayer(Player player) {

    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame("Free For All");
        Game game = new Game();
        frame.add(game);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //ClientTCP client = new ClientTCP();
        //client.connect("127.0.0.1", 12345);
        //System.out.println(client.sendMessage(PASSWORD_HARDCODE));


        while (true) {
            game.move();
            game.repaint();
            Thread.sleep(10);
        }
    }
}