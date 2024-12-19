package com.example.chat_bot

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel,navController : NavHostController){
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route){
        composable(Screen.SignUpScreen.route){
            SignUpScreen(
                authViewModel = authViewModel,OnNavigationToSignUp = {
                navController.navigate(Screen.LoginScreen.route)
            })
        }
        composable(Screen.LoginScreen.route){
            LogInScreen(authViewModel,onNavigationLogIn = {
                navController.navigate(Screen.SignUpScreen.route)
            }, onSignInSuccess = {
                navController.navigate(Screen.ChatRoomScreen.route)
            })
        }
        composable(Screen.ChatRoomScreen.route){
            ChatRoomScreen{
                navController.navigate("${Screen.ChatScreen.route}/${it.id}/${it.name}")
            }
        }
        composable("${Screen.ChatScreen.route}/{roomId}/{roomName}"){
            val roomId:String = it.arguments?.getString("roomId")?:""
            val roomName:String = it.arguments?.getString("roomName")?:""
            ChatScreen(roomId = roomId, navController = navController, roomName = roomName)
        }

    }
}