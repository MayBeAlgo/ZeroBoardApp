package tests;


public class ChatMessageTest {

    public static void main(String[] args) {

        System.out.println("Running chat message test...");

        testChatMessage();

        System.out.println("All chat tests PASSED.");
    }

    private static void testChatMessage() {

        String message =
                "CHAT|Mayank|Hello everyone";

        String[] parts =
                message.split("\\|", 3);

        assert parts.length == 3 :
                "Incorrect chat message format";

        assert parts[0].equals("CHAT") :
                "Incorrect message type";

        assert parts[1].equals("Mayank") :
                "Incorrect username";

        assert parts[2].equals("Hello everyone") :
                "Incorrect chat message";

        System.out.println("✓ Chat message test passed");
    }
}
