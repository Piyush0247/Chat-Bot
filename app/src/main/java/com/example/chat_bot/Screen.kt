package com.example.chat_bot

sealed class Screen(val route : String){
    object LoginScreen:Screen("login_screen")
    object SignUpScreen:Screen("signup_screen")
    object ChatRoomScreen:Screen("chatroom_screen")
    object ChatScreen:Screen("chat_screen")
}