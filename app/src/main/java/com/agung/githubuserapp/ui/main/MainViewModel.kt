package com.agung.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agung.githubuserapp.api.ApiConfig
import com.agung.githubuserapp.model.User
import com.agung.githubuserapp.model.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import androidx.lifecycle.asLiveData


class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser;

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun setSearchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false;
                if (response.isSuccessful) {
                    _listUser.postValue(response.body()?.items)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}