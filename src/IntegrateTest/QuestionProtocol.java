package IntegrateTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class QuestionProtocol {
    Random random = new Random();

    Properties p = new Properties();

    FileInputStream[] f = {
            new FileInputStream("src\\Properties\\Gaming.properties"),
            new FileInputStream("src\\Properties\\History.properties"),
            new FileInputStream("src\\Properties\\Sport.properties"),
            new FileInputStream("src\\Properties\\Film.properties")};

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

    public QuestionProtocol() throws IOException {
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
    /*

    private static final int QUESTION = 0;
    private static final int VALIDATION = 1;
    private static final int QUIT = 2;

    private static final int NUMRIDDLES = 5;

    private int state = QUESTION;
    private int currentRiddle = 0;


    public String[] getRiddles() {
        return riddles;
    }

    public String getRiddle(String[] riddles, int i) {
        return riddles[i];
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getAnswer(String[] answers, int i) {
        return answers[i];
    }

    private String[] riddles = {
            "Riddle 1: " + "\nI am not alive, but I grow; I don't have lungs, but I need air; I don't have a mouth, but water kills me. What am I?\n",
            "Riddle 2: " + "\nThe more you take, the more you leave behind. What am I?",
            "Riddle 3: " + "\nWhat room do ghosts avoid?",
            "What belongs to you, but other people use it more than you?",
            "My life can be measured in hours, I serve by being devoured. Thin, I am quick. Fat, I am slow. Wind is my foe. What am I?"
    };
    private String[] answers = {
            "Fire",
            "Footsteps",
            "Living room",
            "Name",
            "Candle"
    };

    public String questionInput(String theInput) {
    String theOutput = "";
        if (state == QUESTION) {
            System.out.println("STARTING WITH FIRST QUESTION");
            theOutput = riddles[currentRiddle];
            state = VALIDATION;
        } else if (state == VALIDATION) {
            if (theInput.equalsIgnoreCase(answers[currentRiddle])) {
                System.out.println("CORRECT ANSWER");
                currentRiddle++;
                theOutput = riddles[currentRiddle];
            }
            state = QUESTION;
        }
            return theOutput;
    }

     */
}
