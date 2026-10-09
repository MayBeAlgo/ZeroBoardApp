package client;

import commons.Role;
import gui.WhiteboardGUI;

import javax.swing.*;

public class ClientApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

           ClientNetwork client = new ClientNetwork();

            WhiteboardGUI whiteboard = new WhiteboardGUI(Role.GUEST,client);

            whiteboard.setVisible(true);
        });
    }
}
