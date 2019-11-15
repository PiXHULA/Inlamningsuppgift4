package newServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class GameServerTest extends Thread{


    private final Socket clientSocket;

    public GameServerTest(Socket socketToClient) throws IOException {
        this.clientSocket = socketToClient;
    }

    public void run() {
        try (
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine, outPutLine;
            //Initiate conversation with client
            //outPutLine = prot.processInput(null);
            //out.println("Riddler: " + outPutLine);
            ProtocolTest prot = new ProtocolTest();
            outPutLine = prot.getQuestion(null);
            out.println(outPutLine);
            while ((inputLine = in.readLine()) != null) {
                outPutLine = prot.getQuestion(inputLine);
                out.println(outPutLine);
                if (outPutLine.equalsIgnoreCase("Bye"))
                    break;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Player disconnected from server..");
        }
    }
}

