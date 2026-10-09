package gui;

import client.ClientNetwork;
import commons.EventMessage;
import model.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class CanvasPanel extends JPanel{

    private Line line;
    private Color brushColor = Color.BLACK;
    private int brushSize = 4;

    private int prevX,prevY;
    boolean drawing = false;

    private Graphics2D g2;

    java.util.List<Line> lines = new ArrayList<>();

    private ClientNetwork client;
    public CanvasPanel(ClientNetwork client) {

        this.client = client;
        setBackground(Color.WHITE);

        setBorder(
                BorderFactory.createLineBorder(
                        new Color(60, 60, 60),
                        1
                )
        );


    //MAIN DRAWING LOGIC

    addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            prevX = e.getX();
            prevY = e.getY();
            drawing =  true;
        }
        @Override
        public void mouseReleased(MouseEvent e)
        {
            drawing = false;
        }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
             line = new Line();
            int currentX = e.getX();
            int currentY = e.getY();
            if(!drawing)
            {
                return;
            }
            line.setX1(prevX) ;
            line.setY1(prevY);
            line.setX2(currentX);
            line.setY2(currentY);
            line.setBrushColor(brushColor);
            line.setBrushSize(brushSize);
            lines.add(line);

            repaint();
            String message = EventMessage.drawEvent+"|"+ prevX + "|" + prevY + "|" + currentX + "|" + currentY + "|" + line.getBrushColor().getRGB() + "|" + line.getBrushSize();

            //send data to network
            client.send(message);

            prevX = currentX;
            prevY = currentY;
        }

    });
}

public void clearCanvas()
{
    lines.clear();
}
public void drawRemoteLine(String message)
{
    // Implementation for drawing remote line
        String[] parts = message.split("\\|");
        if (parts.length >= 7) {
           // Line line = new Line();
            int x1 = Integer.parseInt(parts[1]);
            int y1 = Integer.parseInt(parts[2]);
            int x2 = Integer.parseInt(parts[3]);
            int y2 = Integer.parseInt(parts[4]);
            Color color = new Color(Integer.parseInt(parts[5]));
            int size = Integer.parseInt(parts[6]);

            SwingUtilities.invokeLater(() -> {

                Line line = new Line();

                line.setX1(x1);
                line.setY1(y1);
                line.setX2(x2);
                line.setY2(y2);
                line.setBrushColor(color);
                line.setBrushSize(size);

                lines.add(line);

                repaint();
            });

    }
}


public void paintComponent(Graphics g)
{
    super.paintComponent(g);
    g2 = (Graphics2D) g;


    //for each new line objects it keeps drawing it
    for(Line line : lines)
    {
        g2.setStroke(new BasicStroke(line.getBrushSize()));
        g2.setColor(line.getBrushColor());
        g2.drawLine(line.getX1(),line.getY1(),line.getX2(),line.getY2());
    }
}

   //Undo and redo option
    public void undoLastLine()
    {

    }

    public void redoLastLine()
    {

    }

public void setBrushColor(Color color) {

        this.brushColor = color;
    }

    public Color getBrushColor() {

        return brushColor;
    }

    public void setBrushSize(int size) {

        this.brushSize = size;
    }

    public int getBrushSize() {

        return brushSize;
    }
}
