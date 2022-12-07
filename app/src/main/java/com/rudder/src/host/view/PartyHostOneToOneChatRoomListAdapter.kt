package com.rudder.src.host.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.PartyHostOneToOneChatRoomItemBinding
import com.rudder.model.dto.ChatDto

class PartyHostOneToOneChatRoomListAdapter(val onHostOneToOneChatRoomClickListener: (Int) -> Unit) : ListAdapter<ChatDto.Companion.PartyOneToOneChatRoom,PartyHostOneToOneChatRoomListAdapter.PartyHostOneToOneChatRoomItemViewHolder>(
    PartyHostOneToOneChatRoomDiffCallback()
){

    inner class PartyHostOneToOneChatRoomItemViewHolder(
        val partyHostOneToOneChatRoomItemBinding: PartyHostOneToOneChatRoomItemBinding
    ): RecyclerView.ViewHolder(partyHostOneToOneChatRoomItemBinding.root)

    class PartyHostOneToOneChatRoomDiffCallback : DiffUtil.ItemCallback<ChatDto.Companion.PartyOneToOneChatRoom>() {
        override fun areItemsTheSame(
            oldItem: ChatDto.Companion.PartyOneToOneChatRoom,
            newItem: ChatDto.Companion.PartyOneToOneChatRoom,
        ): Boolean {
            return newItem.chatRoomId == oldItem.chatRoomId

        }

        override fun areContentsTheSame(
            oldItem: ChatDto.Companion.PartyOneToOneChatRoom,
            newItem: ChatDto.Companion.PartyOneToOneChatRoom,
        ): Boolean {
            try {
                return newItem.recentMessageTime.equals(oldItem.recentMessageTime)
            }catch (e: Exception){
                return false
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyHostOneToOneChatRoomItemViewHolder {
        return PartyHostOneToOneChatRoomItemViewHolder(
            PartyHostOneToOneChatRoomItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: PartyHostOneToOneChatRoomItemViewHolder, position: Int) {
        val partyOneToOneChatRoom = getItem(position)

        holder.partyHostOneToOneChatRoomItemBinding.partyHostOneToOneChatRoomCL.setOnClickListener {
            onHostOneToOneChatRoomClickListener(partyOneToOneChatRoom.chatRoomId)
        }
        holder.partyHostOneToOneChatRoomItemBinding.partyOneToOneChatRoom = partyOneToOneChatRoom

        Glide.with(holder.partyHostOneToOneChatRoomItemBinding.chatRoomImageIV.context)
            .load(partyOneToOneChatRoom.chatRoomImageUrl)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.partyHostOneToOneChatRoomItemBinding.chatRoomImageIV)


    }
}