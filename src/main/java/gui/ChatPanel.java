package gui;

import client.ClientNetwork;
import commons.ClientStatus;
import commons.EventMessage;
import commons.Role;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatPanel extends JPanel {

    private final JTextArea chatArea;
    private final JTextField messageField;
    private String welcomeMssg = "[System] Welcome to ZeroBoard\n";
    private String defaultStatusMssg = "[System] Server is online now\n";
    private String userStatusMessage ;

    private Role role;
    private ClientNetwork client;

    public ChatPanel(Role role, ClientNetwork client) {

        this.role = role;
        this.client = client;

        setPreferredSize(new Dimension(0, 130));

        setBackground(new Color(25, 25, 25));

        setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        setLayout(new BorderLayout(8, 8));


        // CHAT AREA

        chatArea = new JTextArea();

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        chatArea.setBackground(
                new Color(40, 40, 40)
        );

        chatArea.setForeground(Color.WHITE);

        chatArea.setText(welcomeMssg);
        if(role == Role.HOST)
        {
            chatArea.setText(defaultStatusMssg);
            //System.out.println(getSystemServerStatusMssg());
        }

        JScrollPane scrollPane =
                new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);


        // MESSAGE INPUT

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));

        inputPanel.setOpaque(false);

        messageField = new JTextField();

        messageField.setToolTipText("Type a message...");

        JButton sendButton = new JButton("Send");

        inputPanel.add(messageField, BorderLayout.CENTER);

        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);


        // SEND MESSAGE

        sendButton.addActionListener(e -> sendMessage());

         messageField.addActionListener(e -> sendMessage());

    }

    //update incoming chatmessage
    public void handleChatMessage(String chatMessage)
    {
        String[] chat = chatMessage.split("\\|");
        if (chat.length >= 3) {
            String username = chat[1];
            String message = chat[2];
            chatArea.append("[" + username + "] " + message + "\n");
        }

        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    // update incoming system message
    public void addSystemMessage(String userStatusMessage) {
        this.userStatusMessage = userStatusMessage;
        String[] part = userStatusMessage.split("\\|");
        if (part.length >= 2) {
            String user = part[1];
            if (userStatusMessage.startsWith(EventMessage.userJoinedEvent)) {
                chatArea.append("[" + user + "] has joined the session.\n");
            } else if (userStatusMessage.startsWith(EventMessage.userLeftEvent)) {
                chatArea.append("[" + user + "] has left the session.\n");
            }
        }
    }

    //send message to server
    private void sendMessage() {

        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        if (client.getClientStatus() != ClientStatus.CONNECTED) {
            chatArea.append("[System] Connect to a server before chatting.\n");
            messageField.setText("");
            return;
        }

        //send entered message to network
        client.send(EventMessage.chatEvent + "|" + client.getUsername() + "|" + message);
        messageField.setText("");


        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

}
