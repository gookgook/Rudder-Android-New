package com.rudder.src.auth.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentSignup1Binding
import com.rudder.src.auth.viewmodel.SignUpViewModel


class SignUp1Fragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var binding: FragmentSignup1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setBinding()
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup1, container, false)
        binding.main = viewModel
        binding.fragment = this
        return binding.root
    }

    fun setBinding() {
        viewModel.emailValidateResultFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                when (status) {
                    1 -> view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_signup1_to_fragment_signup2) }
                    -2 -> Toast.makeText(this.activity, "Something Empty", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this.activity, "Wrong_email_form", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this.activity, "Email_Already_Exist", Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(this.activity, "Agree Please", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.isTermsAgreed.observe(viewLifecycleOwner, Observer { status ->
            if (status) {
                Glide.with(this.activity)
                    .load(android.R.drawable.checkbox_on_background)
                    .fitCenter()
                    .into(binding.termsB)

            } else {
                Glide.with(this.activity)
                    .load(android.R.drawable.checkbox_off_background)
                    .fitCenter()
                    .into(binding.termsB)
            }
        })

        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })
    }

    fun goTerms(){
        Log.d("go touched clicked","sdf")
        view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_signup1_to_terms) }
    }
}