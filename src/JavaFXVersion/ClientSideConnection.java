package JavaFXVersion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;



public class ClientSideConnection {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private int port = 51730;

    int playerID;
    int playerNumber;
    int maxTurns;

    public ClientSideConnection() {
        System.out.println("---CLIENT CONNECTING---");
        try {
            socket = new Socket("localhost", port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            playerID = dataInputStream.readInt();
            playerNumber = dataInputStream.readInt();
            maxTurns = dataInputStream.readInt() / 2;
            System.out.println("MaxTurns:" + maxTurns);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}