package tests;

import model.Line;

import java.awt.*;

public class LineTest {

    public static void main(String[] args) {

        System.out.println("Running Line tests...");

        testCoordinates();
        testColor();
        testBrushSize();

        System.out.println("All Line tests PASSED.");
    }

    private static void testCoordinates() {

        Line line = new Line();

        line.setX1(100);
        line.setY1(200);
        line.setX2(300);
        line.setY2(400);

        assert line.getX1() == 100 :
                "X1 is incorrect";

        assert line.getY1() == 200 :
                "Y1 is incorrect";

        assert line.getX2() == 300 :
                "X2 is incorrect";

        assert line.getY2() == 400 :
                "Y2 is incorrect";

        System.out.println("✓ Coordinates test passed");
    }

    private static void testColor() {

        Line line = new Line();

        line.setBrushColor(Color.RED);

        assert line.getBrushColor().equals(Color.RED) :
                "Brush color is incorrect";

        System.out.println("✓ Color test passed");
    }

    private static void testBrushSize() {

        Line line = new Line();

        line.setBrushSize(10);

        assert line.getBrushSize() == 10 :
                "Brush size is incorrect";

        System.out.println("✓ Brush size test passed");
    }
}
