import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WordLearner implements ActionListener {
    final private WLOptions wlOptions = new WLOptions();

    public static void main(String[] args) {
        new WordLearner().run();
    }

    private JFrame f;
    private JPanel main;
    private JPanel answers;
    private JLabel q;
    private int choices;
    private int amount;
    private int counter;
    private int correct;
    private long lastTime;
    private long average;
    private long startTime;
    private String answer;
    private final int FONT_SIZE = 50;
    private int fontSize = FONT_SIZE;
    private String[][] questions;
    private Robot robot;

    public void run() {
        // Change the file encoding to UTF-8 (illegal reflective access operation)
//        System.setProperty("file.encoding","UTF-8");
//        try {
//            Field charset = Charset.class.getDeclaredField("defaultCharset");
//            charset.setAccessible(true);
//            charset.set(null,null);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        // Choose File
        wlOptions.start();
        getAll();
        if (questions.length == 0) System.exit(0);

        // Setup JFrame
        f = new JFrame("WordLearner");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WLQuestion.deleteJSON();
            }
        });


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
        q = new JLabel();
        q.setHorizontalAlignment(SwingConstants.CENTER);
        q.setFont(new Font(q.getFont().getName(), q.getFont().getStyle(), fontSize * 3));
        main.add(q);

        // Answer Panel
        int rows = 2;
        int cols = choices / 2 + choices % 2;

        answers = new JPanel(new GridLayout(rows, cols));
        answers.setPreferredSize(new Dimension(500, 200));
        for (int i = 0; i < choices; i++) {
            JButton b = new JButton();
            b.addActionListener(this);
            b.setActionCommand("button");
            b.setFocusable(true);
            b.setFocusTraversalKeysEnabled(true);
            b.setFont(new Font(b.getFont().getName(), b.getFont().getStyle(), fontSize));
            b.setBackground(Color.PINK);
            answers.add(b);
        }

        startTime = System.currentTimeMillis();
        // Game Start
        setQuestion();

        main.add(answers);

        // Add Panels
        f.add(main);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        f.setFocusable(true);
        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key > 48 && key < 58) {
                    answers.getComponent(key - 49).requestFocus();
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }
            }
        });

        // Start
        f.pack();
        f.setVisible(true);
    }

    private void getAll() {
        amount = wlOptions.getAmountQuestions();
        choices = wlOptions.getChoices();
        questions = wlOptions.getQuestions();
    }

    private void setQuestion() {
        WLQuestion question = new WLQuestion(questions);
        question.setChoices(choices);
        q.setText(question.getQuestion());
        String[] answersArray = question.getAnswers();
        for (int j = 0; j < choices; j++) {
            ((JButton) (answers.getComponent(j))).setText(answersArray[j]);
        }
        answer = question.getAnswerString();
    }

    Timer timer = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        // Font Size
        if (e.getActionCommand().equals("fontSize")) {
            String[] choices = {"x0.5", "x1", "x1.50", "x2", "x3"};
            String input = (String) JOptionPane.showInputDialog(f, "Set Font Size Multiplier",
                    "Font Size", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
            int multiplier = (int) Double.parseDouble(input.replace("x", ""));
            fontSize = FONT_SIZE * multiplier;
            for (int i = 0; i < main.getComponentCount(); i++) {
                if (main.getComponent(i) instanceof JLabel) {
                    JLabel l = (JLabel) main.getComponent(i);
                    l.setFont(new Font(l.getFont().getName(), l.getFont().getStyle(), fontSize * 3));
                }
                if (main.getComponent(i) instanceof JPanel) {
                    JPanel jPanel = (JPanel) main.getComponent(i);
                    for (int j = 0; j < jPanel.getComponentCount(); j++) {
                        JButton b = (JButton) jPanel.getComponent(j);
                        b.setFont(new Font(b.getFont().getName(), b.getFont().getStyle(), fontSize));
                    }
                }
            }
        }

        // Check Answer
        if (e.getActionCommand().equals("button")) {
            f.requestFocus();
            if (timer != null) timer.stop();
            if (average == 0) {
                average = System.currentTimeMillis() - startTime;
            } else {
                long tmpAverage = System.currentTimeMillis() - lastTime;
                average = (average + tmpAverage) / 2;
            }
            lastTime = System.currentTimeMillis();

            if (++counter == amount) scoreScreen();
            else {
                int nanoseconds = 500;
                q.setFont(new Font(q.getFont().getName(), q.getFont().getStyle(), fontSize * 2));
                if (((JButton) (e.getSource())).getText().equals(answer)) {
                    q.setForeground(Color.GREEN);
                    q.setText("Correct!");
                    correct++;
                } else {
                    q.setForeground(Color.RED);
                    nanoseconds = 1000;
                    for (int j = 0; j < choices; j++) {
                        JButton b = ((JButton) (answers.getComponent(j)));
                        if (b.getText().equals(answer)) {
                            b.setBackground(Color.GREEN);
                        }
                    }
                    q.setText("Wrong!");
                }
                timer = new Timer(nanoseconds, e1 -> {
                    q.setForeground(Color.BLACK);
                    q.setFont(new Font(q.getFont().getName(), q.getFont().getStyle(), fontSize * 3));
                    for (int j = 0; j < choices; j++) {
                        answers.getComponent(j).setBackground(Color.PINK);
                    }
                    setQuestion();
                    timer.stop();
                });
                timer.start();
            }
        }

        // Close
        if (e.getActionCommand().equals("close")) {
            scoreScreen();
        }
    }

    public void scoreScreen() {
        WLQuestion.deleteJSON();
        f.setVisible(false);
        int time = (int) ((System.currentTimeMillis() - startTime) / 1000);
        WLScore wlScore = new WLScore(time, correct, counter, average);
        wlScore.showScore();
        if (WLScore.restart) reset();
        else System.exit(0);
        f.setVisible(true);
    }

    private void reset() {
        counter = 0;
        WLQuestion.deleteJSON();
        startTime = System.currentTimeMillis();
        correct = 0;
        lastTime = 0;
        average = 0;
    }
}