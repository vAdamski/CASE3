package SocetyV2;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 8080;
    static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(sender.getClientName() + ": " + message);
            }
        }
    }

    public static void sendPrivateMessage(String recipientName, String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client.getClientName().equalsIgnoreCase(recipientName)) {
                client.sendMessage("Private message from " + sender.getClientName() + ": " + message);
                return;
            }
        }
        sender.sendMessage("User with the name " + recipientName + " not found.");
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            writer.println("Enter your name:");
            name = reader.readLine();
            System.out.println(name + " has joined the server.");

            String message;
            do {
                message = reader.readLine();
                if (message.startsWith("ALL")) {
                    String broadcastMessage = message.substring(4);
                    Server.broadcastMessage(broadcastMessage, this);
                } else if (message.startsWith("PW")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String recipientName = parts[1];
                        String privateMessage = parts[2];
                        Server.sendPrivateMessage(recipientName, privateMessage, this);
                    } else {
                        writer.println("Invalid private message format. Use: 'PW name message'");
                    }
                } else if (!message.equalsIgnoreCase("bye")) {
                    writer.println("Invalid message format. Use 'PW {Nick} {message}' / 'ALL {message}.");
                }
            } while (!message.equalsIgnoreCase("bye"));

            Server.broadcastMessage(name + " has left the server.", this);

            reader.close();
            writer.close();
            clientSocket.close();
            Server.clients.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getClientName() {
        return name;
    }
}
