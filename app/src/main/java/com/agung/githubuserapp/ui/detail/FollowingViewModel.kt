package com.agung.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agung.githubuserapp.api.ApiConfig
import com.agung.githubuserapp.model.User
import retrofit2.Call
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private var _listFollowing = MutableLiveData<ArrayList<User>>()
    val listFollowing: LiveData<ArrayList<User>> = _listFollowing

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : retrofit2.Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                } else
                    Log.d(TAG, "onFailure: ${response.message()}")
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })

    }

    companion object {
        val TAG: String = FollowingViewModel::class.java.simpleName
    }
}