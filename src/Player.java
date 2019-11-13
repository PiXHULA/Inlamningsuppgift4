import java.net.*;
import java.io.*;

public class Player {

    private ClientSideConnection csc;
    private int port = 51734;
    private int playerID;
    private int otherPlayer; //Control int so u can set "rules" later
    private int myPoints; // so you can store turn points for yourself
    private int myTotalPoints; // so you can store your totalpoints
    private int enemyPoints; // so you can store points for enenmy
    private int enemyTotalPoints; // so you can store your enemy totalpoints
    private int turn; //so you can see / store points at diffrent "turns" of the game
    private boolean buttonsEnable; //so you can disable the buttons if its not your turn (if we want)

    

    /**
     * somthing like this plus you set "buttonesEnable to true or false depending if its your turn or not
     * public void togglebuttons(){
     *     button1.setEnable(buttonsEnable);
     *     button2.setEnable(buttonsEnable);
     *     button3.setEnable(buttonsEnable);
     *     button4.setEnable(buttonsEnable);
     * }
     */


    //method to call and run CSC
    public void connectToServer(){
        csc = new ClientSideConnection();
    }

    //the "logic" for client connection to server
    private class ClientSideConnection {
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public ClientSideConnection (){
            System.out.println("--CLIENT CONNECTING--");
            try{
                socket = new Socket("localhost", port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                System.out.println("Connected to server as player #" + playerID + ".");
            }catch (IOException ex){
                System.out.println("IOException from CSC constructor");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Player p = new Player();
        p.connectToServer();
        //p.userGameViewGui();

    }

}
