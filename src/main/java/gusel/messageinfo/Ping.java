package gusel.messageinfo;

/**
 * Ping message
 * Rewrite
 * Make this message for server (and make another class -
 * message from user, implement message);
 */
public class Ping {
    private String ping;

    public Ping(String ping) {
        this.ping = ping;
    }

    public String getMessage() {
        return ping;
    }
}
