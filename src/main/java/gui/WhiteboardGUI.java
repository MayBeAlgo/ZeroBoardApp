package gui;

import client.ClientNetwork;
import commons.EventMessage;
import commons.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WhiteboardGUI extends JFrame {
     private CanvasPanel whiteboardCanvas;
     private ConnectionPanel connectionPanel;
     private ToolsPanel toolsPanel;
     private ChatPanel chatPanel;
     private SummaryPanel summaryPanel;

    private final Role role;
    private final ClientNetwork client;

    public WhiteboardGUI(Role role,ClientNetwork client) {

        //idetify the role of the user
        this.role = role;
        this.client = client;


        setTitle("ZeroBoard");
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // MAIN CONTAINER

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        add(mainPanel);

        //TOP BAR OF THE BOARD
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // CANVAS
        whiteboardCanvas = new CanvasPanel(client);
        mainPanel.add(whiteboardCanvas, BorderLayout.CENTER);

        //CONNECTION PANEL
        connectionPanel = new ConnectionPanel(client,role);
        mainPanel.add(connectionPanel, BorderLayout.WEST);

        //TOOLS PANEL
        toolsPanel = new ToolsPanel(client,this,whiteboardCanvas);
        mainPanel.add(toolsPanel, BorderLayout.EAST);

        //CHAT PANEL
        chatPanel = new ChatPanel(role,client);
        mainPanel.add(chatPanel, BorderLayout.SOUTH);

        //ADD SUMMARY BUTTON NEAR TOOLS PANEL FOR NOW



        //send connection mssg
        client.setConnectionListener(new ClientNetwork.ConnectionListener() {
            @Override
            public void onConnected(String mssg) {
                     connectionPanel.setConnected(true);
                    connectionPanel.updateUserList(mssg);
                    chatPanel.addSystemMessage(mssg);
            }

            @Override
            public void onDisconnected(String mssg) {
                connectionPanel.setConnected(false);
                chatPanel.addSystemMessage(mssg);
                connectionPanel.updateUserList(mssg);
            }
        });

        //send incoming message to chatpanel
        client.setNetworkListener(message -> {
            System.out.println("Message listener invoked");
            if (message.startsWith(EventMessage.chatEvent)) {
                chatPanel.handleChatMessage(message);
            }
            else if(message.startsWith(EventMessage.drawEvent))
            {
                whiteboardCanvas.drawRemoteLine(message);
            }
            if (message.startsWith(EventMessage.userLeftEvent)) {
                chatPanel.addSystemMessage(message);
                connectionPanel.updateUserList(message);
            }
        });

        //close socket when user is disconnected
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
                dispose();
            }
        });

        //send the summary to summary panel
        client.setSummaryListener(summary -> {
            SwingUtilities.invokeLater(()->
            {
                summaryPanel.updateSummary(summary);
            });
        });

}

    private JPanel createTopBar() {

            JPanel panel = new JPanel(new BorderLayout());

            panel.setPreferredSize(new Dimension(0, 55));
            panel.setBackground(new Color(25, 25, 25));

            JLabel title = new JLabel("  ZeroBoard");

            title.setForeground(Color.WHITE);
            title.setFont(new Font("SansSerif", Font.BOLD, 22));

            JLabel status = new JLabel("LAN COLLABORATIVE WHITEBOARD  ");

            status.setForeground(new Color(150, 150, 150));
            status.setFont(new Font("SansSerif", Font.PLAIN, 12));

            panel.add(title, BorderLayout.WEST);
            panel.add(status, BorderLayout.EAST);

            return panel;
        }

        public void createSummaryPanel()
        {
            if(summaryPanel == null)
            {
                summaryPanel = new SummaryPanel(client);
                summaryPanel.setVisible(true);
            }
        }
    }

