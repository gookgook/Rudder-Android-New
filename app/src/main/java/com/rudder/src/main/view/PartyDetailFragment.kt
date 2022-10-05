package com.rudder.src.main.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentPartyDetailBinding
import com.rudder.databinding.FragmentPartyMainBinding
import com.rudder.src.main.viewmodel.PartyDetailViewModel
import kotlinx.android.synthetic.main.party_preview_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PartyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPartyDetailBinding

    private val args: PartyDetailFragmentArgs by navArgs()
    private val partyDetailViewModel: PartyDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val partyId = args.partyId
        partyDetailViewModel.getPartyDetail(partyId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentPartyDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner=this
        binding.partyDetailFragment = this
        partyDetailViewModel.partyDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.partyDetail=it
                Glide.with(binding.partyDetailImageIV.context)
                    .load(it.partyThumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.partyDetailImageIV)

                val partyDate = Date(it.partyTime.time)
                val format: DateFormat = SimpleDateFormat("MMM dd")
                binding.partyDate = format.format(partyDate)

                binding.partyApplyB.also { button->
                    when(it.partyStatus){
                        "FINAL_APPROVE" -> {
                            button.text = "Approved"
                        }
                        "HOST" -> button.text = "You are the HOST"
                        "REJECT" -> button.text = "DONE"
                        "PENDING" -> button.text = "Applied"
                        else -> {
                            button.background=ContextCompat.getDrawable(requireContext(),R.drawable.button_style)
                            button.isEnabled = true
                        }
                    }
                }
            }

        })



        return binding.root
    }


    fun showPartyApplyDialog(){
        PartyApplyDialogFragment(args.partyId).show(childFragmentManager,"PartyApplyDialogFragment")
    }

}