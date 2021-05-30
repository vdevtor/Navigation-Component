package com.example.navigationcomponentapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentProfileBinding
import com.example.navigationcomponentapp.ui.login.LoginViewModel

class ProfileFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private var binding: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.authenticationStateEvent.observe(viewLifecycleOwner,{authenticationState ->
            when(authenticationState){
                is LoginViewModel.AuthenticationState.Authenticated->{
                    binding?.textProfileUserName?.text = getString(R.string.profile_text_username,loginViewModel.userName)
                }
                is LoginViewModel.AuthenticationState.UnAuthenticated ->{
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        })
    }

}