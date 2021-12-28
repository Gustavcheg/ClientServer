package gusel.messageinfo;

import gusel.ApplicationConfig;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatHistory implements Serializable {
    private ArrayList<Message> chatHistoryList;

    public ChatHistory(){
        chatHistoryList = new ArrayList<>(ApplicationConfig.HISTORY_LENGTH);

    }

    public void updateChatHistory(Message message) {
        if(chatHistoryList.size() == ApplicationConfig.HISTORY_LENGTH) {
            chatHistoryList.remove(0);
        }
        chatHistoryList.add(message);
    }

    public String getChatHistory() {
        String history  = "";

        for (Message m : chatHistoryList) {
            history += m.getMessage() + "\n";
        }
        return history;
    }

}
