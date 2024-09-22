package org.hl7;


import javax.swing.*;
import java.awt.*;




/**
 * @author - Shirshak Upadhayay
 * @Date - 29/06/2024
 */
public class StarterGUI extends JFrame {

    private JTextField ipField;
    private JTextField portField;

    private JTextField durationField;
    private JButton sendBtn;

    private JLabel logMessage;
    private JTextArea textArea;
    private HL7MessageProvider hl7MessageProvider;



    public StarterGUI(){
        setTitle("Connect to Pigeon");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        ipField = new JTextField(20);
        portField = new JTextField(20);
        durationField = new JTextField(20);

        JLabel label1 = new JLabel("Server's IP:");
        JLabel label2 = new JLabel("Port:");
        JLabel label3 = new JLabel("Interval(in seconds):");
        logMessage = new JLabel("");
        logMessage.setForeground(Color.GREEN);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connect());

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> sendMessage());

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(e -> disconnect());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label1, gbc);
        gbc.gridx = 1;
        panel.add(ipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(label2, gbc);
        gbc.gridx = 1;
        panel.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(label3,gbc);
        gbc.gridx = 1;
        panel.add(durationField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(connectButton, gbc);

        gbc.gridx = 1;
        panel.add(sendBtn, gbc);
        gbc.gridx = 2;
        panel.add(disconnectButton, gbc);


        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setEditable(false);


        JScrollPane scrollPane = new JScrollPane(textArea);


        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void disconnect() {
        try {
            hl7MessageProvider.closeConnection();
        }catch (Exception e){
            textArea.append(e.getMessage()+"\n");
        }
    }

    private void sendMessage() {
           Thread thread = new Thread(new DaemonThread(hl7MessageProvider,textArea));
           thread.start();
    }

    public void connect(){
        try {
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            int interval = Integer.parseInt(durationField.getText().trim());
            hl7MessageProvider = new HL7MessageProvider(ip, port);
            hl7MessageProvider.init();
            hl7MessageProvider.setInterval(interval);
            if (hl7MessageProvider.isConnected())
                textArea.append("Connected to Server Successfully \n");
        }catch (Exception e){
            textArea.append(e.getMessage()+"\n");
        }
    }
}
