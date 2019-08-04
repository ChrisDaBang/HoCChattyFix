package gr16.android.chattyfix.model;

import java.util.ArrayList;

public class ChatRoom
{
    private String roomName;
    private ArrayList<ChatMessage> chatMessages;
    private long lastActivityTime;

    public ChatRoom(String roomName, ArrayList<ChatMessage> chatMessages)
    {
        this.roomName = roomName;
        this.chatMessages = chatMessages;

        // get the time from the latest chat message.
    }

    public ChatRoom()
    {

    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }
}
