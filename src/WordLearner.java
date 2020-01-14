import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WordLearner implements ActionListener {
    public static void main(String[] args) {
        new WordLearner("hiragana.txt");
    }

    private String[][] questions;
    private JFrame f;
    private int amountQuestions;
    private int multipleChoice = 4;

    public WordLearner(String filename) {
        f = new JFrame("WordLearner");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (!Files.exists(Paths.get(filename))) {
            JOptionPane.showMessageDialog(f, "File not found!");
            System.exit(0);
        }

        questions = readFile(filename);

        // Options Panel
        JPanel options = new JPanel(new GridLayout(3,0));
        options.setPreferredSize(new Dimension(400, 200));

        // Info Panel
        amountQuestions = questions.length;
        JPanel info = new JPanel(new GridLayout(3, 0));
        JLabel file = new JLabel("Filename: " + filename, SwingConstants.CENTER);
        JLabel questionsMax = new JLabel("Questions: " + amountQuestions, SwingConstants.CENTER);
        JLabel multipleChoices = new JLabel("Multiple Choices: " + 4, SwingConstants.CENTER);
        info.add(file);
        info.add(questionsMax);
        info.add(multipleChoices);
        options.add(info);

        // Change Question Amount
        JButton setQuestions = new JButton("Change Question Amount");
        setQuestions.setActionCommand("changeQ");
        setQuestions.addActionListener(this);
        options.add(setQuestions);

        // Change Multiple Choice Amount
        JButton setChoiceAmount = new JButton("Change Multiple Questions Amount");
        setChoiceAmount.setActionCommand("changeC");
        setChoiceAmount.addActionListener(this);
        options.add(setChoiceAmount);

        // Add Panels
        f.add(options);

        // Start
        f.pack();
        f.setVisible(true);
    }

    private String[][] readFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.lines().forEach(lines::add);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean correct = true;
        String[][] array = new String[lines.size()][2];
        for (int i = 0; i < lines.size(); i++) {
            String[] split = lines.get(i).split(" - ");
            if (split.length != 2) {
                correct = false;
                break;
            }
            array[i][0] = split[0];
            array[i][1] = split[1];
        }
        if (!correct) {
            JOptionPane.showMessageDialog(f, "This file contains an error!");
            System.exit(0);
        }
        return array;
    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') return false;
        }
        return Integer.parseInt(s) <= amountQuestions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changeQ")) {
            String result = JOptionPane.showInputDialog(f, "Enter the amount of questions you want ["+questions.length+"]:", "Change Question Amount", JOptionPane.PLAIN_MESSAGE);
            if (result != null && isNumber(result)) {
                amountQuestions = Integer.parseInt(result);
                ((JButton)e.getSource()).setText("Set Questions: " + amountQuestions);
            }
        }
        if (e.getActionCommand().equals("ChangeC")) {
            String result = JOptionPane.showInputDialog(f, "Enter the amount of multiple choice questions you want [4]:", "Change Amount", JOptionPane.PLAIN_MESSAGE);
            if (result != null && isNumber(result)) {
                multipleChoice = Integer.parseInt(result);
                ((JButton)e.getSource()).setText("Set Multi Questions: " + multipleChoice);
            }
        }
    }
}
