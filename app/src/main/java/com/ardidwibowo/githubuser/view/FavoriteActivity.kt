package com.ardidwibowo.githubuser.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardidwibowo.githubuser.model.User
import com.ardidwibowo.githubuser.viewModel.FavoriteViewModel
import com.ardidwibowo.githubuser.databinding.ActivityFavoriteBinding
import com.ardidwibowo.githubuser.local.FavoriteUser

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserFavoritAdapter
    private lateinit var viewModel: FavoriteViewModel

    private val mainViewModel by viewModels<FavoriteViewModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserFavoritAdapter(mainViewModel)
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        adapter.setOnItemClickCallback(object : UserFavoritAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@FavoriteActivity, DetailUser::class.java)
                moveIntent.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                startActivity(moveIntent)
                Toast.makeText(this@FavoriteActivity, "Hay", Toast.LENGTH_SHORT).show()
            }
        })

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        }
    }

    private fun mapList(users:List<FavoriteUser>): ArrayList<User>{
        val listUsers = ArrayList<User>()
        for (user in users){
            val userMapped = User(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}