package com.rudder.src.chat.view

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.config.App
import com.rudder.databinding.FragmentChatBinding
import com.rudder.src.chat.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val parentActivity by lazy {
        activity as MainActivity
    }

    private val args : ChatFragmentArgs by navArgs()

    private val lazyContext by lazy {
        context
    }
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setChatRoomId(args.chatRoomId)

    }

    var chatRVOffSet: Int = 0


    var isFirst: Boolean = true

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
        viewModel.getOldChat()
        binding.chatRV.also {
            it.layoutManager = LinearLayoutManager(parentActivity)
            it.setHasFixedSize(false)
            it.adapter = chatListAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    Log.d("offset",it.computeVerticalScrollOffset().toString())

                    if (!it.canScrollVertically(-1) && chatRVOffSet!=0) {
                        Log.d("keyboard", "refresh")
                        viewModel.getOldChat(true)
                    }



                }
            })

            binding.chatBodyET.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    viewModel.isRefreshingFlag = hasFocus
                }

        }


        binding.bigCL.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                chatRVOffSet = binding.chatRV.computeVerticalScrollOffset()
                Log.d("offset2",chatRVOffSet.toString())
                return false
            }
        })

        binding.chatRV.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                chatRVOffSet = binding.chatRV.computeVerticalScrollOffset()
                Log.d("offset2",chatRVOffSet.toString())
                return false
            }
        })

        binding.chatBodyET.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                chatRVOffSet = binding.chatRV.computeVerticalScrollOffset()
                Log.d("offset2",chatRVOffSet.toString())
                return false
            }
        })




        viewModel.scrollToBottomFlag.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.chatRV.scrollToPosition(chatListAdapter.itemCount-1)
            }

        })




        viewModel.chatMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("chatBody","came to fragment")
                chatListAdapter.submitList(it.asReversed().toList())

                if (isFirst) {
                    isFirst = false

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            binding.chatRV.smoothScrollToPosition(viewModel.chatMessages.value!!.size - 1)
                            Log.d("offset", binding.chatRV.computeVerticalScrollOffset().toString())
                        }
                    }, 1300)

                }

            }
        })

        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })

        viewModel.scrollToBottomFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) {
                binding.chatRV.smoothScrollToPosition(viewModel.chatMessages.value?.size!! - 1)
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerEvent()
    }

    override fun onPause(){
        super.onPause()
        viewModel.unregisterEvent()
    }




    private fun scrollToBottom(recyclerView: RecyclerView){
        recyclerView.smoothScrollToPosition(0)
    }
}

