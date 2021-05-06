import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTCP {
    private Socket clientSocket;
    private ObjectOutputStream objOutput;
    private ObjectInputStream objInput;
    private String ipAddress;

    public void connect(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        objOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        objInput = new ObjectInputStream(clientSocket.getInputStream());
        this.ipAddress = clientSocket.getInetAddress().getHostAddress();
    }

    public Object send(Object object) throws IOException, ClassNotFoundException {
        objOutput.writeObject(object);
        objOutput.flush();
        return objInput.readObject();
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void disconnect() throws IOException {
        objInput.close();
        objOutput.close();
        clientSocket.close();
    }
}