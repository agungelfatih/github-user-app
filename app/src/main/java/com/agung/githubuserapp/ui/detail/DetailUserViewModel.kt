package com.agung.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agung.githubuserapp.api.ApiConfig
import com.agung.githubuserapp.database.FavoriteUser
import com.agung.githubuserapp.database.FavoriteUserDao
import com.agung.githubuserapp.database.UserDatabase
import com.agung.githubuserapp.model.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    private var _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: FavoriteUserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.favoritUserDao()
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                id,
                username,
                avatarUrl
            )

            userDao?.addToFav(user)
        }
    }


    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFav(id)
        }
    }

    companion object {
        val TAG: String = DetailUserViewModel::class.java.simpleName
    }
}