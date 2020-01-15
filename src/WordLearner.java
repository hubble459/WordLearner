import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordLearner implements ActionListener {
    private WLOptions wlOptions = new WLOptions();

    public static void main(String[] args) {
        new WordLearner().run();
    }

    private JFrame f;
    private JPanel main;
    private int amount;
    private int choices;
    private final int FONT_SIZE = 30;
    private int fontSize = FONT_SIZE;
    private String[][] questions;

    public void run() {
        wlOptions.start();
        getAll();
        if (questions.length == 0) System.exit(0);

        // Setup JFrame
        f = new JFrame("WordLearner");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Make Menu Bar
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Options");
        JMenuItem mi1 = new JMenuItem("Change Font Size");
        mi1.setSize(20, 20);
        mi1.addActionListener(this);
        mi1.setActionCommand("fontSize");
        JMenuItem mi2 = new JMenuItem("Close");
        mi2.addActionListener(this);
        mi2.setActionCommand("close");
        m.add(mi1);
        m.add(mi2);
        mb.add(m);
        f.setJMenuBar(mb);

        // Make a main panel
        main = new JPanel(new GridLayout(2, 0));

        // Question Component
        final String s = "Question";
        final String html = "<html><body style='width: %1spx'>%1s";
        System.out.println(fontSize);
        JLabel q = new JLabel("<html><font size="+fontSize+" color=blue>"+String.format(html, 200, s)+"</font></html>", SwingConstants.CENTER);
        main.add(q);

        // Answer Panel
        int rows = 2;
        int cols = choices/2+choices%2;

        JPanel answers = new JPanel(new GridLayout(rows, cols));
        answers.setPreferredSize(new Dimension(500, 200));
        for (int i = 0; i < choices; i++) {
            JButton b = new JButton("<html><font size="+fontSize+" color=yellow>"+"B" + (i+1)+"</font></html>");
            b.setBackground(Color.PINK);
            answers.add(b);
        }

        main.add(answers);
        // Add Panels
        f.add(main);

        // Start
        f.pack();
        f.setVisible(true);
    }

    private void getAll() {
        amount = wlOptions.getAmountQuestions();
        choices = wlOptions.getChoices();
        questions = wlOptions.getQuestions();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Font Size
        if (e.getActionCommand().equals("fontSize")) {
            String[] choices = {"x0.5", "x1", "x1.50", "x2", "x3", "x4", "x5", "x7", "x10", "x20"};
            String input = (String) JOptionPane.showInputDialog(f, "Set Font Size Multiplier",
                    "Font Size", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
            int multiplier = (int)Double.parseDouble(input.replace("x", ""));
            fontSize = FONT_SIZE * multiplier;
            System.out.println(fontSize);
            for (int i = 0; i < main.getComponentCount(); i++) {
                if (main.getComponent(i) instanceof JLabel) {
                    JLabel l = (JLabel) main.getComponent(i);
                    final String s = "Question";
                    final String html = "<html><body style='width: %1spx'>%1s";
                    l.setText("<html><font size="+fontSize+" color=blue>"+String.format(html, 200, s)+"</font></html>");
                    System.out.println("uwu");
                }
                if (main.getComponent(i) instanceof JPanel) {
                    JPanel jPanel = (JPanel) main.getComponent(i);
                    for (int j = 0; j < jPanel.getComponentCount(); j++) {
                        JButton b = (JButton) jPanel.getComponent(j);
                        b.setText("<html><font size="+fontSize+" color=yellow>"+"B" + (j+1)+"</font></html>");
                        System.out.println("owo");
                    }
                }
            }
        }

        // Close
        if (e.getActionCommand().equals("close")) {
            System.exit(0);
        }
    }
}