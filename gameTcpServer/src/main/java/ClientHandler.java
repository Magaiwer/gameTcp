import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private static final String password = "freeForAll";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private final Socket clientSocket;
    private ObjectOutputStream objOutput;
    private ObjectInputStream objInput;
    private StateD state;


    public ClientHandler(Socket socket, StateD stateD) {
        this.clientSocket = socket;
        state = stateD;
    }

    @Override
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
        objOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        objInput = new ObjectInputStream(clientSocket.getInputStream());

        String clientIp = clientSocket.getInetAddress().getHostAddress();
        StateD newState = deserialize(objInput);


        if (newState != null) {
            if (newState.getCommands().equals(StateD.Commands.NEW_PLAYER)) {
                System.out.println("Client connect --> " + clientIp);
                newState.getPlayers().add(playerFactory(clientIp));
                assign(newState);
                send();
            } else if (newState.getCommands().equals(StateD.Commands.UPDATE)) {
                assign(newState);
                send();
                System.out.println("Update state!");
            }
        }
    }

    public void assign(StateD newState) {
        newState.getPlayers().forEach(p -> {
            state.removePlayer(p);
            state.addPlayer(p);
        });

        MultiServer.STATE = state;
    }

    public void send() throws IOException {
        objOutput.writeObject(state);
        objOutput.flush();
        objOutput.writeObject(state);
        objOutput.flush();

    }

    public static StateD deserialize(ObjectInputStream objInput) throws IOException, ClassNotFoundException {
        Object obj = null;
        obj = objInput.readObject();
        System.out.println("obj deserialize");
        return (StateD) obj;
    }

    public PlayerServer playerFactory(String player) {
        int x = (int) (Math.random() * WIDTH);
        int y = (int) (Math.random() * HEIGHT);
        player = player + " - " + (int) (Math.random() * HEIGHT);
        return new PlayerServer()
                .id(player)
                .posX(x)
                .posY(y)
                .bot(false)
                .killed(false);
    }
}