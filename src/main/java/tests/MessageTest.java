package tests;

import java.awt.*;

public class MessageTest {

    public static void main(String[] args) {

        System.out.println("Running message tests...");

        testDrawMessage();

        System.out.println("All message tests PASSED.");
    }

    private static void testDrawMessage() {

        String message =
                "DRAW|100|200|300|400|"
                        + Color.RED.getRGB()
                        + "|5";

        String[] parts = message.split("\\|");

        assert parts.length == 7 :
                "Incorrect number of message fields";

        assert parts[0].equals("DRAW") :
                "Incorrect event type";

        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);
        int rgb = Integer.parseInt(parts[5]);
        int size = Integer.parseInt(parts[6]);

        assert x1 == 100 :
                "Incorrect X1";

        assert y1 == 200 :
                "Incorrect Y1";

        assert x2 == 300 :
                "Incorrect X2";

        assert y2 == 400 :
                "Incorrect Y2";

        assert rgb == Color.RED.getRGB() :
                "Incorrect color";

        assert size == 5 :
                "Incorrect brush size";

        System.out.println("✓ DRAW message test passed");
    }
}
