package gr16.android.chattyfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gr16.android.chattyfix.adapters.ChatRoomsRecyclerAdapter;
import gr16.android.chattyfix.model.ChatMessage;
import gr16.android.chattyfix.model.ChatRoom;

import android.content.Intent;
import android.os.Build;
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
import java.util.Comparator;

public class ChatRoomsActivity extends AppCompatActivity implements ChatRoomsRecyclerAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ChatRoomsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText chatRoomName;

    private FirebaseFirestore database;
    private ArrayList<ChatRoom> chatRooms = new ArrayList<>();
    private ArrayList<String> chatRoomNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms);
        recyclerView = findViewById(R.id.chatrooms_recyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        //        layoutManager.getLayoutDirection());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        //Get the data from Firebase
        database = FirebaseFirestore.getInstance();

        //Initial load
        loadChatRooms(true);


        chatRoomName = findViewById(R.id.chatroom_create_name_edittext);
        Button createChatroomBtn = findViewById(R.id.chatroom_create_button);

        createChatroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                chatMessages.add(new ChatMessage("Hello Chris", "NotChris"));
                chatMessages.add(new ChatMessage("Hi not me...", "Chris"));
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
                loadChatRooms(false); //Reload it instantly. Should be moved to a pull feature with its own button?
            }
        });
    }

    private void loadChatRooms(final Boolean firstLoad)
    {
        CollectionReference chatRoomsDB = database.collection("chatRooms");
        chatRoomsDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {

                    chatRooms = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        chatRoomNames.add(document.get("roomName").toString());
                        chatRooms.add(document.toObject(ChatRoom.class));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)  //Currently list is only sorted by newest for android api versions at or above 24.
                    {
                        chatRooms.sort(new Comparator<ChatRoom>() {
                            @Override
                            public int compare(ChatRoom chatRoom, ChatRoom t1) {
                                long diff = t1.getLastActivityTime() - chatRoom.getLastActivityTime();
                                return (int)diff;
                            }
                        });
                        int i = 0;
                        chatRoomNames = new ArrayList<>(); //Clear lists so they are not displayed repeatingly.
                        for (ChatRoom chatRoom : chatRooms)
                        {
                            chatRoomNames.add(i, chatRoom.getRoomName());
                            i++;
                        }
                    }
                    mAdapter = new ChatRoomsRecyclerAdapter(ChatRoomsActivity.this, chatRoomNames);
                    mAdapter.setClickListener(ChatRoomsActivity.this);

                    if (firstLoad) {recyclerView.setAdapter(mAdapter); }
                    else { recyclerView.swapAdapter(mAdapter, true); }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ChatRoomsActivity.this, ChatRoomActivity.class);
        i.putExtra("roomName", chatRoomNames.get(position));
        startActivity(i);
        finish();
    }
}
