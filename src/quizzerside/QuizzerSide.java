package quizzerside;

import com.illposed.osc.*;
import quizzerside.gui.Console;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.lang.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class QuizzerSide extends JFrame {
    public static Console console = new Console();
    public static JTextField inputField = new JTextField();

    public static Font font = new Font("Avenir-Light", Font.PLAIN, 12);

    private void addComponentsToPane() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
        setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        console.setEditable(false);

        JScrollPane consoleScrollPane = new JScrollPane(console);

        console.setBackground(new Color(230, 230, 250));
        console.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        console.setFont(font);

        DefaultCaret caret = (DefaultCaret) console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        inputField.setFont(font);

        inputField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterKeyPressed");
        inputField.getActionMap().put("enterKeyPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.printToConsole(inputField.getText());

                try {
                    InetAddress address = InetAddress.getLocalHost();

                    OSCPortOut sender = new OSCPortOut(address, OSCPortOut.defaultSCOSCPort());

                    String userInput = inputField.getText();
                    Object input = userInput;

                    Collection<Object> inputToSend = Arrays.asList(input);

                    OSCMessage msg = new OSCMessage("/msg", inputToSend);

                    sender.send(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                inputField.setText("");
            }
        });

        getContentPane().add(inputField, BorderLayout.SOUTH);
        getContentPane().add(consoleScrollPane);
    }

    private static void createAndShowGUI() {
        QuizzerSide frame = new QuizzerSide("Quiz Creator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addComponentsToPane();

        frame.setSize(800, 600);
        frame.setVisible(true);

        inputField.requestFocusInWindow();
    }

    public QuizzerSide(String name) {
        super(name);
    }

    public static void main(String[] args) throws java.lang.InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
