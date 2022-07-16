package com.agung.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.githubuserapp.UserAdapter
import com.agung.githubuserapp.databinding.FragmentFollowBinding

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followingViewModel: FollowingViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.onItemClickCallback = UserAdapter.OnItemClickCallback { user ->
            val intent = Intent(activity, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
            intent.putExtra(DetailUserActivity.EXTRA_ID, user.id)
            intent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, user.avatarUrl)
            startActivity(intent)
        }

        followingViewModel.setListFollowing(userName)
        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followingViewModel.listFollowing.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setListUser(it)
                showLoading(false)
            }
        })

        with(binding) {
            rvFollow.setHasFixedSize(true)
            rvFollow.layoutManager = LinearLayoutManager(activity)
            rvFollow.adapter = adapter
        }

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
}