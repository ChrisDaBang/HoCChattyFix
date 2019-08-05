package gr16.android.chattyfix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gr16.android.chattyfix.R;
import gr16.android.chattyfix.interfaces.ItemClickListener;
import gr16.android.chattyfix.model.ChatMessage;

public class ChatRoomRecyclerAdapter extends RecyclerView.Adapter<ChatRoomRecyclerAdapter.ChatRoomViewHolder>
{
    private ArrayList<ChatMessage> chatMessages;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public ChatRoomRecyclerAdapter (Context context, ArrayList<ChatMessage> chatMessages)
    {
        Log.d("DEBUG", "ChatRoomRecyclerAdapter: Creating adapter object");
        this.inflater = LayoutInflater.from(context);
        this.chatMessages = chatMessages;
        Log.d("DEBUG", "ChatRoomRecyclerAdapter: Created.");
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chat, parent, false);
        ChatRoomViewHolder vh = new ChatRoomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position)
    {
        String message;
        if (chatMessages.get(position).getMessageText() != null)
        {
            message = chatMessages.get(position).getMessageText();
        }
        else
        {
            message = "Deleted Message";
        }
        String user;
        if (chatMessages.get(position).getMessageUser() != null)
        {
            user = chatMessages.get(position).getMessageUser();
        }
        else
        {
            user = "Anonymous User";
        }
        Date date = new Date();
        date.setTime(chatMessages.get(position).getMessageTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        String time = dateFormat.format(date);
        if (false) //User has profile image
        {
            //Make actual implementation of getting avatar image from the authenticated users platform.
        }
        else
        {
            holder.avatarImage.setImageResource(R.drawable.pngtreecuteanimal);
        }



        //Give data to intended holder.
        holder.messageText.setText(message);
        holder.nameText.setText(user);
        holder.dateText.setText(time);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }



    public class ChatRoomViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView avatarImage;
        public TextView nameText;
        public TextView messageText;
        public TextView dateText;

        public ChatRoomViewHolder(View v)
        {
            super(v);
            avatarImage = itemView.findViewById(R.id.chatroom_avater_image_view);
            nameText = itemView.findViewById(R.id.chatroom_name_text_view);
            messageText = itemView.findViewById(R.id.chatroom_chattext_text_view);
            dateText = itemView.findViewById(R.id.chatroom_date_text_view);
        }
    }

    // convenience method for getting data at click position
    public ChatMessage getItem(int id) {
        return chatMessages.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
