package com.hypnex.friengo.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.hypnex.friengo.R
import com.hypnex.friengo.data.models.User

class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private var friends = listOf<User>()

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        private val nameSecondNameTextView: TextView = itemView.findViewById(R.id.nameSecondNameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

        fun bind(user: User) {
            profileImage.load(user.photoUrl) {
                crossfade(true)
                fallback(R.drawable.logo_round)
                placeholder(R.mipmap.ic_launcher)
                transformations(CircleCropTransformation())
            }

            nameSecondNameTextView.text = user.displayName

            emailTextView.text = user.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.friend_item, parent, false)

        return FriendsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friends[position])
    }

    override fun getItemCount() = friends.size

    fun updateFriends(newFriend: List<User>) {
        friends = newFriend

        notifyDataSetChanged()
    }

}