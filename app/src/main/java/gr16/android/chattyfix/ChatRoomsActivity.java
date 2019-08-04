package gr16.android.chattyfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gr16.android.chattyfix.adapters.ChatRoomsRecyclerAdapter;
import gr16.android.chattyfix.model.ChatMessage;
import gr16.android.chattyfix.model.ChatRoom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatRoomsActivity extends AppCompatActivity implements ChatRoomsRecyclerAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ChatRoomsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText chatRoomName;

    private FirebaseFirestore database;
    private ArrayList<String> chatRoomNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        recyclerView = findViewById(R.id.chatrooms_recyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get the data from Firebase
        database = FirebaseFirestore.getInstance();

        CollectionReference chatRoomsDB = database.collection("chatRooms");
        chatRoomsDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        chatRoomNames.add(document.get("roomName").toString());
                    }
                    mAdapter = new ChatRoomsRecyclerAdapter(ChatRoomsActivity.this, chatRoomNames);
                    mAdapter.setClickListener(ChatRoomsActivity.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        chatRoomName = findViewById(R.id.chatroom_create_name_edittext);
        Button createChatroomBtn = findViewById(R.id.chatroom_create_button);

        createChatroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                CollectionReference chatRoomsDB = database.collection("chatRooms");
                chatRoomsDB.add(new ChatRoom(chatRoomName.getText().toString(), chatMessages))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                chatRoomName.setText("");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
