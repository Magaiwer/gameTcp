import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {
    private ServerSocket serverSocket;
    public static StateD STATE = new StateD();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), STATE);
            new Thread(clientHandler).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        MultiServer multiServer = new MultiServer();
        multiServer.start(12345);
    }
}