package com.ardidwibowo.githubuser.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardidwibowo.githubuser.model.User
import com.ardidwibowo.githubuser.viewModel.MainViewModel
import com.ardidwibowo.githubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersMainAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<UsersMainAdapter.UserViewHolder>() {
    private val list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            var isChecked = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(user.id)
                withContext(Dispatchers.Main){
                    if(count != null){
                        if(count > 0){
                            binding.toggleFav.isChecked = true
                            isChecked = true
                        }else{
                            binding.toggleFav.isChecked = false
                            isChecked = false
                        }
                    }
                }
            }

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.toggleFav.setOnClickListener {
                isChecked = !isChecked
                if(isChecked){
                    viewModel.addToFavorite(user.login, user.id, user.avatar_url )
                }else{
                    viewModel.removeFromFavorite(user.id)
                }
                binding.toggleFav.isChecked = isChecked
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgItemPhoto)
                tvItemName.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}