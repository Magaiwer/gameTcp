import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 12345;
    Player player = new Player(this);
    StateD stateD = new StateD();
    ClientTCP client = new ClientTCP();
    Map<String, Player> playersMap = new HashMap<>();

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

    private void paintScore(Graphics2D g2d) {
        int height = 40;
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Verdana", Font.BOLD, 12));
        g2d.drawString("Players connected", 10, 20);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Verdana", Font.BOLD, 10));

        if (stateD != null) {
            for (PlayerServer p : stateD.getPlayers()) {
                g2d.drawString(p.getPlayer(), 10, height);
                height += (height / 2);
            }
        }
        g2d.setColor(Color.RED);
        for (int i = 0; i < WIDTH; i++) {
            g2d.drawString("-", i, 100);

        }
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
        move();
        //createPlayer();
        qualqUercoisa();

    }

    public void move() {
        new Thread(player).start();
    }

    public void intiFire(Graphics2D g, Player player) {
        player.getBullets().forEach(bullet -> {
            bullet.paint(g);
            bullet.move();
            // new Thread(bullet).start();
        });
    }

    public void update() {
        try {
            client.connect(ADDRESS, PORT);
            stateD = (StateD) client.send(this.stateD);
            client.disconnect();
            createPlayer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void qualqUercoisa() {
        stateD.setCommands(StateD.Commands.UPDATE);
        update();
    }

    public void createPlayer() {
        stateD.getPlayers().forEach(p -> {

            if (!playersMap.containsKey(p.getPlayer())) {

                if (!p.getPlayer().equals(client.getIpAddress())) {
                    player = new Player(this);
                }
                player.setPosX(p.getPosX());
                player.setPosY(p.getPosY());
                new Thread(player).start();

                playersMap.put(p.getPlayer(), player);
            }
        });
    }

    public void startGame() {
        this.stateD.setCommands(StateD.Commands.NEW_PLAYER);
        update();
    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        JFrame frame = new JFrame("Free For All");
        Game game = new Game();
        frame.add(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(game);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.startGame();

        while (true) {
            game.repaint();
            Thread.sleep(10);
        }
    }
}