package SwingVersion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Protocol {

    String[] sortedGamingQuestions;
    String[] sortedGamingAnswers;
    String[] sortedGamingAlts1;
    String[] sortedGamingAlts2;
    String[] sortedGamingAlts3;
    String[] sortedGamingAlts4;
    String[] gamingAnswers = new String[1];
    String[] gamingQuestions = new String[1];
    String[] gamingAlts1 = new String[1];
    String[] gamingAlts2 = new String[1];
    String[] gamingAlts3 = new String[1];
    String[] gamingAlts4 = new String[1];

    String[] sortedFilmQuestions;
    String[] sortedFilmAnswers;
    String[] sortedFilmAlts1;
    String[] sortedFilmAlts2;
    String[] sortedFilmAlts3;
    String[] sortedFilmAlts4;
    String[] filmAnswers = new String[1];
    String[] filmQuestions = new String[1];
    String[] filmAlts1 = new String[1];
    String[] filmAlts2 = new String[1];
    String[] filmAlts3 = new String[1];
    String[] filmAlts4 = new String[1];

    String[] sortedHistoryQuestions;
    String[] sortedHistoryAnswers;
    String[] sortedHistoryAlts1;
    String[] sortedHistoryAlts2;
    String[] sortedHistoryAlts3;
    String[] sortedHistoryAlts4;
    String[] historyAnswers = new String[1];
    String[] historyQuestions = new String[1];
    String[] historyAlts1 = new String[1];
    String[] historyAlts2 = new String[1];
    String[] historyAlts3 = new String[1];
    String[] historyAlts4 = new String[1];

    String[] sortedSportQuestions;
    String[] sortedSportAnswers;
    String[] sortedSportAlts1;
    String[] sortedSportAlts2;
    String[] sortedSportAlts3;
    String[] sortedSportAlts4;
    String[] sportAnswers = new String[1];
    String[] sportQuestions = new String[1];
    String[] sportAlts1 = new String[1];
    String[] sportAlts2 = new String[1];
    String[] sportAlts3 = new String[1];
    String[] sportAlts4 = new String[1];

    String tempAnswer = "";
    String tempQuestion = "";
    String tempAlt1 = "";
    String tempAlt2 = "";
    String tempAlt3 = "";
    String tempAlt4 = "";



    Properties p = new Properties();

    String[] properties =
            {"src\\Gaming.properties", "src\\History.properties", "src\\Sport.properties", "src\\Film.properties"};
    String question;
    String alt1;
    String alt2;
    String alt3;
    String alt4;
    String correct;

    public String[] getSortedGamingQuestions() {
        return sortedGamingQuestions;
    }

    public String[] getSortedGamingAnswers() {
        return sortedGamingAnswers;
    }

    public String[] getSortedGamingAlts1() {
        return sortedGamingAlts1;
    }

    public String[] getSortedGamingAlts2() {
        return sortedGamingAlts2;
    }

    public String[] getSortedGamingAlts3() {
        return sortedGamingAlts3;
    }

    public String[] getSortedGamingAlts4() {
        return sortedGamingAlts4;
    }

    public String[] getSortedFilmQuestions() {
        return sortedFilmQuestions;
    }

    public String[] getSortedFilmAnswers() {
        return sortedFilmAnswers;
    }

    public String[] getSortedFilmAlts1() {
        return sortedFilmAlts1;
    }

    public String[] getSortedFilmAlts2() {
        return sortedFilmAlts2;
    }

    public String[] getSortedFilmAlts3() {
        return sortedFilmAlts3;
    }

    public String[] getSortedFilmAlts4() {
        return sortedFilmAlts4;
    }

    public String[] getSortedHistoryQuestions() {
        return sortedHistoryQuestions;
    }

    public String[] getSortedHistoryAnswers() {
        return sortedHistoryAnswers;
    }

    public String[] getSortedHistoryAlts1() {
        return sortedHistoryAlts1;
    }

    public String[] getSortedHistoryAlts2() {
        return sortedHistoryAlts2;
    }

    public String[] getSortedHistoryAlts3() {
        return sortedHistoryAlts3;
    }

    public String[] getSortedHistoryAlts4() {
        return sortedHistoryAlts4;
    }

    public String[] getSortedSportQuestions() {
        return sortedSportQuestions;
    }

    public String[] getSortedSportAnswers() {
        return sortedSportAnswers;
    }

    public String[] getSortedSportAlts1() {
        return sortedSportAlts1;
    }

    public String[] getSortedSportAlts2() {
        return sortedSportAlts2;
    }

    public String[] getSortedSportAlts3() {
        return sortedSportAlts3;
    }

    public String[] getSortedSportAlts4() {
        return sortedSportAlts4;
    }

    public Protocol() throws IOException {
        propertyFileIntoQuiz();
    }

    public void propertyFileIntoQuiz() {

        for (String property : properties) {

            try {
                p.load(new FileInputStream(property));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (property.equals("src\\Gaming.properties")) {
                for (int i = 1; i <= 4; i++) {
                    question = p.getProperty("question" + i);
                    alt1 = p.getProperty("alt" + i + ".1");
                    alt2 = p.getProperty("alt" + i + ".2");
                    alt3 = p.getProperty("alt" + i + ".3");
                    alt4 = p.getProperty("alt" + i + ".4");
                    correct = p.getProperty("correct" + i);

                    tempQuestion = tempQuestion + question;
                    gamingQuestions[0] = tempQuestion;

                    tempAlt1 = tempAlt1 + alt1;
                    gamingAlts1[0] = tempAlt1;

                    tempAlt2 = tempAlt2 + alt2;
                    gamingAlts2[0] = tempAlt2;

                    tempAlt3 = tempAlt3 + alt3;
                    gamingAlts3[0] = tempAlt3;

                    tempAlt4 = tempAlt4 + alt4;
                    gamingAlts4[0] = tempAlt4;

                    tempAnswer = tempAnswer + correct;
                    gamingAnswers[0] = tempAnswer;

                }
                sortedGamingQuestions = removeEmptyElementsInArray(gamingQuestions);
                sortedGamingAnswers = removeEmptyElementsInArray(gamingAnswers);
                sortedGamingAlts1 = removeEmptyElementsInArray(gamingAlts1);
                sortedGamingAlts2 = removeEmptyElementsInArray(gamingAlts2);
                sortedGamingAlts3 = removeEmptyElementsInArray(gamingAlts3);
                sortedGamingAlts4 = removeEmptyElementsInArray(gamingAlts4);

                emptyTempVariables();
            }
            if (property.equals("src\\Film.properties")) {
                for (int i = 1; i <= 4; i++) {
                    question = p.getProperty("question" + i);
                    alt1 = p.getProperty("alt" + i + ".1");
                    alt2 = p.getProperty("alt" + i + ".2");
                    alt3 = p.getProperty("alt" + i + ".3");
                    alt4 = p.getProperty("alt" + i + ".4");
                    correct = p.getProperty("correct" + i);

                    tempQuestion = tempQuestion + question;
                    filmQuestions[0] = tempQuestion;

                    tempAlt1 = tempAlt1 + alt1;
                    filmAlts1[0] = tempAlt1;

                    tempAlt2 = tempAlt2 + alt2;
                    filmAlts2[0] = tempAlt2;

                    tempAlt3 = tempAlt3 + alt3;
                    filmAlts3[0] = tempAlt3;

                    tempAlt4 = tempAlt4 + alt4;
                    filmAlts4[0] = tempAlt4;

                    tempAnswer = tempAnswer + correct;
                    filmAnswers[0] = tempAnswer;

                }
                sortedFilmQuestions = removeEmptyElementsInArray(filmQuestions);
                sortedFilmAnswers = removeEmptyElementsInArray(filmAnswers);
                sortedFilmAlts1 = removeEmptyElementsInArray(filmAlts1);
                sortedFilmAlts2 = removeEmptyElementsInArray(filmAlts2);
                sortedFilmAlts3 = removeEmptyElementsInArray(filmAlts3);
                sortedFilmAlts4 = removeEmptyElementsInArray(filmAlts4);

                emptyTempVariables();
            }
            if (property.equals("src\\History.properties")) {
                for (int i = 1; i <= 4; i++) {
                    question = p.getProperty("question" + i);
                    alt1 = p.getProperty("alt" + i + ".1");
                    alt2 = p.getProperty("alt" + i + ".2");
                    alt3 = p.getProperty("alt" + i + ".3");
                    alt4 = p.getProperty("alt" + i + ".4");
                    correct = p.getProperty("correct" + i);

                    tempQuestion = tempQuestion + question;
                    historyQuestions[0] = tempQuestion;

                    tempAlt1 = tempAlt1 + alt1;
                    historyAlts1[0] = tempAlt1;

                    tempAlt2 = tempAlt2 + alt2;
                    historyAlts2[0] = tempAlt2;

                    tempAlt3 = tempAlt3 + alt3;
                    historyAlts3[0] = tempAlt3;

                    tempAlt4 = tempAlt4 + alt4;
                    historyAlts4[0] = tempAlt4;

                    tempAnswer = tempAnswer + correct;
                    historyAnswers[0] = tempAnswer;

                }
                sortedHistoryQuestions = removeEmptyElementsInArray(historyQuestions);
                sortedHistoryAnswers = removeEmptyElementsInArray(historyAnswers);
                sortedHistoryAlts1 = removeEmptyElementsInArray(historyAlts1);
                sortedHistoryAlts2 = removeEmptyElementsInArray(historyAlts2);
                sortedHistoryAlts3 = removeEmptyElementsInArray(historyAlts3);
                sortedHistoryAlts4 = removeEmptyElementsInArray(historyAlts4);

                emptyTempVariables();
            }
            if (property.equals("src\\Sport.properties")) {
                for (int i = 1; i <= 4; i++) {
                    question = p.getProperty("question" + i);
                    alt1 = p.getProperty("alt" + i + ".1");
                    alt2 = p.getProperty("alt" + i + ".2");
                    alt3 = p.getProperty("alt" + i + ".3");
                    alt4 = p.getProperty("alt" + i + ".4");
                    correct = p.getProperty("correct" + i);

                    tempQuestion = tempQuestion + question;
                    sportQuestions[0] = tempQuestion;

                    tempAlt1 = tempAlt1 + alt1;
                    sportAlts1[0] = tempAlt1;

                    tempAlt2 = tempAlt2 + alt2;
                    sportAlts2[0] = tempAlt2;

                    tempAlt3 = tempAlt3 + alt3;
                    sportAlts3[0] = tempAlt3;

                    tempAlt4 = tempAlt4 + alt4;
                    sportAlts4[0] = tempAlt4;

                    tempAnswer = tempAnswer + correct;
                    sportAnswers[0] = tempAnswer;

                }
                sortedSportQuestions = removeEmptyElementsInArray(sportQuestions);
                sortedSportAnswers = removeEmptyElementsInArray(sportAnswers);
                sortedSportAlts1 = removeEmptyElementsInArray(sportAlts1);
                sortedSportAlts2 = removeEmptyElementsInArray(sportAlts2);
                sortedSportAlts3 = removeEmptyElementsInArray(sportAlts3);
                sortedSportAlts4 = removeEmptyElementsInArray(sportAlts4);

                emptyTempVariables();
            }
        }
    }

    public String[] removeEmptyElementsInArray(String[] unsortedArray) {

        String[] tempString = unsortedArray[0].split("\"");
        List<String> tempList = new ArrayList<String>(Arrays.asList(tempString));
        tempList.removeAll(Arrays.asList("", null));
        String[] sortedArray = tempList.toArray(new String[tempList.size()]);

        return sortedArray;
    }
    
    public void emptyTempVariables(){
        tempAnswer = "";
        tempQuestion = "";
        tempAlt1 = "";
        tempAlt2 = "";
        tempAlt3 = "";
        tempAlt4 = "";
    }

    public static void main(String[] args) throws IOException {
        new Protocol();
    }
}