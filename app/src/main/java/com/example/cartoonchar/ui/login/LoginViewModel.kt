package com.example.cartoonchar.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartoonchar.network.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
):ViewModel() {

    val snackbar = MutableLiveData<String>()

    val loginStatus = MutableLiveData<Boolean>()

    fun login(username: String, password: String) = viewModelScope.launch {
        try {
            val token = repository.login(username, password)
            loginStatus.postValue(true)
            snackbar.postValue("Login success")
        } catch (ex: Exception) {
            loginStatus.postValue(false)
            snackbar.postValue(ex.message)
        }
    }
}