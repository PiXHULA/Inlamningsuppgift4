package JavaFXVersion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Protocol {

    Random random = new Random();

    Properties p = new Properties();

    FileInputStream[] f = {
            new FileInputStream("src\\JavaFXVersion\\Properties\\Gaming.properties"),
            new FileInputStream("src\\JavaFXVersion\\Properties\\History.properties"),
            new FileInputStream("src\\JavaFXVersion\\Properties\\Sport.properties"),
            new FileInputStream("src\\JavaFXVersion\\Properties\\Film.properties")};

    String question;
    String alt1;
    String alt2;
    String alt3;
    String alt4;
    String correct;

    public String getQuestion() {
        return question;
    }

    public String getAlt1() {
        return alt1;
    }

    public String getAlt2() {
        return alt2;
    }

    public String getAlt3() {
        return alt3;
    }

    public String getAlt4() {
        return alt4;
    }

    public String getAnswer() {
        return correct;
    }

    public Protocol() throws IOException {
        newGame();
    }


    public void newGame() {

        int randomNumber = random.nextInt(20 - 16) + 1;

        try {
            p.load(f[randomNumber - 1]);

            question = p.getProperty("question" + randomNumber);
            alt1 = p.getProperty("alt" + randomNumber + ".1");
            alt2 = p.getProperty("alt" + randomNumber + ".2");
            alt3 = p.getProperty("alt" + randomNumber + ".3");
            alt4 = p.getProperty("alt" + randomNumber + ".4");
            correct = p.getProperty("correct" + randomNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Protocol p = new Protocol();
    }
}