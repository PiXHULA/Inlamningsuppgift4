package newServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(51734);
        System.out.println("Quizkampen : server is running..");
        while (true) {
            try {
                final Socket socketToClient = serverSocket.accept();
                PlayerClientTest player1 = new PlayerClientTest(socketToClient);
                PlayerClientTest player2 = new PlayerClientTest(socketToClient);
                player1.run();
                player2.run();
            } catch (IOException e) {
                System.out.println("Quizkampen : server is terminated or other shit happened..");
                e.printStackTrace();
            }
        }
    }
}