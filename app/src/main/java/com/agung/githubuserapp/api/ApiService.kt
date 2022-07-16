package com.agung.githubuserapp.api

import com.agung.githubuserapp.model.DetailUserResponse
import com.agung.githubuserapp.model.User
import com.agung.githubuserapp.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(@Query("q") query: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<User>>
}