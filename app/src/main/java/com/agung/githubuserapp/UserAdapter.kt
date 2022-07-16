package com.agung.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agung.githubuserapp.databinding.ItemUserLayoutBinding
import com.agung.githubuserapp.model.User
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var listUser = arrayListOf<User>()
    internal lateinit var onItemClickCallback: OnItemClickCallback

    fun setListUser(users: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val binding = ItemUserLayoutBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.tvUsername.text = user.login
        Glide.with(holder.itemView)
            .load(user.avatarUrl)
            .centerCrop()
            .into(holder.binding.imgAvatar)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    override fun getItemCount() = listUser.size
}