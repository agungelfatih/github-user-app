package com.agung.githubuserapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.R
import com.agung.githubuserapp.UserAdapter
import com.agung.githubuserapp.database.FavoriteUser
import com.agung.githubuserapp.databinding.ActivityFavoriteBinding
import com.agung.githubuserapp.model.User
import com.agung.githubuserapp.ui.detail.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBarTitle(getString(R.string.favorite_user))

        favoriteUserViewModel = ViewModelProvider(this).get(FavoriteUserViewModel::class.java)

        adapter = UserAdapter()
        adapter.onItemClickCallback = UserAdapter.OnItemClickCallback { user ->
            val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
            intent.putExtra(DetailUserActivity.EXTRA_ID, user.id)
            intent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, user.avatarUrl)
            startActivity(intent)
        }

        with(binding) {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavoriteUser.adapter = adapter
        }

        favoriteUserViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        favoriteUserViewModel.getFavUser()?.observe(this, {
            if (it != null) {
                adapter.setListUser(mapList(it))
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUser = ArrayList<User>()

        for (user in users) {
            val userMapped = User(
                id = user.id,
                login = user.login,
                avatarUrl = user.avatarUrl
            )
            listUser.add(userMapped)
        }

        return listUser
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

}