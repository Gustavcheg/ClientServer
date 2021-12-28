package gusel.serverpart;

import gusel.ApplicationConfig;
import gusel.clientpart.Client;
import gusel.messageinfo.ChatHistory;
import gusel.messageinfo.Message;
import gusel.messageinfo.UserList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Server class provides main server functions
 * Receive message from clients
 * Send message to clients
 * Create new client thread
 */
public class Server {
    private Socket clientSocket;
    private ServerSocket serverSocket = null;
    private ChatHistory chatHistory = new ChatHistory();
    private UserList userList = new UserList();


    public Server(int port){
        try {
            // Create server socket on some port to listen connections
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on IP -> " + serverSocket.getLocalSocketAddress());
            System.out.println("Waiting for a Client...");

            // Listen for new client connections
            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("New client connected!");

                new ClientConnectionThread(clientSocket);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Class handler to provide client server interaction
     */
    private class ClientConnectionThread extends Thread {
        private final Socket clientSocket;

        public ClientConnectionThread(Socket clientSocket){
            this.clientSocket = clientSocket;
            this.start();
        }

        @Override
        public void run() {
            String login;
            Message mes;

            try {
                ObjectInputStream input = new ObjectInputStream(this.clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(this.clientSocket.getOutputStream());

                // Receive ping message from user, to complete connection
                mes = (Message) input.readObject();
                login = mes.getUsername();
                // Create client
                Client client = new Client(login, clientSocket, input, output);

                System.out.println(mes.getMessage());
                // Update and send user chat history
                chatHistory.updateChatHistory(mes);
                client.getClientOutput().writeObject(new Message("Server",
                        "Chat_Bot",
                        "Ввостановление истории сообщений (5 сообщ.): \n " + chatHistory.getChatHistory()));

                userList.addUser(client);
                sendMsgToAllExceptUser(new Message("Server",
                        "Chat_Bot",
                        "User " + login + " connected!"), client);

                // Loop handler, there we get message from users
                while (true) {
                    mes = (Message) client.getClientInput().readObject();
                    if (mes.getText().equals("Disconnected from server!")){
                        // Send reset message to close ServerHandler (in ClientSender
                        sendMsgToAllUsers(mes);
                        break;
                    }
                    // Log in server
                    System.out.println(mes.getMessage());

                    // Send everybody new message
                    sendMsgToAllExceptUser(mes, client);
                }

                // Close all io threads and socket
                output.close();
                input.close();
                clientSocket.close();
                // Delete user from online lists
                userList.deleteUser(login);

            }
            catch (IOException | ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }

        /**
         * Method to send message to all online users
         * @param message - Message message
         */
        public void sendMsgToAllUsers(Message message) {
            try {
                for (Client client : userList.getClients()) {
                    client.getClientOutput().writeObject(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /**
         * Method to send message everybody, but not user
         * Broadcast new messages for every body
         * User how send message won't see his message
         * @param message - Message message
         * @param clientComp - Client clientComp (to compare)
         */
        public void sendMsgToAllExceptUser(Message message, Client clientComp) {
            try {
                for (Client client : userList.getClients()) {
                    if (!client.equals(clientComp)) {
                        client.getClientOutput().writeObject(message);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Server(ApplicationConfig.PORT);
    }
}
