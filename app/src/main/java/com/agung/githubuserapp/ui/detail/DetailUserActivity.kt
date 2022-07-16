package com.agung.githubuserapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.agung.githubuserapp.R
import com.agung.githubuserapp.databinding.ActivityDetailUserBinding
import com.agung.githubuserapp.ui.main.SectionsPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        setActionBarTitle(getString(R.string.detail_user))

        val username = intent.getStringExtra(EXTRA_USERNAME) as String
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL) as String

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        detailUserViewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        detailUserViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        detailUserViewModel.setUserDetail(username)
        detailUserViewModel.user.observe(this, {
            if (it != null) {
                with(binding) {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollower.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvRepository.text = it.publicRepos.toString()
                    tvCompany.text = it.company ?: "-"
                    tvLocation.text = it.location ?: "-"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .into(imgAvatar)
                }
            }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailUserViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleButton.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleButton.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                detailUserViewModel.addToFavorite(username, id, avatarUrl)
                showToast("$username ditambahkan ke daftar favorite!")
            } else {
                detailUserViewModel.removeFromFavorite(id)
                showToast("$username dihapus dari daftar favorite!")
            }
            binding.toggleButton.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}