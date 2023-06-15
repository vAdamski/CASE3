package SocetyV2;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String serverMessage = reader.readLine();
            System.out.println(serverMessage);

            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            writer.println(name);

            Thread messageReceiver = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            messageReceiver.start();

            String message;
            do {
                message = scanner.nextLine();
                if (message.startsWith("PW")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String recipientName = parts[1];
                        String privateMessage = parts[2];
                        writer.println("PW " + recipientName + " " + privateMessage);
                    } else {
                        System.out.println("Invalid private message format. Use: 'PW name message'");
                    }
                } else {
                    writer.println(message);
                }
            } while (!message.equalsIgnoreCase("bye"));

            reader.close();
            writer.close();
            socket.close();

            try {
                messageReceiver.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
