package client;

import commons.ClientStatus;
import commons.EventMessage;
import commons.NetworkListener;
import commons.SummaryListener;
import model.AiResponse;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientNetwork {

    private Socket socket;

    private String ip;
    private String port;
    private String username;

    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private ClientStatus clientStatus;

    private ConnectionListener connectionListener;
    private NetworkListener networkListener;
    private JButton connectButtonStatus;
    private SummaryListener summaryListener;

    // ---------------- CONNECTION LISTENER ----------------


    public interface ConnectionListener {

        void onConnected(String mssg);
        void onDisconnected(String mssg);
    }

    public void setConnectionListener(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    public void setNetworkListener(NetworkListener networkListener)
    {
        this.networkListener = networkListener;
    }

    public void setSummaryListener(SummaryListener summaryListener) { this.summaryListener = summaryListener; }

    // ---------------- CONNECT ----------------

    public void connect(String ip, String port, String username) {

        this.ip = ip;
        this.port = port;
        this.username = username;

        try {

            if (socket != null && !socket.isClosed()) {
                return; // already connected
            }

            socket = new Socket(ip, Integer.parseInt(port));
            connectButtonStatus.setText("Disconnect");
            clientStatus = ClientStatus.CONNECTED;

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to " + ip + ":" + port);

            startReceiver();

            if (connectionListener != null) {
                connectionListener.onConnected("");
            }


        } catch (IOException e) {

            if (connectionListener != null) {
                connectionListener.onDisconnected("");
                JOptionPane.showMessageDialog(null, "Connection failed: RETRY  " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            }

            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    //UPDATE IF CLIENT IS CONNECTED
    public void updateConnectButtonStatus(JButton connectButtonStatus){
        this.connectButtonStatus = connectButtonStatus;
    }


    // ---------------- SEND ----------------
    //GET THE MESSAGE FROM GUI AND SEND IT TO SERVER
    //SEND THE MESSAGE TO SERVER
    public synchronized void send(Object object) {

        if (objectOutputStream != null) {
            try {
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // ---------------- RECEIVE ----------------

    //START A RECIEVER THREAD FOR EACH CLIENT
    private void startReceiver() {

        Thread.startVirtualThread(() -> {

            try {

                Object object;
                while ((object = objectInputStream.readObject()) != null) {

                    System.out.println("From server: " + object);

                    if(object instanceof String) {
                        String message = (String) object;
                        if (message.startsWith(EventMessage.userJoinedEvent)) {
                            connectionListener.onConnected(message);
                        } else if (message.startsWith(EventMessage.chatEvent)) {
                            networkListener.onMessageRecieved(message);
                        } else if (message.startsWith(EventMessage.drawEvent)) {
                            networkListener.onMessageRecieved(message);
                        } else if (message.startsWith(EventMessage.userLeftEvent)) {
                            networkListener.onMessageRecieved(message);
                        }
                    }
                    if(object instanceof AiResponse) {
                        AiResponse aiResponse = (AiResponse) object;
                        summaryListener.onSummaryReceived(aiResponse);
                    }

                }

            } catch (IOException e) {

                System.out.println("Disconnected from server. exception");

                if (connectionListener != null) {
                    connectionListener.onDisconnected(EventMessage.userLeftEvent+"|"+username);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


    // ---------------- DISCONNECT ----------------

    public void disconnect() {

        try {
            if (socket != null && !socket.isClosed()) {

                // Tell server WHICH user is leaving
                objectOutputStream.writeObject(EventMessage.userLeftEvent + "|" + username);
                objectOutputStream.flush();

                socket.close();
            }
        } catch (IOException ignored) {
        }
    }


    // ---------------- GETTERS / SETTERS ----------------

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

}