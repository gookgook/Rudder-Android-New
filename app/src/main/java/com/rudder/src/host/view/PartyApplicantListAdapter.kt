package com.rudder.src.host.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.PartyApplicantItemBinding
import com.rudder.model.dto.PartyDto

class PartyApplicantListAdapter(val onPartyApplicantClickListener: (Int, Int, Boolean) -> Unit) : ListAdapter<PartyDto.Companion.PartyApplicant,PartyApplicantListAdapter.PartyApplicantItemViewHolder>(
    PartyApplicantDiffCallback()
){

    inner class PartyApplicantItemViewHolder(
        val partyApplicantItemBinding: PartyApplicantItemBinding
    ): RecyclerView.ViewHolder(partyApplicantItemBinding.root)

    class PartyApplicantDiffCallback : DiffUtil.ItemCallback<PartyDto.Companion.PartyApplicant>() {
        override fun areItemsTheSame(
            oldItem: PartyDto.Companion.PartyApplicant,
            newItem: PartyDto.Companion.PartyApplicant,
        ): Boolean {
            return newItem.partyMemberId == oldItem.partyMemberId

        }

        override fun areContentsTheSame(
            oldItem: PartyDto.Companion.PartyApplicant,
            newItem: PartyDto.Companion.PartyApplicant,
        ): Boolean {
            return newItem.partyMemberId == oldItem.partyMemberId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyApplicantItemViewHolder {
        return PartyApplicantItemViewHolder(
            PartyApplicantItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: PartyApplicantItemViewHolder, position: Int) {
        val partyApplicant = getItem(position)

        holder.partyApplicantItemBinding.partyApplicant = partyApplicant
        holder.partyApplicantItemBinding.partyApplicantItem.setOnClickListener {
            onPartyApplicantClickListener(partyApplicant.partyMemberId, partyApplicant.userInfoId, partyApplicant.isChatExist)
        }

        Glide.with(holder.partyApplicantItemBinding.partyApplicantImageIV.context)
            .load(partyApplicant.partyProfileImageUrl)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.partyApplicantItemBinding.partyApplicantImageIV)


    }
}