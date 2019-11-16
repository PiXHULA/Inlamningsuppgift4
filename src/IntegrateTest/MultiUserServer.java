package IntegrateTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiUserServer extends Thread{

    private Socket clientSocket;

    public MultiUserServer(Socket socketToClient) throws IOException{
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

            QuestionProtocol prot = new QuestionProtocol();
/*
            outPutLine = prot.questionInput("HEJ");
            out.println(outPutLine);

            while ((inputLine = in.readLine()) != null) {
                outPutLine = prot.questionInput(inputLine);
                out.println(outPutLine);
                if (outPutLine.equalsIgnoreCase("Bye"))
                    break;
            }
 */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

