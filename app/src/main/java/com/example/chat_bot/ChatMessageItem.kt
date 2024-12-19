package com.example.chat_bot

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
private fun formatTimeStamp(timeStamp :Long):String{
    val  messageDateTime= LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when{
        isSameDay(messageDateTime,now) -> "Today${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1),now) -> "yesterday${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }
}
@SuppressLint("NewApi")
private fun isSameDay(dateTime1:LocalDateTime, dateTime2:LocalDateTime):Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}
@SuppressLint("NewApi")
private fun formatTime(dateTime:LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}
@SuppressLint("NewApi")
private fun formatDate(dateTime:LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("MMM d,yyyy")
    return formatter.format(dateTime)
}

@Composable
fun ChatMessageItem(message:Message){
    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = if (message.isSendByCurrentUser)Alignment.End else Alignment.Start) {
        Box(modifier = Modifier.background(if (message.isSendByCurrentUser)
            colorResource(id = R.color.purple_200)else colorResource(id = R.color.light_green),
            shape = RoundedCornerShape(8.dp)).padding(8.dp)){
            Text(message.text,
                color = Color.White,
                style = TextStyle(fontSize = 16.sp))
        }
        Spacer(Modifier.height(4.dp))
        Text(text = message.senderFirstName,
            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
        )
        Text(
            text = formatTimeStamp(message.timeStamp),
            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
        )
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End) {
            when(message.status){
                "sent" -> {
                    Text("Sent",
                        style = TextStyle(fontSize = 12.sp,
                            color = Color.DarkGray))
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Gray)
                }

                "delivered" -> {
                    Text("Delivered",
                        style = TextStyle(fontSize = 12.sp,
                            color = Color.Green)
                    )
                    Icon(painter = painterResource(R.drawable.done_icon), contentDescription = null, tint = Color.DarkGray)
                }
                "seen" -> {
                    Text("Seen",
                        style = TextStyle(fontSize = 12.sp,
                            color = Color.Blue)
                    )
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Blue)
                }
            }
        }
    }
}