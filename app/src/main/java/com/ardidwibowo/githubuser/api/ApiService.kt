package com.ardidwibowo.githubuser.api

import com.ardidwibowo.githubuser.model.DetailUserResponse
import com.ardidwibowo.githubuser.model.User
import com.ardidwibowo.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query(value = "q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}