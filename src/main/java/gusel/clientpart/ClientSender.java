package gusel.clientpart;

import gusel.ApplicationConfig;
import gusel.messageinfo.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender {
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Socket socket = null;
    private Message messageFromServer = null;

    public ClientSender(String ip, int port){
        try {
            Scanner scan = new Scanner(System.in);

            socket = new Socket(ip, port);



            System.out.println("Вас приветсвует клиентская часть чата!");
            System.out.println("Подклюечение к серверу -> " + ip + ":" + port);
            System.out.println("Пожалуйста введите свой логин (Enter - ввод): ");
            String login = scan.nextLine();


            output = new ObjectOutputStream(socket.getOutputStream());

            output.writeObject(new Message("Server-Bot:", login, "Connected to Server!"));

            try {
                System.out.println("Подключение...");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            new ServerListenerThread(socket, login);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            String messageText = "";

            System.out.println("Чат подключён, вводите сообщения (Enter - оптравить): ");

            while (true){
                System.out.print(socket.getInetAddress().toString() + " " + login + ": ");
                messageText = scan.nextLine();

                if (messageText.equals("/Exit")){
                    System.out.println("Disconnected from server!");
                    output.writeObject(new Message(socket.getInetAddress().toString(),
                            login,
                            "Disconnected from server!"));
                    output.close();
                    //socket.close();
                    break;
                } else {
                    output.writeObject(new Message(socket.getInetAddress().toString(),
                            login,
                            messageText));
                }

            }

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private class ServerListenerThread extends Thread {
        private Socket socket;
        private String login;

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

                    System.out.print("\n" + mes.getMessage());

                    System.out.print("\n" + socket.getInetAddress().toString() + " " + login + ": ");

                }

            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ClientSender client = new ClientSender(ApplicationConfig.IP, ApplicationConfig.PORT);
    }
}
