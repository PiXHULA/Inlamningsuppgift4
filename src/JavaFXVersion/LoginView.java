package JavaFXVersion;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginView extends Application {
    String playerName = "";
    Stage playerNameWindow;
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static void main(String[] args) {
        launch(args);
        LoginView g = new LoginView();
        System.out.println(g.getPlayerName());
    }

    @Override
    public void start(Stage stage) throws Exception {
        //login screen. Username input > transfer username to "player1 / player " in gameview.
        GridPane loginView = new GridPane();
        loginView.setMinSize(400,600);
        TextField loginName = new TextField("Enter name");
        Button submit = new Button ("Submit");
        submit.setOnAction(actionEvent -> {
            setPlayerName(loginName.getText());
                //stage.close();
        });
        loginName.setAlignment(Pos.CENTER);
        loginView.add(loginName, 0,0);
        loginView.add(submit, 0,1);
        Scene scene = new Scene(loginView);
        scene.getStylesheets().add("JavaFXVersion/GameviewStyle.css");
        stage.setScene(scene);
        stage.show();
    }
}
