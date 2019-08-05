package gr16.android.chattyfix.adapters;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gr16.android.chattyfix.R;
import gr16.android.chattyfix.interfaces.ItemClickListener;


//Big thanks to Suragch -> https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example for improving standard implementation.
public class ChatRoomsRecyclerAdapter extends RecyclerView.Adapter<ChatRoomsRecyclerAdapter.ChatRoomsViewHolder> {
    private ArrayList<String> chatRoomNames;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public ChatRoomsRecyclerAdapter(Context context, ArrayList<String> chatRoomNames)
    {
        this.inflater = LayoutInflater.from(context);
        this.chatRoomNames = chatRoomNames;
    }

    @NonNull
    @Override
    public ChatRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        ChatRoomsViewHolder vh = new ChatRoomsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomsViewHolder holder, int position) {
        String chatRoom = chatRoomNames.get(position);
        holder.roomBtn.setText(chatRoom);

    }

    @Override
    public int getItemCount() {
        return chatRoomNames.size();
    }

    public class ChatRoomsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public Button roomBtn;

        public ChatRoomsViewHolder(View v)
        {
            super(v);
            roomBtn = itemView.findViewById(R.id.btn_roomname);
            itemView.setOnClickListener(this);
            roomBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return chatRoomNames.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
