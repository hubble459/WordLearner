import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class WLFiles implements ActionListener {
//    if (!Files.exists(Paths.get(filename))) {
//        JOptionPane.showMessageDialog(f, "File not found!");
//        System.exit(0);
//    }

    private static String filename;

    public static String getFilename() {
        return filename;
    }

    static void chooseFile() {
        filename = null;
        JDialog dialog = new JDialog();
        dialog.setTitle("Choose File");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Hiragana, Katakana or Custom
        JPanel panel = new JPanel(new GridLayout(3, 0));
        JButton hiragana = new JButton("Hiragana");
        JButton katakana = new JButton("Katakana");
        JButton custom = new JButton("Custom");

        hiragana.addActionListener(e -> {
            filename = "files/hiragana.txt";
            dialog.dispose();
        });
        katakana.addActionListener(e -> {
            filename = "files/katakana.txt";
            dialog.dispose();
        });
        custom.addActionListener(e -> {
            // Add Custom
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
                JDialog dialog1 = new JDialog();
                dialog1.setTitle("Choose Custom File");
                JPanel filesListing = new JPanel(new GridLayout(files.size(), 2));
                for (String file : files) {
                    JButton button = new JButton(file);
                    button.setSize(new Dimension(300, 40));
                    button.addActionListener(e1 -> {
                        filename = e1.getActionCommand();
                        dialog1.dispose();
                    });
                    filesListing.add(button);
                }
                dialog1.add(filesListing);
                dialog1.pack();
                dialog1.setVisible(true);
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
        dialog.add(panel);

        // Start
        dialog.pack();
        dialog.setVisible(true);
    }

    static String[][] readFile(String filename, String regex) {
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
            JOptionPane.showMessageDialog(null, filename + " contains an error on line " + line + "!");
            System.exit(0);
        }
        return array;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
