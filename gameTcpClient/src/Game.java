import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Game extends JPanel {
    private static final String PASSWORD_HARDCODE = "freeForAll";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    Player player = new Player(this);
    StateD stateD = new StateD();

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
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Verdana", Font.BOLD, 12));
        g2d.drawString("Players connected", 10, 20);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Verdana", Font.BOLD, 10));
        g2d.drawString("192.168.0.1", 10, 40);
        g2d.drawString("192.168.0.2", 10, 60);
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
/*        if (player.getBullet() != null) {
            player.getBullet().paint(g);
            player.getBullet().move();
            //new Thread(player.getBullet()).start();
        }*/
        player.getBullets().forEach(bullet -> {
            bullet.paint(g);
            bullet.move();
            // new Thread(bullet).start();
        });
    }

    public void createPlayer(StateD stateD) {
        this.stateD = stateD;
        stateD.getPlayers().forEach(p -> {
            player = new Player(this);
            player.setPosX(p.getPosX());
            player.setPosY(p.getPosY());
            new Thread(player).start();
        });
    }

    public void startGame() {
        this.stateD.setCommands(StateD.Commands.NEW_PLAYER);
/*        PlayerServer playerServer = new PlayerServer();
        playerServer.posX(player.getPosX());
        playerServer.posY(player.getPosY());

        this.stateD.getPlayers().add(playerServer);*/
        //this.createPlayer(stateD);
    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public StateD getStateD() {
        return stateD;
    }

    public void setStateD(StateD stateD) {
        this.stateD = stateD;
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

        ClientTCP client = new ClientTCP();
        client.connect("127.0.0.1", 12345);

        game.startGame();
        StateD stateD = (StateD) client.send(game.getStateD());
        game.createPlayer(stateD);
        client.disconnect();
        //System.out.println(client.sendMessage(PASSWORD_HARDCODE));


        while (true) {
            game.move();
            game.repaint();
            Thread.sleep(10);
        }
    }
}