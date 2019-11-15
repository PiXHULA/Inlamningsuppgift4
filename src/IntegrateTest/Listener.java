package IntegrateTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(55555);
        while (true) {
            try {
                final Socket socketToClient = serverSocket.accept();
                MultiUserServer player1 = new MultiUserServer(socketToClient);
                MultiUserServer player2 = new MultiUserServer(socketToClient);
                player1.start();
                player2.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
