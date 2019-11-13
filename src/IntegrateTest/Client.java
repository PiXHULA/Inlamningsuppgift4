package IntegrateTest;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Application implements Runnable {
    String hostName = "127.0.0.1";
    int portNumber = 55555;
    Socket socket = new Socket(hostName, portNumber);
    Stage window;
    Scene scene;
    Label responseFromServer;
    Button connect;
    Button exit;
    TextArea monitor;
    TextField answerField = new TextField();
    BorderPane layout;
    Listener server;
    Thread thread = new Thread(this);
    String text;
    ActionEvent actionEvent = new ActionEvent();

    public Client() throws IOException {
    }

    @Override
    public void start(Stage stage) throws Exception {

        window = stage;
        stage.setTitle("Riddler Client");
        responseFromServer = new Label("Server: ");
        monitor = new TextArea();
        monitor.setEditable(false);
        monitor.setWrapText(true);
        answerField = new TextField();
        answerField.setOnAction(e -> {
            //text = answerField.getText();
            if (!answerField.getText().equals("")) {
                monitor.appendText("User: " + answerField.getText() + "\n");
                if (answerField.getText() != null) {
                    System.out.println("Client: " + answerField.getText());
                    String inputFromUser = answerField.getText();
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(
                                socket.getOutputStream(), true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    out.println(inputFromUser);
                }
            } answerField.clear();});

        layout = new BorderPane();
        layout.setCenter(monitor);
        layout.setBottom(answerField);
        scene = new Scene(layout, 400, 400);
        window.setScene(scene);
        window.show();
        this.thread.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream()));
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                monitor.appendText(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}