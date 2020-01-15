import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class WordLearner implements ActionListener {
    public static void main(String[] args) {
        WordLearner wl = new WordLearner();
    }

    private String[][] questions;
    private JFrame f;
    private JLabel questionsMax;
    private JLabel multipleChoices;
    private JLabel file;
    private String regex = " - ";
    private volatile String filename;
    private int amountQuestions;
    private int choices = 4;

    public WordLearner() {
        chooseFile();
        questions = WLFiles.readFile(filename, regex);

        f = new JFrame("WordLearner");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        // Options Panel
        JPanel options = new JPanel(new GridLayout(3, 0));
        options.setPreferredSize(new Dimension(400, 200));

        // Info Panel
        amountQuestions = questions.length;
        JPanel info = new JPanel(new GridLayout(3, 0));
        file = new JLabel("File: " + filename.replace("files/", ""), SwingConstants.CENTER);
        questionsMax = new JLabel("Questions: " + amountQuestions, SwingConstants.CENTER);
        multipleChoices = new JLabel("Choices: " + choices, SwingConstants.CENTER);
        info.add(file);
        info.add(questionsMax);
        info.add(multipleChoices);
        options.add(info);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3, 0));
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

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') return false;
        }
        return true;
    }

    private void chooseFile() {
        WLFiles.chooseFile();
        while (WLFiles.getFilename() == null) Thread.onSpinWait();
        System.out.println(WLFiles.getFilename());
        System.out.println("OwO");
        filename = WLFiles.getFilename();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the Change Question button is pressed this gives an input dialog.
        // The input will be checked, if its lower than 0, or higher than the maximum amount of questions, it will do nothing.
        // Else it changes the JLabel of question amount to the new amount and stores this value in amountQuestions
        if (e.getActionCommand().equals("changeQ")) {
            String result = JOptionPane.showInputDialog(f, "Enter the amount of questions you want [" + questions.length + "]:", "Change Question Amount", JOptionPane.PLAIN_MESSAGE);
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
                choices = Integer.parseInt(result);
                multipleChoices.setText("Set Multi Questions: " + choices);
            }
        }

        // Change Text File
        if (e.getActionCommand().equals("changeT")) {
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
}
