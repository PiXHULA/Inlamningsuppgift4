import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {

    String question;
    String alt1_1;
    String alt1_2;
    String alt1_3;
    String alt1_4;
    String correct;

    public String getQuestion() {
        return question;
    }

    public String getAlt1_1() {
        return alt1_1;
    }

    public String getAlt1_2() {
        return alt1_2;
    }

    public String getAlt1_3() {
        return alt1_3;
    }

    public String getAlt1_4() {
        return alt1_4;
    }

    public String getCorrect() {
        return correct;
    }

    public Protocol() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("C:\\Users\\pedra\\Skrivbord\\YH\\IntelliJDE Project\\OBJP\\Collab\\QuizCollab\\src\\Gaming.properties"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        question = p.getProperty("question1");
        alt1_1 = p.getProperty("alt1.1");
        alt1_2 = p.getProperty("alt1.2");
        alt1_3 = p.getProperty("alt1.3");
        alt1_4 = p.getProperty("alt1.4");
        correct = p.getProperty("correct1");

        System.out.println(question);
    }

    public boolean isAltCorrect(String clientInput){
        if (clientInput.equals(correct)){
            return true;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args) {
        new Protocol();
    }

}