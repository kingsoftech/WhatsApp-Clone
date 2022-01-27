package com.example.whatsappClone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappClone.adapters.ChatAdapter
import com.example.whatsappClone.databinding.ActivityGroupChatBinding
import com.example.whatsappClone.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {
    //initializing binding
    private var activityChatGroupBinding:ActivityGroupChatBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding he activity with the layout
        activityChatGroupBinding=ActivityGroupChatBinding.inflate(layoutInflater)
        //setting the rootView as the content of the main activity
        setContentView(activityChatGroupBinding!!.root)
        //hiding the default actionBar
        supportActionBar!!.hide()
        //setting an onClicked listener to the back arrow image to move back to the main activity when the  back button is pressed
        activityChatGroupBinding!!.ivBackArrow.setOnClickListener {
            //moved back
            onBackPressed()}
        //creating an instance of firebaseDatabase
            val database = FirebaseDatabase.getInstance()
        //creating a list of MessageModel
            val messageModelList:ArrayList<MessageModel> = ArrayList()
        //creating an instance of ChatAdapter
            val chatAdapter  = ChatAdapter(this,messageModelList, null )
        //getting the logged in user ID from firebaseAuth
            val senderId = FirebaseAuth.getInstance().uid!!
        //setting the username text
            activityChatGroupBinding!!.tvUserName.text = "groupChat"
        //setting the adapter of the recyclerView
            activityChatGroupBinding!!.rvChat.adapter = chatAdapter
        // setting the layout manager to the recyclerView to be Linear
            activityChatGroupBinding!!.rvChat.layoutManager = LinearLayoutManager(this)
        //getting data from the database in form of DataSnapShot
            database.reference.child("Group Chat").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //clearing the list of MessageModel
                    messageModelList.clear()
                    //using the for loop to get all data in the snapshot
                    for(dataSnapShot in snapshot.children){
                        //getting the value from the database snapshot and storing as MessageModel
                        val messageModel:MessageModel = dataSnapShot.getValue<MessageModel>()!!
                        //getting the message ID
                        messageModel.messageId = dataSnapShot!!.key.toString()
                        //adding the message to the messageModel List
                        messageModelList.add(messageModel)
                    }
                    //notify the Chat Adapter of dataset Change
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                   // TODO("Not yet implemented")
                }

            })
        //setting an onClicked Listener to send the message to the database and input it into appropriate space
            activityChatGroupBinding!!.ivSend.setOnClickListener {
                //setting the text in the input as message
                val message = activityChatGroupBinding!!.etEnterMessage.text.toString()
                //inputting the sender ID and the message into the model
                val model = MessageModel(uid = senderId, message = message)
                //setting the timeStamp
                model.timeStamp = Date().time
                //clearing the edit text
                activityChatGroupBinding!!.etEnterMessage.text.clear()
                //putting the model into the database
                database.reference
                    .child("Group Chat")
                    .push()
                    .setValue(model)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Message sent", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
