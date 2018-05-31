package tiagodccosta.assestcoimbra;

import android.widget.TextView;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String adminAns;
    private String key;
    private String id;

    public ChatMessage(String messageText, String messageUser, String adminAns, String id) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.adminAns = adminAns;
        this.id = id;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminAns() {
        return adminAns;
    }

    public void setAdminAns(String adminAns) {
        this.adminAns = adminAns;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(ChatMessage newChatMessage) {
        messageText = newChatMessage.messageText;
        messageUser = newChatMessage.messageUser;
        messageTime = newChatMessage.messageTime;
        adminAns = newChatMessage.adminAns;
    }

    public String getKey() {
        return key;
    }
}
