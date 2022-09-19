package com.rudder.src.notification.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.databinding.FragmentNotificationBinding
import com.rudder.src.notification.viewmodel.NotificationViewModel


class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding

    private val notificationViewModel: NotificationViewModel by viewModels()

    private val notificationListAdapter by lazy {
        NotificationListAdapter()
    }

    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext= context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        binding.notificationFragment=this

        binding.notificationListRV.also {
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.setHasFixedSize(false)
            it.adapter = notificationListAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        notificationViewModel.getNotifications(isMore = true)
                    }
                }
            })
        }

        binding.notificationListSRL.setOnRefreshListener {
            notificationViewModel.refreshNotifications()
            binding.notificationListSRL.isRefreshing = false
        }

        notificationViewModel.getNotifications()
        notificationViewModel.notificationList.observe(viewLifecycleOwner, Observer {
            notificationListAdapter.submitList(it.toList())
        })

        notificationViewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it.let {
                Toast.makeText(mContext,it,Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }


    fun goBack(){
        requireActivity().onBackPressed()
    }



}