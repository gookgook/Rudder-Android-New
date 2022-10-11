package com.rudder.src.host.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.databinding.ApplicantProfileImageItemBinding

class ApplicantProfileImageListAdapter : ListAdapter<String,ApplicantProfileImageListAdapter.ApplicantProfileImageItemViewHolder>(
    ApplicantProfileImageDiffCallback()
){

    inner class ApplicantProfileImageItemViewHolder(
        val applicantProfileImageItemBinding: ApplicantProfileImageItemBinding
    ): RecyclerView.ViewHolder(applicantProfileImageItemBinding.root)

    class ApplicantProfileImageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return newItem.equals(oldItem)

        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return newItem.equals(oldItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantProfileImageItemViewHolder {
        return ApplicantProfileImageItemViewHolder(
            ApplicantProfileImageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ApplicantProfileImageItemViewHolder, position: Int) {
        val applicantProfileImageUrl = getItem(position)


        Glide.with(holder.applicantProfileImageItemBinding.applicantProfileImageIV.context)
            .load(applicantProfileImageUrl)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.applicantProfileImageItemBinding.applicantProfileImageIV)


    }
}