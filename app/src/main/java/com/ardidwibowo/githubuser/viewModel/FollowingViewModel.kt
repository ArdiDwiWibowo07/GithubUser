package com.ardidwibowo.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardidwibowo.githubuser.api.ApiConfig
import com.ardidwibowo.githubuser.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setListFollowing(usename: String) {
        val client = ApiConfig.getApiService().getFollowing(usename)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listFollowing.postValue(response.body())
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                t.message?.let { Log.d("failure", it) }
            }
        })
    }

    fun getListFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}