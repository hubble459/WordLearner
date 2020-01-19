import javax.swing.*;
import java.awt.*;

public class WLScore {
    private int time;
    private int correct;
    private int amount;
    private long average;
    public static boolean restart;

    public WLScore(int time, int correct, int amount, long average) {
        this.time = time;
        this.correct = correct;
        this.amount = amount;
        this.average = average;
    }

    public void showScore() {
        JDialog d = new JDialog();
        d.setTitle("Score");
        d.setModal(true);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLayout(new GridLayout(7, 0));
        JLabel q = new JLabel("Questions:  " + amount);
        JLabel c = new JLabel("Correct:       " + correct);
        JLabel w = new JLabel("Wrong:         " + (amount - correct));
        double score = Math.floor((100.0 / amount * correct) * 10.0) / 10.0;
        JLabel g = new JLabel("Grade:          " + score);

        int minutes = time / 60;
        int seconds = time % 60;
        JLabel t = new JLabel("Total Time:   " + minutes + " minutes and " + seconds + " seconds");
        JLabel a = new JLabel("Avg. Time:    " + (average / 1000.0) + " seconds");

        d.add(q);
        d.add(c);
        d.add(w);
        d.add(g);
        d.add(t);
        d.add(a);

        JPanel buttons = new JPanel(new GridLayout(0,2));
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> {WLScore.restart = true; d.dispose();});
        buttons.add(exit);
        buttons.add(restart);

        d.add(buttons);

        d.pack();
        d.setVisible(true);
    }
}
