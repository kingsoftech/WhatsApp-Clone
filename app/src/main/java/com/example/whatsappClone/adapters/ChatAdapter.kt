package com.example.whatsappClone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappClone.databinding.SampleReceiverBinding
import com.example.whatsappClone.databinding.SampleSenderBinding
import com.example.whatsappClone.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(private val  context:Context,private val list:ArrayList<MessageModel>,private val receiverId: String?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val SENDER_VIEW_TYPE = 1
        const val RECEIVER_VIEW_TYPE = 2
    }
    class ReceiverViewHolder(sampleReceiverBinding: SampleReceiverBinding):RecyclerView.ViewHolder(sampleReceiverBinding.root)
    {
        val receiverMessage = sampleReceiverBinding.tvReceiver
        val receiverTimeStamp = sampleReceiverBinding.tvReceiverTime
    }


    class SenderViewHolder(sampleSenderBinding: SampleSenderBinding):RecyclerView.ViewHolder(sampleSenderBinding.root)
    {
        val senderMessage = sampleSenderBinding.tvSender
        val senderTimeStamp = sampleSenderBinding.tvSenderTime
    }

    override fun getItemViewType(position: Int): Int {
        if(this.list[position].uid == FirebaseAuth.getInstance().uid){
            return SENDER_VIEW_TYPE
        }
       else{
           return RECEIVER_VIEW_TYPE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == SENDER_VIEW_TYPE){
            val binding = SampleSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SenderViewHolder(binding)
        }
        else {
            val binding = SampleReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return  ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      val model = this.list[position]


        holder.itemView.setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Delete")
            alertDialog.setMessage("are you sure you want to delete the message")
            alertDialog.setPositiveButton("yes") { interfaceDialog, _ ->
            val database = FirebaseDatabase.getInstance()
                val senderRoom = FirebaseAuth.getInstance().uid +receiverId
                database.reference.child("chats")
                    .child(senderRoom)
                    .child(model.messageId)
                    .setValue(null)
                interfaceDialog.dismiss()
            }
            alertDialog.setNegativeButton("NO") {
                interfaceDialog,_->
                interfaceDialog.dismiss()
            }
            alertDialog.show()
            return@setOnLongClickListener false
        }
        if(holder is SenderViewHolder)
        {
            holder.senderMessage.text = model.message
            holder.senderTimeStamp.text = timeStampToDate(model.timeStamp)
        }
       else if(holder is ReceiverViewHolder){
           holder.receiverMessage.text = model.message
           holder.receiverTimeStamp.text = timeStampToDate(model.timeStamp)
       }
    }

    override fun getItemCount(): Int {
      return  this.list.size
    }
//help from stack overflow
    private fun epochToIso8601(time: Long): String {
        val format = "dd MMM yyyy HH:mm:ss" // you can add the format you need
        val sdf = SimpleDateFormat(format, Locale.getDefault()) // default local
        sdf.timeZone = TimeZone.getDefault() // set anytime zone you need
        return sdf.format(Date(time * 1000L))
    }

    private fun timeStampToDate(time: Long): String {
        val date = Date(time)
        val format = "h:mm a"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }
}