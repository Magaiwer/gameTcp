import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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

    private static class ClientHandler extends Thread {
        private static final String password = "freeForAll";
        private final Socket clientSocket;
        private ObjectOutputStream objectOutputStream;
        private BufferedReader input;
        private final HashMap<String, Boolean> users = new HashMap<String, Boolean>();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                String inputLine;
                while ((inputLine = input.readLine()) != null) {
                    String clientIp = clientSocket.getInetAddress().getHostAddress();

                    if (password.equals(inputLine)) {
                        objectOutputStream.writeChars("Client connect --> " + clientIp);
                        users.put(clientIp, true);
                    } else if (users.get(clientIp) != null) {
                        objectOutputStream.writeChars("Client 1 " );

                        // pegar linha e coluna do tiro e testar se tem alguem
                      // logica




                    } else {
                        objectOutputStream.writeChars("Problema de autenticação");
                    }

                }
                Thread.sleep(2000);
                input.close();
                objectOutputStream.close();
               clientSocket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MultiServer multiServer = new MultiServer();
        multiServer.start(12345);
    }
}