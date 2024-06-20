package com.example.fitmotion.Friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitmotion.R
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody

class ListFriendAdapter(private val friends: List<Friend>) : RecyclerView.Adapter<ListFriendAdapter.ViewHolder>() {

    // ViewHolder untuk menampilkan setiap item dalam RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.tv_user)
        val userImageView: CircleImageView = itemView.findViewById(R.id.iv_user)
        val moreButton: ImageButton = itemView.findViewById(R.id.user_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]

        holder.usernameTextView.text = friend.username

//        // Menampilkan gambar teman menggunakan Glide atau library lainnya
//        // Ganti `friend.imageUrl` dengan properti yang sesuai dari model `Friend`
//        Glide.with(holder.itemView)
//            .load(friend.imageUrl) // Ganti dengan properti yang sesuai dari model Friend
//            .placeholder(R.drawable.ic_account) // Placeholder jika gambar sedang dimuat
//            .error(R.drawable.ic_placeholder) // Gambar yang akan ditampilkan jika ada kesalahan
//            .into(holder.userImageView)
//
//        // Tambahkan onClickListener pada tombol 'more' jika diperlukan
//        holder.moreButton.setOnClickListener {
//            // Implementasi aksi ketika tombol 'more' ditekan
//            // Misalnya, tampilkan menu popup atau detail tambahan
//        }
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}
