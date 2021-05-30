package com.example.navigationcomponentapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigationcomponentapp.R

class LoginViewModel : ViewModel() {
    sealed class AuthenticationState {
        object Authenticated : AuthenticationState()
        object UnAuthenticated : AuthenticationState()
        class InvalidAuthentication(val fields: List<Pair<String, Int>>) : AuthenticationState()
    }
    var userName: String = ""
    private var token: String = ""
    private val _authenticationStateEvent = MutableLiveData<AuthenticationState>()

    val authenticationStateEvent : LiveData<AuthenticationState>
    get() = _authenticationStateEvent

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication() {
        _authenticationStateEvent.value = AuthenticationState.UnAuthenticated
    }

    fun authenticateToken(token:String,username: String){
        this.token = token
        this.userName = username
        _authenticationStateEvent.value = AuthenticationState.Authenticated
    }
    fun authentication(username: String, password: String) {
        if (isValidForm(username, password)) {
            this.userName = username
            _authenticationStateEvent.value = AuthenticationState.Authenticated
        }
    }

    private fun isValidForm(username: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (username.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            _authenticationStateEvent.value = AuthenticationState.InvalidAuthentication(invalidFields)
            return false
        }
        return true
    }

    companion object {
        val INPUT_USERNAME = "INPUT_USERNAME" to R.string.login_input_layout_error_invalid_username
        val INPUT_PASSWORD = "INPUT_PASSWORD" to R.string.login_input_layout_error_invalid_password
    }
}