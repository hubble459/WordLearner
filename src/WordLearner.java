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
    private JLabel questionsMax;
    private JLabel multipleChoices;
    private String regex = " - ";
    private int amountQuestions;
    private int multipleChoice = 4;

    public WordLearner(String filename) {
        f = new JFrame("WordLearner");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (!Files.exists(Paths.get(filename))) {
            JOptionPane.showMessageDialog(f, "File not found!");
            System.exit(0);
        }

        // Menu Bar
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Options");
        JMenuItem mi1 = new JMenuItem("Change Regex");
        mi1.addActionListener(this);
        mi1.setActionCommand("regex");
        JMenuItem mi2 = new JMenuItem("Change FontSize");
        mi2.addActionListener(this);
        mi2.setActionCommand("fontSize");
        JMenuItem mi3 = new JMenuItem("Turn on Debug Mode");
        mi3.addActionListener(this);
        mi3.setActionCommand("debug");
        m.add(mi1);
        m.add(mi2);
        m.add(mi3);
        mb.add(m);
        f.setJMenuBar(mb);

        questions = readFile(filename, regex);

        // Options Panel
        JPanel options = new JPanel(new GridLayout(3,0));
        options.setPreferredSize(new Dimension(400, 200));

        // Info Panel
        amountQuestions = questions.length;
        JPanel info = new JPanel(new GridLayout(3, 0));
        JLabel file = new JLabel("Filename: " + filename, SwingConstants.CENTER);
        questionsMax = new JLabel("Questions: " + amountQuestions, SwingConstants.CENTER);
        multipleChoices = new JLabel("Multiple Choices: " + 4, SwingConstants.CENTER);
        info.add(file);
        info.add(questionsMax);
        info.add(multipleChoices);
        options.add(info);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,0));
        // Change File
        JButton changeFile = new JButton("<html><font color=blue>Change File</font></html>");
        changeFile.setActionCommand("changeT");
        changeFile.addActionListener(this);
        changeFile.setOpaque(false);
        changeFile.setContentAreaFilled(false);
        changeFile.setBorderPainted(false);
        buttons.add(changeFile);

        // Change Question Amount
        JButton setQuestions = new JButton("<html><font color=blue>Change Question Amount</font></html>");
        setQuestions.setActionCommand("changeQ");
        setQuestions.addActionListener(this);
        setQuestions.setOpaque(false);
        setQuestions.setContentAreaFilled(false);
        setQuestions.setBorderPainted(false);
        buttons.add(setQuestions);

        // Change Multiple Choice Amount
        JButton setChoiceAmount = new JButton("<html><font color=blue>Change Multiple Questions Amount</font></html>");
        setChoiceAmount.setActionCommand("changeC");
        setChoiceAmount.addActionListener(this);
        setChoiceAmount.setOpaque(false);
        setChoiceAmount.setContentAreaFilled(false);
        setChoiceAmount.setBorderPainted(false);
        buttons.add(setChoiceAmount);
        options.add(buttons);

        // Start Button
        JButton start = new JButton("Start");
        start.addActionListener(this);
        start.setOpaque(false);
        start.setBorderPainted(false);
        options.add(start);

        // Add Panels
        f.add(options);

        // Start
        f.pack();
        f.setVisible(true);
    }

    private String[][] readFile(String filename, String regex) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.lines().forEach(lines::add);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean correct = true;
        int line = -1;
        String[][] array = new String[lines.size()][2];
        for (int i = 0; i < lines.size(); i++) {
            String[] split = lines.get(i).split(regex);
            if (split.length != 2) {
                correct = false;
                line = i;
                break;
            }
            array[i][0] = split[0];
            array[i][1] = split[1];
        }
        if (!correct) {
            JOptionPane.showMessageDialog(f, filename + " contains an error on line "+line+"!");
            System.exit(0);
        }
        return array;
    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the Change Question button is pressed this gives an input dialog.
        // The input will be checked, if its lower than 0, or higher than the maximum amount of questions, it will do nothing.
        // Else it changes the JLabel of question amount to the new amount and stores this value in amountQuestions
        if (e.getActionCommand().equals("changeQ")) {
            String result = JOptionPane.showInputDialog(f, "Enter the amount of questions you want ["+questions.length+"]:", "Change Question Amount", JOptionPane.PLAIN_MESSAGE);
            if (result != null && isNumber(result)) {
                if (Integer.parseInt(result) > amountQuestions || Integer.parseInt(result) < 0) return;
                amountQuestions = Integer.parseInt(result);
                questionsMax.setText("Set Questions: " + amountQuestions);
            }
        }

        // If the Change Multiple Choice Amount button is pressed this gives an input dialog.
        // If the input is not a number or the number is not between 2 and 8, it will be ignored.
        // Else the input will be set as the new multiple choice option amount.
        if (e.getActionCommand().equals("changeC")) {
            String result = JOptionPane.showInputDialog(f, "Enter the amount of multiple choice questions you want [4]:", "Change Amount", JOptionPane.PLAIN_MESSAGE);
            if (result != null && isNumber(result)) {
                if (Integer.parseInt(result) < 2 || Integer.parseInt(result) > 8) return;
                multipleChoice = Integer.parseInt(result);
                multipleChoices.setText("Set Multi Questions: " + multipleChoice);
            }
        }
    }
}
