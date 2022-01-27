package com.example.whatsappClone.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    var profileImage:String = "",
    var userName:String = "",
    val email: String = "",
    val password:String = "",
    var userId:String = "",
    var lastMessage:String = "",
    var status:String = ""
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)= with(parcel) {
        parcel.writeString(profileImage)
        parcel.writeString(userName)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(userId)
        parcel.writeString(lastMessage)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
