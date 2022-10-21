package com.rudder.src.application.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.AcceptedPreItemBinding
import com.rudder.model.dto.PartyDto
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ApprovedPartyListAdapter(val onApprovedPartyClickListener: (Int) -> Unit) : ListAdapter<PartyDto.Companion.ApprovedPartyItem, ApprovedPartyListAdapter.ApprovedPartyItemViewHolder>(
    PartyPreviewDiffCallback()
) {
    inner class ApprovedPartyItemViewHolder(
        val approvedPreItemBinding: AcceptedPreItemBinding
    ): RecyclerView.ViewHolder(approvedPreItemBinding.root)

    class PartyPreviewDiffCallback : DiffUtil.ItemCallback<PartyDto.Companion.ApprovedPartyItem>() {
        override fun areItemsTheSame(
            oldItem: PartyDto.Companion.ApprovedPartyItem,
            newItem: PartyDto.Companion.ApprovedPartyItem,
        ): Boolean {
            return newItem.party.partyId == oldItem.party.partyId

        }

        override fun areContentsTheSame(
            oldItem: PartyDto.Companion.ApprovedPartyItem,
            newItem: PartyDto.Companion.ApprovedPartyItem,
        ): Boolean {
            return oldItem.partyGroupChatRoom.recentMessageTime.equals(newItem.partyGroupChatRoom.recentMessageTime)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovedPartyItemViewHolder {
        return ApprovedPartyItemViewHolder(AcceptedPreItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ApprovedPartyItemViewHolder, position: Int) {
        val approvedPartyItem = getItem(position)
        holder.approvedPreItemBinding.approvedPartyItem = approvedPartyItem
        val partyDate = Date(approvedPartyItem.party.partyTime.time)
        val format: DateFormat = SimpleDateFormat("MMM dd")
        holder.approvedPreItemBinding.partyTimeString = format.format(partyDate)
        Glide.with(holder.approvedPreItemBinding.partyThumbnailIV.context)
            .load(approvedPartyItem.party.partyThumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.approvedPreItemBinding.partyThumbnailIV)

        holder.approvedPreItemBinding.approvedPartyItemCL.setOnClickListener {
            onApprovedPartyClickListener(approvedPartyItem.partyGroupChatRoom.chatRoomId)
        }

    }
}