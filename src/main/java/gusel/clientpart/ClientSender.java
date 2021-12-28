package gusel.clientpart;

import gusel.ApplicationConfig;
import gusel.messageinfo.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClientSender class provides main client functions
 * Connect to server
 * Send message to the server
 * Receive message from server
 * ~Disconnect from server
 */
public class ClientSender {
    private ObjectOutputStream output = null;
    private Socket socket = null;
    private final Message messageFromServer = null;

    public ClientSender(String ip, int port){
        try {
            // Read text from user
            Scanner scan = new Scanner(System.in);

            // Create socket connection with server
            socket = new Socket(ip, port);
            // Welcome-messages
            System.out.println("Вас приветсвует клиентская часть чата!");
            System.out.println("Подклюечение к серверу -> " + ip + ":" + port);
            System.out.println("Пожалуйста введите свой логин (Enter - ввод): ");
            // User input login
            String login = scan.nextLine();

            // Create output stream to send messages
            output = new ObjectOutputStream(socket.getOutputStream());

            // Send "ping" message
            output.writeObject(new Message("Server", login, "Connected to Server!"));

            // Sleep for a few seconds
            // Printing is very fast
            try {
                System.out.println("Подключение...");
                Thread.sleep(1000);
                // Create server listener
                new ServerListenerThread(socket, login);
                Thread.sleep(1000);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            String messageText = "";

            System.out.println("Чат подключён, вводите сообщения (Enter - оптравить): ");

            // Create Message sender
            while (true){
                System.out.print(socket.getInetAddress().toString() + " " + login + ": ");
                messageText = scan.nextLine();

                // Exit
                if (messageText.equals("/Exit")){
                    // Send bye message
                    output.writeObject(new Message(socket.getInetAddress().toString(),
                            login,
                            "Disconnected from server!"));

                    break;
                } else {
                    // Send message
                    output.writeObject(new Message(socket.getInetAddress().toString(),
                            login,
                            messageText));
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Class ServerListenerThread
     * Listen for server answers
     * Like message from other users
     * Chat history
     */
    private static class ServerListenerThread extends Thread {
        private final Socket socket;
        private final String login;

        public ServerListenerThread(Socket socket, String login) {
            this.socket = socket;
            this.login = login;
            this.start();
        }

        @Override
        public void run() {
            try {
                Message mes;
                ObjectInputStream serverMessages = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    mes = (Message) serverMessages.readObject();

                    // Exit when server send bye message
                    if(mes.getText().equals("Disconnected from server!")) {
                        break;
                    } else {
                        System.out.print("\n" + mes.getMessage());
                    }

                    System.out.print("\n" + socket.getInetAddress().toString() + " " + login + ": ");
                }

            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ClientSender(ApplicationConfig.IP, ApplicationConfig.PORT);
    }
}
