package gui;

import client.ClientNetwork;
import commons.EventMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolsPanel extends JPanel {
    private CanvasPanel canvas;
    private ClientNetwork clientNetwork;
    private SummaryPanel summaryPanel;
    private WhiteboardGUI whiteboardGUI;
    public ToolsPanel(ClientNetwork clientNetwork,WhiteboardGUI whiteboardGUI,CanvasPanel canvas) {

        this.clientNetwork = clientNetwork;
        this.whiteboardGUI = whiteboardGUI;
        this.canvas = canvas;

        setPreferredSize(new Dimension(100, 0));

        setBackground(new Color(35, 35, 35));

        setBorder(new EmptyBorder(15, 10, 15, 10));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        // TITLE

        JLabel title = new JLabel("TOOLS");

        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);

        add(Box.createVerticalStrut(20));


        // COLOR
        JButton colorButton = createToolButton("Color");

        colorButton.addActionListener(e -> {

            Color selected = JColorChooser.showDialog(this, "Choose Color", canvas.getBrushColor());

            if (selected != null) {
                canvas.setBrushColor(selected);
            }
        });

        add(colorButton);

        add(Box.createVerticalStrut(10));


        // BRUSH SIZE
        JButton brushButton = createToolButton("Brush");

        brushButton.addActionListener(e -> {

            String value = JOptionPane.showInputDialog(this, "Brush size:", canvas.getBrushSize());

            if (value != null) {

                try {

                    int size = Integer.parseInt(value);

                    if (size > 0 && size <= 50) {

                        canvas.setBrushSize(size);
                    }

                } catch (NumberFormatException ignored) {
                }
            }
        });

        add(brushButton);

        add(Box.createVerticalStrut(10));


        // ERASER
        JButton eraserButton = createToolButton("Eraser");

        eraserButton.addActionListener(e -> {

            canvas.setBrushColor(Color.WHITE);
        });
        add(eraserButton);

        add(Box.createVerticalStrut(10));


        // CLEAR
        JButton clearButton = createToolButton("Clear");

        clearButton.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(this, "Clear whiteboard?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {

                canvas.clearCanvas();
                canvas.repaint();
            }
        });

        add(clearButton);

        add(Box.createVerticalGlue());

        //AI SUMMARY BUTTON
        JButton aiSummaryButton = new JButton("ZeroBot");
        aiSummaryButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        aiSummaryButton.addActionListener(e -> {
            whiteboardGUI.createSummaryPanel();
            clientNetwork.send(EventMessage.aiSummaryEvent);
        });

        add(aiSummaryButton);
        aiSummaryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //***NO SUITABLE IMPLEMTATION YET*******


//        //UNDO OPTION
//        JButton undoButton = createToolButton("Undo");
//
//        undoButton.addActionListener(e -> {
//            canvas.undoLastLine();
//        });
//
//       add(undoButton);
//       add(Box.createVerticalStrut(10));
//
//       //REDO OPTION
//        JButton redoButton = createToolButton("Redo");
//
//        redoButton.addActionListener(e -> {
//            canvas.redoLastLine();
//        });
//
//        add(redoButton);
//        add(Box.createVerticalGlue());

    }


    private JButton createToolButton(String text) {

        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return button;
    }
    }

