package host;

import client.ClientNetwork;
import commons.NetworkConfig;
import commons.Role;
import gui.WhiteboardGUI;
import server.Server;


public class HostApp {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            ClientNetwork client = new ClientNetwork();

            WhiteboardGUI gui = new WhiteboardGUI(Role.HOST,client);
            gui.setVisible(true);
        });

        //SERVER START
        Server server = new Server(NetworkConfig.PORT);
       // server.start();
        Thread.startVirtualThread(server::start);
    }
}