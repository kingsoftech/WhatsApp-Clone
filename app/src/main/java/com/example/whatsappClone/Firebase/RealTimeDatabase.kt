package com.example.whatsappClone.Firebase

import com.example.whatsappClone.adapters.ChatFragmentAdapter
import com.example.whatsappClone.model.MessageModel
import com.example.whatsappClone.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RealTimeDatabase {
    private val database = FirebaseDatabase.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    fun registerUser(user:User){
        database.reference.
        child("Users").
        child(user.userId).
            setValue(user)

    }
    fun  loadUsers(lists:ArrayList<User>, chatFragmentAdapter: ChatFragmentAdapter):ArrayList<User>{
        database.reference.child("Users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                for(dataSnapShot in snapshot.children){
                    val user: User = dataSnapShot.getValue<User>()!!
                    if(user.userId != mAuth.uid)
                    {
                    lists.add(user)
                    }
                    user.userId = dataSnapShot.key.toString()
                }
                chatFragmentAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return lists
    }

    fun userChats(senderRoom:String,receiverRoom:String, model: MessageModel){
        database.reference.child("chats")
                .child(receiverRoom)
                .push()
                .setValue(model)
                .addOnSuccessListener {
                    database.reference.child("chats")
                            .child(senderRoom)
                            .push()
                            .setValue(model).addOnSuccessListener {

                            }
                }

    }
}