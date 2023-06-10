package com.example.chatgptapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatgptapp.Msg
import com.example.chatgptapp.R
import java.lang.IllegalArgumentException

/**
 * @author :lwh
 * @date : 2023/3/13
 */
class ChatIndexAdapter(val msgList: List<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftMsg: TextView = view.findViewById(R.id.leftMsg);
    }

    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rightMsg: TextView = view.findViewById(R.id.rightMsg);
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == Msg.TYPE_RECEIVED) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
            LeftViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right, parent, false)
            RightViewHolder(view)
        }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]

        when(holder){
            is LeftViewHolder -> {
                holder.leftMsg.text = ""
                holder.leftMsg.text = msg.content
            }
            is RightViewHolder -> {
                holder.rightMsg.text = ""
                holder.rightMsg.text = msg.content
            }
            else -> throw IllegalArgumentException()
        }
    }
}