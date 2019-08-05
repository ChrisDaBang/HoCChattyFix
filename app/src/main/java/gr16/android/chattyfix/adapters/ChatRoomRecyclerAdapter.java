package gr16.android.chattyfix.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomRecyclerAdapter extends RecyclerView.Adapter<ChatRoomRecyclerAdapter.ChatRoomsViewHolder>
{

    @NonNull
    @Override
    public ChatRoomRecyclerAdapter.ChatRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomRecyclerAdapter.ChatRoomsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class ChatRoomsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public ChatRoomsViewHolder(View v)
        {
            super(v);
        }
        @Override
        public void onClick(View view) {

        }
    }
}
