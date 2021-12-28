package gusel.clientpart;

import java.io.*;
import java.net.Socket;

/**
 * Client class
 * Contains information about client
 * Login, socket, output, input
 * Uses in online users list
 * Send messages
 */
public class Client implements Serializable {
    private Socket socket;
    private ObjectInputStream clientInput = null;
    private ObjectOutputStream clientOutput = null;
    String login;

    public Client(String login, Socket socket, ObjectInputStream clientInput, ObjectOutputStream clientOutput){
        this.socket = socket;
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
        this.login = login;
    }

    public ObjectInputStream getClientInput() {
        return clientInput;
    }

    public ObjectOutputStream getClientOutput() {
        return clientOutput;
    }

    public String getLogin() {
        return this.login;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setClientInput(ObjectInputStream clientInput) {
        this.clientInput = clientInput;
    }

    public void setClientOutput(ObjectOutputStream clientOutput) {
        this.clientOutput = clientOutput;
    }
}
