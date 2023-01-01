package com.ardidwibowo.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ardidwibowo.githubuser.api.ApiConfig
import com.ardidwibowo.githubuser.model.User
import com.ardidwibowo.githubuser.model.UserResponse
import com.ardidwibowo.githubuser.local.FavoriteUser
import com.ardidwibowo.githubuser.local.FavoriteUserDao
import com.ardidwibowo.githubuser.local.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (app: Application) : AndroidViewModel(app) {
    val listUsers = MutableLiveData<ArrayList<User>>()

    private var userDao : FavoriteUserDao?
    private var userDb : UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(app)
        userDao = userDb?.FavoriteUserDao()
    }

    fun setSearchUsers(query: String) {
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listUsers.postValue(response.body()?.items)
                    }

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
    fun addToFavorite(username: String, id: Int, avatar_url: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatar_url
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

}