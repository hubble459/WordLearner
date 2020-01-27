import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WLFiles {
    private String filename;

    public String getFilename() {
        return filename;
    }

    private JDialog d;

    public void chooseFile() {
        d = new JDialog();
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
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.showOpenDialog(d);
            if (chooser.getSelectedFile() != null) {
                filename = chooser.getSelectedFile().getName();
                d.dispose();
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

    public String[][] readFile(String filename, String regex) {
        if (!filename.contains("files/") && !Files.exists(Paths.get(filename))) return null;

        ArrayList<String> lines = new ArrayList<>();
        if (filename.contains("files/")) {
            InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream(filename));
            System.out.println("[ENCODING] " + isr.getEncoding());
            if (!isr.getEncoding().equalsIgnoreCase("UTF8")) {
                resetWithUTF8();
            }
            BufferedReader reader = new BufferedReader(isr);
            reader.lines().forEach(lines::add);
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                reader.lines().forEach(lines::add);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
                JOptionPane.showMessageDialog(null, filename + " is unreadable! Try a different regex.");
                array = new String[0][0];
            } else {
                JOptionPane.showMessageDialog(null, filename + " contains errors on lines " + errorLines + "!");
            }
        }

        return array;
    }

    private void resetWithUTF8() {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar;

            currentJar = new File(WordLearner.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // is it a jar file?
            if (!currentJar.getName().endsWith(".jar")) {
                JOptionPane.showMessageDialog(null, "You have to run it from a .jar file to be able to change the file");
                return;
            }

            // Build command: java -Dfile.encoding=utf-8 -jar application.jar
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-Dfile.encoding=utf-8");
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
