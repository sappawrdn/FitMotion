package com.example.fitmotion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.Friend.AddFriendActivity
import com.example.fitmotion.Friend.Friend
import com.example.fitmotion.Friend.FriendApiConfig
import com.example.fitmotion.Friend.ListFriendAdapter
import com.example.fitmotion.UserHelper.UserPreference
import com.example.fitmotion.UserHelper.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListFriendAdapter
    private lateinit var friendsList: MutableList<Friend>
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        recyclerView = view.findViewById(R.id.friends_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        friendsList = mutableListOf()
        adapter = ListFriendAdapter(friendsList)
        recyclerView.adapter = adapter

        view.findViewById<ImageButton>(R.id.add_friend_button).setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.notification_button).setOnClickListener {
            val intent = Intent(activity, FriendRequestsActivity::class.java)
            startActivity(intent)
        }

        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(activity, "Search: $query", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        fetchFriends()
    }

    private fun fetchFriends() {
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect { user ->
                val token = user.token

                withContext(Dispatchers.Main) {
                    if (token.isEmpty()) {
                        Toast.makeText(activity, "No auth token found", Toast.LENGTH_SHORT).show()
                        return@withContext
                    }

                    val service = FriendApiConfig.getFriendApiService(token)
                    service.getFriendsList().enqueue(object : Callback<List<Friend>> {
                        override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                            if (response.isSuccessful) {
                                response.body()?.let { friends ->
                                    friendsList.clear()
                                    friendsList.addAll(friends)
                                    adapter.notifyDataSetChanged()
                                    Log.d("FriendsFragment", "Successfully fetched friends list") // Log ketika berhasil mengambil daftar teman
                                }
                            } else {
                                Toast.makeText(activity, "Failed to fetch friends", Toast.LENGTH_SHORT).show()
                                Log.e("FriendsFragment", "Failed to fetch friends: ${response.message()}") // Log error jika gagal mengambil daftar teman
                            }
                        }

                        override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                            Toast.makeText(activity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    companion object {

    }
}
