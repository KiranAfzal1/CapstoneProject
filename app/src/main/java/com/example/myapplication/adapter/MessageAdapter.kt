package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.model.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val onProfileClick: (String) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.tvSenderName.text = message.senderName
        holder.tvMessage.text = message.message
        holder.ivProfile.setImageResource(R.drawable.ic_person)

        val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(message.timestamp))
        holder.tvTime.text = formattedTime

        val prefs = SharedPrefManager(holder.itemView.context)
        val myId = prefs.getUserId()
        val isMine = message.senderId == myId

        val lp = holder.tvMessage.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            holder.tvMessage.setBackgroundResource(
                if (isMine) R.drawable.bg_message_right else R.drawable.bg_message_left
            )
            if (isMine) {
                lp.marginStart = 100
                lp.marginEnd = 0
            } else {
                lp.marginStart = 0
                lp.marginEnd = 100
            }
            holder.tvMessage.layoutParams = lp
        }

        holder.ivProfile.setOnClickListener { onProfileClick(message.senderId) }
    }

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}
