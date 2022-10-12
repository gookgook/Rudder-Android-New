package com.rudder.src.notification.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rudder.databinding.NotificationItemBinding
import com.rudder.model.dto.NotificationDto
import com.rudder.util.LocaleUtil
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class NotificationListAdapter : ListAdapter<NotificationDto.Companion.Notification, NotificationListAdapter.NotificationItemViewHolder>(
    NotificationDiffCallback()
){

    inner class NotificationItemViewHolder(
        val notBinItemBinding: NotificationItemBinding
    ) : RecyclerView.ViewHolder(notBinItemBinding.root)

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationDto.Companion.Notification>() {
        override fun areItemsTheSame(
            oldItem: NotificationDto.Companion.Notification,
            newItem: NotificationDto.Companion.Notification,
        ): Boolean {
            return oldItem.notificationId == newItem.notificationId

        }

        override fun areContentsTheSame(
            oldItem: NotificationDto.Companion.Notification,
            newItem: NotificationDto.Companion.Notification,
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewHolder {
        return NotificationItemViewHolder(NotificationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        val notification = getItem(position)
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(holder.notBinItemBinding.root.context)).format(Date(notification.notificationTime.time)).replace(" ago","")
        holder.notBinItemBinding.notification = getItem(position)
        holder.notBinItemBinding.timeago = timeago

    }
}