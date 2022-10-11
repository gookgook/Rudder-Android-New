package com.rudder.src.chat.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.config.App
import com.rudder.databinding.FragmentChatBinding
import com.rudder.src.chat.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val parentActivity by lazy {
        activity as MainActivity
    }

    private val lazyContext by lazy {
        context
    }
    private val viewModel: ChatViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //viewModel.channelId 지정해주셈
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.main = viewModel
        binding.setLifecycleOwner(this)
        val userInfoId = App.prefs.getValue("userInfoId")
        val chatListAdapter = ChatListAdapter(userInfoId!!.toInt())
        binding.chatRV.also {
            it.layoutManager = LinearLayoutManager(parentActivity)
            it.setHasFixedSize(false)
            it.adapter = chatListAdapter
        }
        //viewModel.getChatMessages(chatRoomId)
        viewModel.connectChatRoomSocket()
        //viewModel.getOldChat()

        viewModel.chatMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("chatBody","came to fragment")
                chatListAdapter.submitList(it.asReversed().toList())
                scrollToBottom(binding.chatRV)

            }
        })
        return binding.root
    }

    private fun scrollToBottom(recyclerView: RecyclerView){
        recyclerView.smoothScrollToPosition(0)
    }
}