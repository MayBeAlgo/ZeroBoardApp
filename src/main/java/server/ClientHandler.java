package server;

import commons.EventMessage;
import model.AiResponse;
import service.AIService;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;

    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private final AIService aiService = new AIService();

    public ClientHandler(Socket socket, Server server) {

        this.socket = socket;
        this.server = server;
        try {
            //reads socket messages
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {

        try {

            Object object;

            //broadcast normal messages and handle summarize
            while ((object = objectInputStream.readObject()) != null) {

                System.out.println("DEBUG: Received object: " + object);

                if (object instanceof String) {
                    String message = (String) object;
                    if (message.equals(EventMessage.aiSummaryEvent)) {
                        List<String> chats = server.getChatHistory();

                        //new thread start so that ui ,and chat is not getting blocked
                        Thread.startVirtualThread(() -> {
                            try {
                                AiResponse summaryObject = aiService.summarize(chats);
                                //send to object stream
                                send(summaryObject);
                            } catch (Exception e) {
                                System.out.println("DEBUG: Exception in virtual thread: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    } else {
                        System.out.println(message);
                        server.broadcast(message, this);
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {

            //IF ANYTHING GOES WRONG DISCONNET THE CLIENT AND CLOSE THE SOCKET
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    //send the aiReponse object to network
    public synchronized void send(Object object) throws IOException {
        if(objectOutputStream!=null)
        {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        }
    }

}