package com.rudder.src.application.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.AppliedPreItemBinding
import com.rudder.model.dto.PartyDto

class AppliedPartyListAdapter(val onAppliedPartyClickListener: (PartyDto.Companion.AppliedPartyItem) -> Unit) : ListAdapter<PartyDto.Companion.AppliedPartyItem,AppliedPartyListAdapter.AppliedPartyItemViewHolder>(
    PartyPreviewDiffCallback()
){

    inner class AppliedPartyItemViewHolder(
        val appliedPreItemBinding: AppliedPreItemBinding
    ): RecyclerView.ViewHolder(appliedPreItemBinding.root)

    class PartyPreviewDiffCallback : DiffUtil.ItemCallback<PartyDto.Companion.AppliedPartyItem>() {
        override fun areItemsTheSame(
            oldItem: PartyDto.Companion.AppliedPartyItem,
            newItem: PartyDto.Companion.AppliedPartyItem,
        ): Boolean {
            return newItem.party.partyId == oldItem.party.partyId

        }

        override fun areContentsTheSame(
            oldItem: PartyDto.Companion.AppliedPartyItem,
            newItem: PartyDto.Companion.AppliedPartyItem,
        ): Boolean {
            val newRecentMessage = newItem.partyOneToOneChatRoom.recentMessage ?: ""
            val oldRecentMessage = oldItem.partyOneToOneChatRoom.recentMessage ?: ""
            return oldRecentMessage.equals(newRecentMessage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedPartyItemViewHolder {
        return AppliedPartyItemViewHolder(AppliedPreItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: AppliedPartyItemViewHolder, position: Int) {
        val appliedPartyItem = getItem(position)



        if (appliedPartyItem.partyOneToOneChatRoom.partyId.equals(-1)){
            Glide.with(holder.appliedPreItemBinding.appliedPartyImageIV.context)
                .load(appliedPartyItem.party.partyThumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.appliedPreItemBinding.appliedPartyImageIV)
            holder.appliedPreItemBinding.appliedPreTitleTV.text = appliedPartyItem.party.partyTitle
            holder.appliedPreItemBinding.appliedPartyItemCL.setOnClickListener {
                onAppliedPartyClickListener(appliedPartyItem)
            }
        }else{
            Glide.with(holder.appliedPreItemBinding.appliedPartyImageIV.context)
                .load(appliedPartyItem.partyOneToOneChatRoom.chatRoomImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.appliedPreItemBinding.appliedPartyImageIV)
            holder.appliedPreItemBinding.appliedPreTitleTV.text = appliedPartyItem.partyOneToOneChatRoom.chatRoomTitle
            holder.appliedPreItemBinding.appliedPreStatusTV.text = appliedPartyItem.partyOneToOneChatRoom.recentMessage
            holder.appliedPreItemBinding.appliedPartyItemCL.setOnClickListener {
                onAppliedPartyClickListener(appliedPartyItem)
            }
        }

    }
}