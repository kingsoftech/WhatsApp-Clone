package com.example.whatsappClone.model

import android.os.Parcel
import android.os.Parcelable

data class MessageModel(
    var message:String= "",
    val  uid:String = "",
    var messageId:String = "",
    var timeStamp: Long = 0L
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(uid)
        parcel.writeString(messageId)
        parcel.writeLong(timeStamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageModel> {
        override fun createFromParcel(parcel: Parcel): MessageModel {
            return MessageModel(parcel)
        }

        override fun newArray(size: Int): Array<MessageModel?> {
            return arrayOfNulls(size)
        }
    }
}
