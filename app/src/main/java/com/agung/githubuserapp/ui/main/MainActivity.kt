package com.agung.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.R
import com.agung.githubuserapp.UserAdapter
import com.agung.githubuserapp.databinding.ActivityMainBinding
import com.agung.githubuserapp.model.User
import com.agung.githubuserapp.ui.detail.DetailUserActivity
import com.agung.githubuserapp.ui.favorite.FavoriteActivity
import com.agung.githubuserapp.ui.setting.SettingActivity
import com.agung.githubuserapp.ui.setting.SettingPreferences
import com.agung.githubuserapp.ui.setting.SettingViewModel
import com.agung.githubuserapp.ui.setting.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showTheme()
        setActionBarTitle(getString(R.string.github_user))
        showLoading(false)
        searchUser()

        adapter = UserAdapter()

        adapter.onItemClickCallback = UserAdapter.OnItemClickCallback { user ->
            val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
            intent.putExtra(DetailUserActivity.EXTRA_ID, user.id)
            intent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, user.avatarUrl)
            startActivity(intent)
        }

        with(binding) {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            edtSearch.setOnKeyListener { _, keycode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.listUser.observe(this, {
            if (it != null) {
                adapter.setListUser(it as ArrayList<User>)
                showLoading(false)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }

            R.id.setting_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun searchUser() {
        with(binding) {
            val query = edtSearch.text.toString()
            if (query.isEmpty()) {
                showLoading(false)
            } else
                mainViewModel.setSearchUser(query)
        }
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

    private fun showTheme() {
        val pref = SettingPreferences.getInstance(datastore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

    }

}

