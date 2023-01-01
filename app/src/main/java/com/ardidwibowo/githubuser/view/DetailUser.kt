package com.ardidwibowo.githubuser.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.ardidwibowo.githubuser.R
import com.ardidwibowo.githubuser.viewModel.DetailUserViewModel
import com.ardidwibowo.githubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }


    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailUserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)


        if (username != null) {
            detailViewModel.setUserDetail(username)

            detailViewModel.getUserDetail().observe(this) {
                if (it != null) {
                    binding.apply {
                        tvName.text = it.name
                        tvUsername.text = it.login
                        tvFollowers.text =
                            StringBuilder(it.followers.toString()).append(" Followers")
                        tvFollowing.text =
                            StringBuilder(it.following.toString()).append(" Following")
                        Glide.with(this@DetailUser)
                            .load(it.avatar_url)
                            .centerCrop()
                            .into(profileImage)
                    }
                }
            }


        }
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_share -> {
                val username = intent.getStringExtra(EXTRA_USERNAME)
                val bundle = Bundle()
                bundle.putString(EXTRA_USERNAME, username)
                val githubAccount = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/$username"))
                startActivity(githubAccount)
            }
        }
    }
}