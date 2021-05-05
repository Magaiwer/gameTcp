import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MultiServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static class ClientHandler extends Thread {
        private static final String password = "freeForAll";
        private static final int WIDTH = 800;
        private static final int HEIGHT = 800;
        private final Socket clientSocket;
        private ObjectOutputStream objectOutputStream;
        private BufferedReader input;
        private HashMap<String, Set<Player>> STATE = new HashMap<>();
        private Set<Player> players = new HashSet<>();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }


        public void run() {
            try {

                System.out.println("Waiting player connect ...");
                System.out.println("Client connected  " + clientSocket.getInetAddress().getHostName());
                startGame();

            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void startGame() throws IOException, InterruptedException, ClassNotFoundException {
            byte[] objectAsByte = new byte[clientSocket.getReceiveBufferSize()];
            BufferedInputStream bf = new BufferedInputStream(clientSocket.getInputStream());

            int read = 0;
            int totalRead = 0;
            int remaining = objectAsByte.length;

            while ((read = bf.read(objectAsByte , 0,objectAsByte.length)) > 0) {
                totalRead += read;
                remaining -= read;
                System.out.println("read " + totalRead + " bytes.");

                String clientIp = clientSocket.getInetAddress().getHostAddress();

                STATE = deserialize(objectAsByte);


                if (STATE.containsKey(Commands.NEW_PLAYER.toString())) {

           /*         if (password.equals("")) {
                        objectOutputStream.writeChars("Client connect --> " + clientIp);
                        players.add(playerFactory(clientIp));

                    } else {
                        objectOutputStream.writeChars("Problema de autenticação");
                    }*/

                }

            }
        }

        public static HashMap<String, Set<Player>> deserialize(byte[] objectAsByte) throws IOException, ClassNotFoundException {
            Object obj = null;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectAsByte);
            System.out.println("Archive bis  - - - - > " + objectAsByte.length);
            ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream);
            System.out.println("Archive ois");

            obj = objectInputStream.readObject();
            System.out.println("Archive deserialize");
            byteArrayInputStream.close();
            objectInputStream.close();

            return (HashMap<String, Set<Player>>) obj;
        }

        public void addPlayer() {
            STATE.put("new-player", players);
        }


        public Player playerFactory(String player) {
            Random random = new Random();
            return new Player()
                    .player(player)
                    .posX(random.nextInt() * WIDTH)
                    .posY(random.nextInt() * HEIGHT)
                    .bot(false)
                    .killed(false);
        }

        private byte[] serialize() {
            try {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                ObjectOutputStream ous;
                ous = new ObjectOutputStream(bao);
                ous.writeObject(this);
                return bao.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void sendFile(Socket socket) throws IOException {
            BufferedOutputStream bf = new BufferedOutputStream(socket.getOutputStream());

            byte[] byteSerialized = serialize();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteSerialized);
            byte[] buffer = new byte[byteSerialized.length];

            int count;
            while ((count = inputStream.read(buffer, 0, buffer.length)) != -1) {
                bf.write(buffer, 0, count);
                System.out.println("enviando" + count);
            }
            System.out.println("Arquivo enviado");
            bf.flush();
            bf.close();
        }
    }

    private static enum Commands {
        NEW_PLAYER,
        KILL_PLAYER,
        NEW_BOT,

    }

    public static class Player implements Serializable {
        private String player;
        private String password;
        private int posX;
        private int posY;
        private boolean killed = false;
        private boolean bot = false;

        public String getPlayer() {
            return player;
        }

        public Player player(String player) {
            this.player = player;
            return this;
        }

        public int getPosX() {
            return posX;
        }

        public Player posX(int posX) {
            this.posX = posX;
            return this;
        }

        public int getPosY() {
            return posY;
        }

        public Player posY(int posY) {
            this.posY = posY;
            return this;
        }

        public boolean isKilled() {
            return killed;
        }

        public Player killed(boolean killed) {
            this.killed = killed;
            return this;
        }

        public boolean isBot() {
            return bot;
        }

        public Player bot(boolean bot) {
            this.bot = bot;
            return this;
        }
    }

    public static void main(String[] args) throws IOException {
        MultiServer multiServer = new MultiServer();
        multiServer.start(12345);
    }
}