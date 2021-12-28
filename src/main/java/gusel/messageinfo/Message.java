package gusel.messageinfo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to make message, that can be delivered from client to server
 * Contains user IP, username, time of message, message text,
 * List of all users(only their usernames)
 */
public class Message implements Serializable {
    private String userIP;
    private String username;
    private String text;
    private String time;

    public Message(String userIP, String username, String text){
        this.userIP = userIP;
        this.username = username;
        this.text = text;
        this.time = new SimpleDateFormat("dd/MM/yy '-' HH:mm:ss").format(new Date());
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public void setText(String newMText) {
        this.text = newMText;
    }

    public String getUserIP() {
        return userIP;
    }

    /**
     * Get Message as String
     * @return - String bigMes
     */
    public String getMessage(){
        return "[IP:" + userIP + "]" + " "
                + time + " " +
                username + ": " + text;
    }



}
