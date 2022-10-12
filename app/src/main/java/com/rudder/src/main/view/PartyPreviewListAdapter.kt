package com.rudder.src.main.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.PartyPreviewItemBinding
import com.rudder.model.dto.PartyDto
import kotlinx.android.synthetic.main.party_preview_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PartyPreviewListAdapter(val onPartyPreviewClickListener: (Int) -> Unit) : ListAdapter<PartyDto.Companion.PartyPreview, PartyPreviewListAdapter.PartyPreviewItemViewHolder>(
    PartyPreviewDiffCallback()
){

    inner class PartyPreviewItemViewHolder(
        val partyPreviewItemBinding: PartyPreviewItemBinding
    ) : RecyclerView.ViewHolder(partyPreviewItemBinding.root)

    class PartyPreviewDiffCallback : DiffUtil.ItemCallback<PartyDto.Companion.PartyPreview>() {
        override fun areItemsTheSame(
            oldItem: PartyDto.Companion.PartyPreview,
            newItem: PartyDto.Companion.PartyPreview,
        ): Boolean {
            return oldItem.partyId == newItem.partyId

        }

        override fun areContentsTheSame(
            oldItem: PartyDto.Companion.PartyPreview,
            newItem: PartyDto.Companion.PartyPreview,
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyPreviewItemViewHolder {
        return PartyPreviewItemViewHolder(PartyPreviewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: PartyPreviewItemViewHolder, position: Int) {
        val partyPreview = getItem(position)
        holder.partyPreviewItemBinding.partyPreview = partyPreview

        val partyDate = Date(partyPreview.partyTime.time)
        val format: DateFormat = SimpleDateFormat("MMM dd")
        holder.partyPreviewItemBinding.partyTimeString = format.format(partyDate)



        Glide.with(holder.itemView.partyImageIV.context)
            .load(partyPreview.partyThumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.itemView.partyImageIV)

        Glide.with(holder.itemView.universityLogoIV.context)
            .load(partyPreview.universityLogoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.itemView.universityLogoIV)

        holder.partyPreviewItemBinding.partyPreviewItemCL.setOnClickListener{
            onPartyPreviewClickListener(partyPreview.partyId)
        }
    }
}