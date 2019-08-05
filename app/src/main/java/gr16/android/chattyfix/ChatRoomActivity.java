package gr16.android.chattyfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gr16.android.chattyfix.adapters.ChatRoomRecyclerAdapter;
import gr16.android.chattyfix.interfaces.ItemClickListener;
import gr16.android.chattyfix.model.ChatMessage;
import gr16.android.chattyfix.model.ChatRoom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChatRoomActivity extends AppCompatActivity implements ItemClickListener {

    private FirebaseFirestore database;
    DocumentReference chatRoomDocument;
    private ChatRoom chatRoom;

    private RecyclerView recyclerView;
    private ChatRoomRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView chatRoomNameView;
    private ImageButton sendButton;
    private EditText chatText;
    private FirebaseAuth firebaseAuth;
    FirebaseUser authUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatroom_rv);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent lastIntent = getIntent();
        final String chatRoomName = lastIntent.getExtras().getString("roomName");
        final String chatRoomID = lastIntent.getExtras().getString("roomID");

        chatRoomNameView = findViewById(R.id.chatroom_roomname_textview);
        chatRoomNameView.setText(chatRoomName);

        firebaseAuth = FirebaseAuth.getInstance();
        authUser = firebaseAuth.getCurrentUser();

        //FirebaseFirestore
        database = FirebaseFirestore.getInstance();
        chatRoomDocument = database.collection("chatRooms").document(chatRoomID);
        Log.d("DEBUG", "onCreate: attempting firestore get()");
        chatRoomDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                if (document.exists())
                {
                    Log.d("DEBUG", "onComplete: document exists");
                    chatRoom = document.toObject(ChatRoom.class);
                    Log.d("DEBUG", "onComplete: trying object messages array to string " + chatRoom.getChatMessages().toString());
                }
                else
                {
                    Log.d("DEBUG", "onComplete: document does not exist");
                }

                mAdapter = new ChatRoomRecyclerAdapter(ChatRoomActivity.this, chatRoom.getChatMessages());
                mAdapter.setClickListener(ChatRoomActivity.this);
                recyclerView.setAdapter(mAdapter);
            }
        });

        chatRoomDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() { //Update chat as it is updated on Firestore.
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("DEBUG", "Listen failed.", e);
                    return;
                }

                if (document != null && document.exists()) {
                    Log.d("DEBUG", "Current data: " + document.getData());
                    chatRoom = document.toObject(ChatRoom.class);
                    mAdapter = new ChatRoomRecyclerAdapter(ChatRoomActivity.this, chatRoom.getChatMessages());
                    mAdapter.setClickListener(ChatRoomActivity.this);
                    recyclerView.swapAdapter(mAdapter, true);

                } else {
                    Log.d("DEBUG", "Current data: null");
                }
            }
        });


        chatText = findViewById(R.id.chatroom_user_text_edit);
        chatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND)
                {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        sendButton = findViewById(R.id.chatroom_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    private void sendMessage()
    {
        String userName;
        if (!authUser.getDisplayName().contentEquals(""))
        {
            userName = authUser.getDisplayName();
        }
        else
        {
            userName = "Anonymous User";
        }
        ChatMessage newMessage = new ChatMessage(chatText.getText().toString(), userName);
        chatRoom.getChatMessages().add(newMessage);
        chatRoom.setLastActivityTime(newMessage.getMessageTime());
        chatRoomDocument.set(chatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DEBUG", "onSuccess: chatRoom and messages updated to Firestore");
                Toast.makeText(ChatRoomActivity.this,"Your message was sent", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG", "onFailure: chatRoom and messages not updated to Firestore");
            }
        });
        chatText.setText("");
    }

    @Override
    public void onItemClick(View view, int position) {
        //Nothing happens when chat items are clicked.
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ChatRoomsActivity.class);
        startActivity(i);
        finish();
    }
}
