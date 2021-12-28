package gusel.messageinfo;

import gusel.clientpart.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides online users list
 * Can delete users,
 */
public class UserList {
    private Map<String, Client> onlineUsersList = new HashMap<>();

    /**
     * Add new user in online users list (connect)
     * @param - user, login
     */
    public int addUser(Client user) {
        if(!this.onlineUsersList.containsKey(user.getLogin())){
            this.onlineUsersList.put(user.getLogin(), user);
            return 0;
        } else {
            System.out.println("Client with this login exists! Create another login");
            return -1;
        }
    }

    /**
     * Delete user by login (disconnect user)
     * @param login
     */
    public void deleteUser(String login) {
        this.onlineUsersList.remove(login);
    }

    /**
     * Get String[] array with users login
     */
    public String[] getUsers() {
        return this.onlineUsersList.keySet().toArray(new String[0]);
    }

    /**
     * Get all online Client, their sockets, inputStream, outputStream
     * @return - ArrayList<ChatClient>
     */
    public Collection<Client> getClients() {
        return this.onlineUsersList.values();
    }
}
