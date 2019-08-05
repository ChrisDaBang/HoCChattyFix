package gr16.android.chattyfix;

import androidx.appcompat.app.AppCompatActivity;
import gr16.android.chattyfix.model.ChatMessage;
import gr16.android.chattyfix.model.ChatRoom;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ChatRoom chatRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent lastIntent = getIntent();
        String chatRoomName = lastIntent.getExtras().getString("roomName");


        //FirebaseFirestore
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();

        chatRoom = new ChatRoom(chatRoomName, chatMessages);
    }
}
