package com.example.chatgptapp.chatView;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.R;
import com.example.chatgptapp.model.Msg;

public class ChatViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Override
    public int getItemViewType(int position) {
        Msg msg = ChatView.getChatList().get(position);
        return msg.getType();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        if (viewType == Msg.TYPE_RECEIVED) {
            Log.d("holder left", "init");
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new LeftViewHolder(itemView);
        } else if (viewType == Msg.TYPE_SENT) {
            Log.d("holder right", "init");
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new RightViewHolder(itemView);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Msg msg = ChatView.getChatList().get(position);


        if (holder instanceof LeftViewHolder) {
            Log.d("holder left", msg.getContent());
            ((LeftViewHolder) holder).bindData(msg);
        } else if (holder instanceof RightViewHolder) {
            Log.d("holder right", msg.getContent());
            ((RightViewHolder) holder).bindData(msg);
        }
    }

    @Override
    public int getItemCount() {

        return ChatView.getChatList().size();
    }


    public static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView leftMsg;

        LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMsg = itemView.findViewById(R.id.leftMsg);
        }

        public void bindData(Msg msg) {

            leftMsg.setText(msg.getContent().replace("\\n", "\n"));
        }
    }

    public static class RightViewHolder extends RecyclerView.ViewHolder {

        TextView rightMsg;

        RightViewHolder(@NonNull View itemView) {
            super(itemView);
            rightMsg = itemView.findViewById(R.id.rightMsg);
        }

        public void bindData(Msg msg) {

            rightMsg.setText(msg.getContent());
        }
    }
}
