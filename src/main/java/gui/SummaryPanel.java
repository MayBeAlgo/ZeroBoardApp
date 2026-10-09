package gui;

import client.ClientNetwork;
import model.AiResponse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SummaryPanel extends JFrame {
    private ClientNetwork clientNetwork;
    private JTextArea textArea;
    private Timer timer;
    private JPanel panel;
    private String aiText = "";

    public SummaryPanel(ClientNetwork clientNetwork)
    {
        this.clientNetwork = clientNetwork;
        panel = new JPanel();
        setTitle("ZeroBot Ai Chat Summary");
        setSize(500, 400);
        setLocationRelativeTo(null);
        panel.setBackground(new Color(35, 35, 35));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addTextArea();
        generatingSummaryAnimation();

        add(panel);

    }
    public void updateSummary(AiResponse aiResponse)
    {
        timer.stop();
        if(aiResponse.getCandidates()!=null && !aiResponse.getCandidates().isEmpty())
        {
            aiText = aiResponse.getCandidates().getFirst()
                    .getContent()
                    .getParts().getFirst()
                    .getText();
            System.out.println("DEBUG: AI Response Text: " + aiText);
        }
        SwingUtilities.invokeLater(() -> {
            textArea.setText(aiText);
            textArea.setCaretPosition(0);
        });
    }
    private void addTextArea()
    {
        textArea = new JTextArea(15,45);
        textArea.setBackground(new Color(45, 45, 45));
        textArea.setForeground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 350));
        panel.add(scrollPane);
    }
    private void generatingSummaryAnimation()
    {
         timer = new Timer(300, e -> {
            String text = "Generating";

                textArea.setText(text + "...");

        });
        timer.start();
    }
}
