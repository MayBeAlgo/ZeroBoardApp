package server;

import commons.EventMessage;
import service.AIService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final int port;

    private final Set<ClientHandler> clientList = ConcurrentHashMap.newKeySet();
    private final List<String> drawingHistory = new ArrayList<>();
    private final List<String> chatHistory = new ArrayList<>();
    private String previousSummary = "";
    private AIService aiService;

    public Server(int port) {
        this.port = port;
        this.aiService = new AIService();
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);
            System.out.println("Waiting for clients...");

            //ACCEPT ALL INCOMING CLIENT REQUESTS
            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("Client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, this);

                clientList.add(handler);

                //SEND CURRENT BOARD STATE
                sendDrawingHistory(handler,socket);

                //START NEW THREAD FOR EACH CLIENT
                Thread.startVirtualThread(handler);

            }

        } catch (IOException e) {

            System.out.println("Server stopped: " + e.getMessage());
        }
    }

    //BROADCAST RECIEVED MESSAGE TO ALL THE CLIENTS
    public synchronized void broadcast(String message, ClientHandler sender) throws IOException {

        //SAVE ALL CHAT HISTORY *for summarization*
        if(message.startsWith(EventMessage.chatEvent))
        {
            chatHistory.add(message);
        }
        //SAVE ALL DRAWING HISTORY
        if (message.startsWith(EventMessage.drawEvent)) {

            drawingHistory.add(message);
        }
        for (ClientHandler client : clientList) {

            //SYNCHRONIZED
            client.send(message);
        }
    }

    //SEND DRAWING HISTORY TO NEW JOINING CLIENTS
    private synchronized void sendDrawingHistory(ClientHandler client,Socket sender) throws IOException {

        System.out.println("Sending drawing history to new client...");

        for (String drawing : drawingHistory) {
           // if (client != sender) {
                client.send(drawing);
          //  }

            //System.out.println(drawing);
        }

        System.out.println("Drawing history sent: " + drawingHistory.size() + " lines");
    }

    public List<String> getChatHistory()
    {
        return chatHistory;
    }

    public void removeClient(ClientHandler client) {

        clientList.remove(client);

        System.out.println("Client disconnected. Active clients: " + clientList.size());
    }
}