package com.agung.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agung.githubuserapp.database.FavoriteUser
import com.agung.githubuserapp.database.FavoriteUserDao
import com.agung.githubuserapp.database.UserDatabase

class FavoriteUserViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDB: UserDatabase?

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.favoritUserDao()
    }

    fun getFavUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavUser()
    }


}