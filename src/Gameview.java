import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Gameview extends Application implements Runnable{
    Button button;
    Button button2;
    Button button3;
    Button button4;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TextArea quizArea = new TextArea();
        quizArea.setEditable(false);
        quizArea.setFocusTraversable(false);
        quizArea.setWrapText(true);
        button = createButton("Answer1");
        button2 = createButton("Answer2");
        button3 = createButton("Answer3");
        button4 = createButton("Answer4");

        button.setOnAction(getActionEventEventHandler(quizArea, button.getText()));
        button2.setOnAction(getActionEventEventHandler(quizArea, button2.getText()));
        button3.setOnAction(getActionEventEventHandler(quizArea, button3.getText()));
        button4.setOnAction(getActionEventEventHandler(quizArea, button4.getText()));

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(400,600);
        Label player1 = new Label("Player 1");
        player1.setMinSize(5,5);
        Label player2 = new Label("Player 2");
        HBox playerStatus = new HBox(10,player1,player2);
        playerStatus.setSpacing(400);
        playerStatus.setPrefHeight(50);
        GridPane buttonLayout = createButtonLayout();
        borderPane.setTop(playerStatus);
        borderPane.setCenter(quizArea);
        borderPane.setBottom(buttonLayout);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
    private Button createButton(String s){
        Button button = new Button(s);
        button.setMinSize(300.0,150.0);
        return button;
    }
    private GridPane createButtonLayout() {
        GridPane buttonLayout = new GridPane();
        buttonLayout.add(button,0,0);
        buttonLayout.add(button2,1,0);
        buttonLayout.add(button3,0,1);
        buttonLayout.add(button4,1,1);
        return buttonLayout;
    }

    private EventHandler<ActionEvent> getActionEventEventHandler(TextArea quizArea, String s) {
        return actionEvent -> quizArea.appendText("Pushed " + s + "\n");
    }

    @Override
    public void run() {

    }
}
