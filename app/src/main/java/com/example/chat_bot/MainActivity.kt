package com.example.chat_bot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.chat_bot.ui.theme.ChatBotTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val token = task.result
                    Log.d("FCM token",token)
                }
            }
            val  navController = rememberNavController()
            val authViewModel:AuthViewModel = viewModel()
            ChatBotTheme {

                   NavigationGraph(authViewModel,navController)



            }
        }
    }
}
