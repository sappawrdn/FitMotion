package com.example.fitmotion.Friend

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmotion.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<UserResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        // Initialize RecyclerView and Adapter
        val rvAddFriend: RecyclerView = findViewById(R.id.rv_add_friend)
        userAdapter = UserAdapter(userList, this) { user -> addFriend(user) }
        rvAddFriend.layoutManager = LinearLayoutManager(this)
        rvAddFriend.adapter = userAdapter

        // Initialize SearchView
        val searchLayout = findViewById<LinearLayout>(R.id.search_add_friends)
        val searchView = searchLayout.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Optionally handle text changes as needed
                return false
            }
        })
    }

//    private fun searchUser(username: String) {
//        val userService = FriendApiConfig.getFriendApiService()
//        val call = userService.searchUsers(username)
//        call.enqueue(object : Callback<List<UserResponse>> {
//            override fun onResponse(
//                call: Call<List<UserResponse>>,
//                response: Response<List<UserResponse>>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        userList.clear()
//                        userList.addAll(it)
//                        userAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    Toast.makeText(
//                        this@AddFriendActivity,
//                        "Failed to retrieve users: ${response.message()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }

//            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
//                Toast.makeText(
//                    this@AddFriendActivity,
//                    "Error: ${t.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//    }

    private fun addFriend(user: UserResponse) {
        // Implement your logic for adding a friend here, for example:
        // This could involve sending a friend request to the user.
        Toast.makeText(this, "Friend request sent to ${user.username}", Toast.LENGTH_SHORT).show()
    }
}
