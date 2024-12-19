package com.example.chat_bot

import android.provider.Settings

data class Message(
    val id : String = "",
    val senderFirstName:String = "",
    val senderId:String = "",
    val text:String = "",
    val timeStamp:Long = System.currentTimeMillis(),
    val isSendByCurrentUser:Boolean = false,
    val status:String = "sent",
    val receiverId: String = "",
)
