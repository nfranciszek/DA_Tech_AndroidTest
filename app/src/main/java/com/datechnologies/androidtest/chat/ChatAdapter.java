package com.datechnologies.androidtest.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.api.ChatLogMessageModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A recycler view adapter used to display chat log messages in {@link ChatActivity}.

 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

   // private List<ChatLogMessageModel> chatLogMessageModelList;

    private ArrayList<ChatLogMessageModel> chatLogMessageModelList;


    //==============================================================================================
    // Constructor
    //==============================================================================================

    public ChatAdapter()
    {

        chatLogMessageModelList = new ArrayList<>();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void setChatLogMessageModelList(ArrayList<ChatLogMessageModel> chatLogMessageModelList)
    {
        this.chatLogMessageModelList = chatLogMessageModelList;
        chatLogMessageModelList.addAll(chatLogMessageModelList);
        notifyDataSetChanged();
    }

    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {



        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_chat, parent, false);

        return new ChatViewHolder(itemView);



    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position)
    {
        ChatLogMessageModel chatLogMessageModel = chatLogMessageModelList.get(position);

        viewHolder.messageTextView.setText(chatLogMessageModel.message);

        viewHolder.usernameView.setText(chatLogMessageModel.name);

        Picasso.get().load(chatLogMessageModel.avatar_url).into(viewHolder.avatarImageView);



    }

    @Override
    public int getItemCount()
    {
        return chatLogMessageModelList.size();
    }










    //==============================================================================================
    // ChatViewHolder Class
    //==============================================================================================

    public static class ChatViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView avatarImageView;
        TextView messageTextView;
        TextView usernameView;

        public ChatViewHolder(View view)
        {
            super(view);

            usernameView = view.findViewById(R.id.usernameView);
            avatarImageView = view.findViewById(R.id.userAvatarImage);
            messageTextView = view.findViewById(R.id.messageTextView);
        }
    }

}
