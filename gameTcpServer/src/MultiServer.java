import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

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
        private ObjectOutputStream objOutput;
        private ObjectInputStream objInput;
        private StateD state;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            state = new StateD();
        }


        public void run() {
            try {

                System.out.println("Waiting player connect ...");
                System.out.println("Client connected  " + clientSocket.getInetAddress().getHostName());
                startGame();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void startGame() throws IOException, ClassNotFoundException {
            /*  byte[] objectAsByte = new byte[clientSocket.getReceiveBufferSize()];*/
            objOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objInput = new ObjectInputStream(clientSocket.getInputStream());

            String clientIp = clientSocket.getInetAddress().getHostAddress();
            state = deserialize(objInput);


            if (state != null) {
                if (state.getCommands().equals(StateD.Commands.NEW_PLAYER)) {
                    System.out.println("Client connect --> " + clientIp);
                    state.getPlayers().add(playerFactory(clientIp));
                    objOutput.writeObject(state);
                    objOutput.flush();
                }

            }
            objOutput.close();
            objInput.close();
            System.out.println("Update player!");
        }

        public static StateD deserialize(ObjectInputStream objInput) throws IOException, ClassNotFoundException {
            Object obj = null;
            System.out.println("Archive bis  - - - - > " + objInput);
            obj = objInput.readObject();
            System.out.println("Archive deserialize");
            return (StateD) obj;
        }

/*        public static MultiServer.State deserialize(byte[] objectAsByte) throws IOException, ClassNotFoundException {
            Object obj = null;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectAsByte);
            System.out.println("Archive bis  - - - - > " + objectAsByte.length);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            System.out.println("Archive ois");

            obj = inputStream.readObject();
            System.out.println("Archive deserialize");
            byteArrayInputStream.close();
            inputStream.close();

            return (MultiServer.State) obj;
        }*/


        public void addPlayer() {
            //STATE.put("new-player", players);
        }


        public PlayerServer playerFactory(String player) {
            int x = (int) (Math.random()  * WIDTH);
            int y = (int) (Math.random()  * HEIGHT);
            return new PlayerServer()
                    .id(player)
                    .posX(x)
                    .posY(y)
                    .bot(false)
                    .killed(false);


        }

    }

    public static void main(String[] args) throws IOException {
        MultiServer multiServer = new MultiServer();
        multiServer.start(12345);
    }
}