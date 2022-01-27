package com.example.whatsappClone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsappClone.Firebase.RealTimeDatabase
import com.example.whatsappClone.adapters.ChatAdapter
import com.example.whatsappClone.databinding.ActivityChatDetailBinding
import com.example.whatsappClone.model.MessageModel
import com.example.whatsappClone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var chatDetailActivityBinding: ActivityChatDetailBinding? = null
    private lateinit var userName:String
    private lateinit var profilePic:String
    private lateinit var senderId:String
    private lateinit var receiverId:String
    private lateinit var senderRoom :String
    private lateinit var receiverRoom :String
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        chatDetailActivityBinding = ActivityChatDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(chatDetailActivityBinding!!.root)
        supportActionBar!!.hide()
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        if(intent.hasExtra("userName") or intent.hasExtra("profilePic") or intent.hasExtra("userId")){
            userName = intent.getStringExtra("userName")!!
            profilePic = intent.getStringExtra("profilePic")!!
            receiverId = intent.getStringExtra("userId")!!
        }
        val lists:ArrayList<MessageModel> = ArrayList()
        senderId = mAuth.currentUser!!.uid

        val chatAdapter =  ChatAdapter(this, lists, receiverId)
        chatDetailActivityBinding!!.rvChat.layoutManager = LinearLayoutManager(this)
        chatDetailActivityBinding!!.rvChat.adapter = chatAdapter
        senderRoom = senderId +receiverId
        receiverRoom = receiverId +senderId

        database.reference.child("chats").child(senderRoom)

            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                    for(data in snapshot.children){
                        val messageModel:MessageModel = data.getValue<MessageModel>()!!
                        messageModel.messageId = data!!.key.toString()
                        lists.add(messageModel)
                    }
                        chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        setToolBarUi()
        chatDetailActivityBinding!!.ivBackArrow.setOnClickListener(this)
        chatDetailActivityBinding!!.ivSend.setOnClickListener(this)


    }
   private fun setToolBarUi(){

        Glide.with(this)
                .load(profilePic)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.avatar3)
                .into(chatDetailActivityBinding!!.ivUserProfileImage)
        chatDetailActivityBinding!!.tvUserName.text =userName

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back_arrow->{
                onBackPressed()
                finish()
            }
            R.id.iv_send->
            {
                val message = chatDetailActivityBinding!!.etEnterMessage.text
                val messageModel = MessageModel(uid = senderId, message = message.toString())
                messageModel.timeStamp = Date().time
                message.clear()
                RealTimeDatabase().userChats(senderRoom,receiverRoom, messageModel)
            }
        }
    }
}