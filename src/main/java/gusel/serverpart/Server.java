package gusel.serverpart;

import gusel.ApplicationConfig;
import gusel.clientpart.Client;
import gusel.messageinfo.ChatHistory;
import gusel.messageinfo.Message;
import gusel.messageinfo.UserList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket clientSocket;
    private ServerSocket serverSocket = null;
    private BufferedReader input = null;
    private ChatHistory chatHistory = new ChatHistory();
    private UserList userList = new UserList();


    public Server(int port){
        try {
            // Create server socket on some port to listen connections
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on IP -> " + serverSocket.getLocalSocketAddress());
            System.out.println("Waiting for a Client...");

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("New client connected!");

                new ClientConnectionThread(clientSocket);
                System.out.println("Thread started!");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Class handler to provide client server interaction
     */
    private class ClientConnectionThread extends Thread {
        private Socket clientSocket;

        public ClientConnectionThread(Socket clientSocket){
            this.clientSocket = clientSocket;
            this.start();;
        }

        @Override
        public void run() {
            String login;
            Message mes;

            try {
                ObjectInputStream input = new ObjectInputStream(this.clientSocket.getInputStream());

                // Receive ping message from user, to complete connection
                mes = (Message) input.readObject();
                login = mes.getUsername();

                ObjectOutputStream output = new ObjectOutputStream(this.clientSocket.getOutputStream());

                System.out.println(mes.getMessage());
                // Update and send user chat history
                chatHistory.updateChatHistory(mes);
                output.writeObject(new Message("Server",
                        "Chat_Bot",
                        "Ввостановление истории сообщений (5 сообщ.): \n " + chatHistory.getChatHistory()));

                // Create new client, add to online list
                Client client = new Client(login, clientSocket, input, output);
                userList.addUser(client);
                sendMsgToAllExceptUser(new Message("Server",
                        "Chat_Bot",
                        "User " + login + " connected!"), client);

                // Loop handler, there we get message from users
                while (true) {
                    mes = (Message) input.readObject();
                    if (mes.getText().equals("/Exit")){
                        break;
                    }
                    // Log in server
                    System.out.println(mes.getMessage());

                    // Send everybody new message
                    sendMsgToAllExceptUser(mes, client);
                }

                output.close();
                input.close();
                clientSocket.close();

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
