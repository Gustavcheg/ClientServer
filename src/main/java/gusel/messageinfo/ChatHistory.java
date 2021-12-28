package gusel.messageinfo;

import gusel.ApplicationConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Chat History
 * Class contains messages for 5 times sent
 *
 */
public class ChatHistory implements Serializable {
    private ArrayList<Message> chatHistoryList;

    public ChatHistory(){
        chatHistoryList = new ArrayList<>(ApplicationConfig.HISTORY_LENGTH);

    }

    /**
     * Update chat history
     * Size got from properties.file
     * @param message - Message message to write
     */
    public void updateChatHistory(Message message) {
        // Check array if is it filled with messages
        if(chatHistoryList.size() == ApplicationConfig.HISTORY_LENGTH) {
            chatHistoryList.remove(0);
        }
        chatHistoryList.add(message);
    }

    /**
     * Get chat history in String
     * @return - String history
     */
    public String getChatHistory() {
        String history  = "";

        for (Message m : chatHistoryList) {
            history += m.getMessage() + "\n";
        }
        return history;
    }

}
