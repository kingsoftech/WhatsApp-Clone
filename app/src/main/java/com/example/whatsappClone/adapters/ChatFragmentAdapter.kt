package com.example.whatsappClone.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappClone.ChatDetailActivity
import com.example.whatsappClone.R
import com.example.whatsappClone.databinding.ChatItemLayoutBinding
import com.example.whatsappClone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragmentAdapter(private val fragment: Fragment, private val lists:ArrayList<User>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ChatItemLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = lists[position]
        if(holder is ViewHolder){
            Glide.with(fragment)
                .load(model.profileImage)
                .centerCrop()
                    .placeholder(R.drawable.avatar3)
                .into(holder.ivImage)



            holder.tvUserName.text = model.userName
            FirebaseDatabase.getInstance().reference.child("chats")
                .child(FirebaseAuth.getInstance().uid + model.userId)
                .orderByChild("timeStamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.hasChildren()){
                            for(dataSnapShot in snapshot.children){
                                holder.tvLastMessage.text = dataSnapShot.child("message").value.toString()
                            }
                        }
                        else{
                            holder.tvLastMessage.text ="no message to show"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
                )
            holder.tvLastMessage.text = model.lastMessage
            holder.itemView.setOnClickListener {
                val intent = Intent(fragment.context,ChatDetailActivity::class.java)
                intent.putExtra("userId", model.userId)
                intent.putExtra("profilePic", model.profileImage)
                intent.putExtra("userName", model.userName)

                fragment.context?.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }
class ViewHolder(view: ChatItemLayoutBinding):RecyclerView.ViewHolder(view.root){
    val ivImage = view.ivUserProfileImage
    val tvUserName = view.userName
    val tvLastMessage = view.lastMessage
}
}