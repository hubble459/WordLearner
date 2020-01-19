import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WLFiles {
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void chooseFile() {
        JDialog d = new JDialog();
        d.setTitle("Choose File");
        d.setModal(true);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Hiragana, Katakana or Custom
        JPanel panel = new JPanel(new GridLayout(3, 0));
        JButton hiragana = new JButton("Hiragana");
        JButton katakana = new JButton("Katakana");
        JButton custom = new JButton("Custom");

        hiragana.addActionListener(e -> {
            filename = "files/hiragana.txt";
            d.dispose();
        });
        katakana.addActionListener(e -> {
            filename = "files/katakana.txt";
            d.dispose();
        });
        custom.addActionListener(e -> {
            ArrayList<String> files = new ArrayList<>();
            try {
                Files.list(new File("").toPath()).forEach(x -> {
                    if (!Files.isDirectory(x))
                        files.add(x.getFileName().toString());
                });
            } catch (IOException x) {
                x.printStackTrace();
            }

            if (files.size() != 0) {
                JDialog d2 = new JDialog();
                d2.setTitle("Choose Custom File");
                d2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                d2.setModal(true);
                JPanel filesListing = new JPanel(new GridLayout(files.size(), 2));
                for (String file : files) {
                    JButton button = new JButton(file);
                    button.setSize(new Dimension(150, 40));
                    button.addActionListener(e1 -> {
                        filename = e1.getActionCommand();
                        d.dispose();
                        d2.dispose();
                    });
                    filesListing.add(button);
                }
                d2.add(filesListing);
                d2.pack();
                d2.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No files found in " + System.getProperty("user.dir"));
            }
        });

        panel.add(hiragana);
        panel.add(katakana);
        panel.add(custom);
        for (int i = 0; i < panel.getComponentCount(); i++) {
            ((JButton) panel.getComponent(i)).setOpaque(false);
        }

        // Add Panels
        d.add(panel);

        // Start
        d.pack();
        d.setVisible(true);
    }

    static String[][] readFile(String filename, String regex) {
        if (!Files.exists(Paths.get(filename))) return null;
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.lines().forEach(lines::add);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean correct = true;
        ArrayList<Integer> errorLines = new ArrayList<>();
        String[][] array = new String[lines.size()][2];
        for (int i = 0; i < lines.size(); i++) {
            String[] split = lines.get(i).split(regex);
            if (split.length != 2) {
                correct = false;
                errorLines.add(i);
            } else {
                array[i][0] = split[0];
                array[i][1] = split[1];
            }
        }
        if (array.length < 4) {
            JOptionPane.showMessageDialog(null, filename + " has to have more than 4 lines!");
            array = new String[0][0];
        } else if (!correct) {
            if (errorLines.size() == lines.size()) {
                JOptionPane.showMessageDialog(null, filename + " is unreadable!");
                array = new String[0][0];
            } else {
                JOptionPane.showMessageDialog(null, filename + " contains errors on lines " + errorLines + "!");
            }
        }

        return array;
    }
}
