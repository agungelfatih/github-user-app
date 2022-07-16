package com.agung.githubuserapp.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("items")
    val items: List<User>
)

data class User(

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String
)
