package com.example.chatgptapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.model.Msg;
import com.example.chatgptapp.R;

import java.util.List;

public class ChatIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Msg> msgList;

    public ChatIndexAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @Override
    public int getItemViewType(int position) {
        Msg msg = msgList.get(position);
        return msg.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Msg.TYPE_RECEIVED) {
            View view = inflater.inflate(R.layout.item_chat_left, parent, false);
            return new LeftViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_chat_right, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Msg msg = msgList.get(position);

        if (holder instanceof LeftViewHolder) {
            LeftViewHolder leftViewHolder = (LeftViewHolder) holder;
            leftViewHolder.leftMsg.setText(msg.getContent().replace("\\n", "\n"));
        } else if (holder instanceof RightViewHolder) {
            RightViewHolder rightViewHolder = (RightViewHolder) holder;
            rightViewHolder.rightMsg.setText(msg.getContent());
        } else {
            throw new IllegalArgumentException("Invalid view holder type");
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView leftMsg;

        LeftViewHolder(View view) {
            super(view);
            leftMsg = view.findViewById(R.id.leftMsg);
        }
    }

    static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView rightMsg;

        RightViewHolder(View view) {
            super(view);
            rightMsg = view.findViewById(R.id.rightMsg);
        }
    }
}
