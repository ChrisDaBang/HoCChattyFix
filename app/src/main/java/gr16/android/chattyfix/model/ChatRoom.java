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

        correctLastActivityTime(chatMessages);
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
        correctLastActivityTime(chatMessages);
        this.chatMessages = chatMessages;
    }

    /**
     * check the time for each message. Save that time as latest activity time if it is greater than the current.
     * @param chatMessages
     */
    private void correctLastActivityTime(ArrayList<ChatMessage> chatMessages)
    {
        long lastActivityTime = 0;
        for (ChatMessage message : chatMessages)
        {
            if (message.getMessageTime() > lastActivityTime)
            {
                this.lastActivityTime = message.getMessageTime();
            }
        }
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
