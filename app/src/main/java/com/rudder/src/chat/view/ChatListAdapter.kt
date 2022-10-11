package com.rudder.src.chat.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rudder.databinding.MyChatItemBinding
import com.rudder.databinding.YourChatItemBinding
import com.rudder.databinding.YourFirstChatItemBinding
import com.rudder.model.dto.ChatDto

class ChatListAdapter constructor(_userInfoId: Int): ListAdapter<ChatDto.Companion.Chat, RecyclerView.ViewHolder>(
    ChatDiffCallback()
){
    val userInfoId: Int
    init{
        userInfoId = _userInfoId
    }
    inner  class MyChatItemViewHolder(
        val myChatItemBinding: MyChatItemBinding
    ) : RecyclerView.ViewHolder(myChatItemBinding.root)

    inner class YourChatItemViewHolder(
        val yourChatItemBinding: YourChatItemBinding
    ) : RecyclerView.ViewHolder(yourChatItemBinding.root)

    inner class YourFirstChatItemViewholder(
        val yourFirstChatItemBinding: YourFirstChatItemBinding
    ) : RecyclerView.ViewHolder(yourFirstChatItemBinding.root)

    class ChatDiffCallback: DiffUtil.ItemCallback<ChatDto.Companion.Chat>(){
        override fun areItemsTheSame(
            oldItem: ChatDto.Companion.Chat,
            newItem: ChatDto.Companion.Chat
        ): Boolean {
            return oldItem.chatMessageId == newItem.chatMessageId
        }

        override fun areContentsTheSame(
            oldItem: ChatDto.Companion.Chat,
            newItem: ChatDto.Companion.Chat
        ): Boolean {
            return oldItem.chatMessageTime == newItem.chatMessageTime
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).sendUserInfoId != userInfoId) {
            if(position == 0 || getItem(position).sendUserInfoId != getItem(position-1).sendUserInfoId) return 3
            else return 1
        }
        else return 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            return YourChatItemViewHolder(YourChatItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        }

        if (viewType == 3){
            return YourFirstChatItemViewholder(YourFirstChatItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        }


        return  MyChatItemViewHolder(MyChatItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatItem = getItem(position)
        if (getItemViewType(position) == 2) {
            (holder as MyChatItemViewHolder).myChatItemBinding.chatBodyTV.text = chatItem.chatMessageBody
        } else if (getItemViewType(position) == 1){
            (holder as YourChatItemViewHolder).yourChatItemBinding.chatBodyTV.text = chatItem.chatMessageBody
        } else {
            (holder as YourFirstChatItemViewholder).yourFirstChatItemBinding.chatBodyTV.text = chatItem.chatMessageBody
            (holder as YourFirstChatItemViewholder).yourFirstChatItemBinding.nicknameTV.text = chatItem.sendUserNickname
        }
    }

}