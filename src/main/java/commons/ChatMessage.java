package commons;

public class ChatMessage {
    private static String systemConnectedMessage = "Connected to server ";
    private static String systemDisconnectedMessage = " Disconnected from server";
    private String sentMessage;

    public static String getSystemConnectedMessage()
    {
        return systemConnectedMessage;
    }
    public static String getSystemDisconnectedMessage()
    {
        return systemDisconnectedMessage;
    }
    public void setSentMessage(String sentMessage)
    {
        this.sentMessage=sentMessage;
    }
    public String getSentMessage()
    {
        return sentMessage;
    }
}
