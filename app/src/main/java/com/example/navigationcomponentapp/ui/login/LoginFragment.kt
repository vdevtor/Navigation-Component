package com.example.navigationcomponentapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setObservable()

        binding?.buttonLoginSignIn?.setOnClickListener {
            val userName = binding?.inputLoginUsername?.text.toString()
            val passWord = binding?.inputLoginPassword?.text.toString()
            viewModel.authentication(userName, passWord)
        }
    }

    fun setObservable() {
        viewModel.authenticationStateEvent.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                    val validationsFields: Map<String, TextInputLayout?> = initValidationFields()
                    authenticationState.fields.forEach { fieldsError->
                        validationsFields[fieldsError.first]?.error = getString(fieldsError.second)
                    }
                }
            }
        })
    }

    private fun initValidationFields() = mapOf(
            LoginViewModel.INPUT_USERNAME.first to binding?.inputLayoutLoginUsername,
            LoginViewModel.INPUT_PASSWORD.first to binding?.inputLayoutLoginPassword
    )

}