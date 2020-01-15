import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class WordLearner {
    private WLOptions wlOptions = new WLOptions();

    public static void main(String[] args) {
        new WordLearner().run();
    }

    private JFrame f;
    private int amount;
    private int choices;
    private String[][] questions;

    public void run() {
        wlOptions.start();
        getAll();
        System.out.println(amount);
        System.out.println(choices);
        System.out.println(Arrays.deepToString(questions));
    }

    private void getAll() {
        amount = wlOptions.getAmountQuestions();
        choices = wlOptions.getChoices();
        questions = wlOptions.getQuestions();
    }

    private void reset() {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar;

            currentJar = new File(WordLearner.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // is it a jar file?
            if (!currentJar.getName().endsWith(".jar")) {
                JOptionPane.showMessageDialog(f, "You have to run it from a .jar file to be able to change the file");
                return;
            }

            // Build command: java -jar application.jar
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (URISyntaxException | IOException ex) {
            ex.printStackTrace();
        }
    }
}