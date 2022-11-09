package com.rudder.src.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentSignup2Binding
import com.rudder.src.auth.viewmodel.SignUpViewModel

class SignUp2Fragment: Fragment() {
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var binding: FragmentSignup2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setBinding()
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup2, container, false)
        binding.main = viewModel
        return binding.root
    }

    fun setBinding() {
        viewModel.profileCheckFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let {

                when (status) {
                    1 -> view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_signup2_to_fragment_signup3) }
                    -2 -> Toast.makeText(this.activity, "Something Empty", Toast.LENGTH_SHORT).show()
                    -3 -> Toast.makeText(this.activity, "Description must be at least 20 characters long", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })
    }

}