import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WLQuestion {
    final private int side1;
    final private int side2;
    private int choices = 4;
    private int answerInQuestions;
    private String question;
    final private String[][] questions;
    final private ArrayList<String> previous = new ArrayList<>();

    public WLQuestion(String[][] questions) {
        getJSONFile();
        this.questions = questions;
        side1 = (int) (Math.random() * 2);
        side2 = side1 == 0 ? 1 : 0;
    }

    private int getRandom() {
        int random;
        do {
            random = (int) (Math.random() * questions.length);
        } while (previous.contains(questions[random][0]));
        previous.add(questions[random][0]);
        return random;
    }

    public String getQuestion(int q) {
        int question = q;
        if (q == -1) question = getRandom();
        this.answerInQuestions = question;
        this.question = questions[question][side1];
        makeJSONFile();
        return this.question;
    }

    public String getQuestion() {
        return getQuestion(-1);
    }

    public String[] getAnswers(int q) {
        if (q != -1) answerInQuestions = q;
        if (question == null) return null;
        String[] answers = new String[choices];
        ArrayList<Integer> tempArray = new ArrayList<>();
        int answerInArray = (int) (Math.random() * choices);
        for (int i = 0; i < choices; i++) {
            if (i == answerInArray) {
                answers[i] = questions[answerInQuestions][side2];
                tempArray.add(answerInQuestions);
            } else {
                int rand;
                boolean repeat;
                do {
                    repeat = false;
                    rand = (int) (Math.random() * questions.length);
                    for (Integer prev : tempArray) {
                        if (prev == rand) {
                            repeat = true;
                            break;
                        }
                    }
                } while (repeat || questions[rand][side2].equals(questions[answerInQuestions][side2]));
                answers[i] = questions[rand][side2];
                tempArray.add(rand);
            }
        }
        return answers;
    }

    public String[] getAnswers() {
        return getAnswers(-1);
    }


    public String getAnswerString() {
        return questions[answerInQuestions][side2];
    }

    private void getJSONFile() {
        if (Files.exists(new File("previous.json").toPath())) {
            try {
                String jsonString = new String(Files.readAllBytes(new File("previous.json").toPath()));
                JSONObject jsonObject = new JSONObject(jsonString);
                for (int i = 0; i < jsonObject.getJSONArray("previous").length(); i++) {
                    String q = jsonObject.getJSONArray("previous").getString(i);
                    if (!previous.contains(q)) previous.add(q);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeJSONFile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("previous", previous);
        String jsonString = jsonObject.toString();
        try {
            Files.write(new File("previous.json").toPath(), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteJSON() {
        if (Files.exists(Paths.get("previous.json"))) {
            try {
                Files.delete(Paths.get("previous.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setChoices(int choices) {
        this.choices = choices;
    }
}
