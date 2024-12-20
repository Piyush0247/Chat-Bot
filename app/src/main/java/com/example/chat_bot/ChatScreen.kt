@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chat_bot

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ChatScreen(roomId: String,
               roomName:String,
               messageViewModel: MessageViewModel = viewModel(),
               navController: NavController) {
    val message by messageViewModel.message.observeAsState(emptyList())
    LaunchedEffect(roomId) {
        messageViewModel.setRoomId(roomId) // Set the room ID only once
        messageViewModel.fetchRoomName(roomName) // Fetch the room name only once
    }
   var changeBackgroundIndex by remember { mutableIntStateOf(0) }
    val background = listOf(R.drawable.image1
        ,R.drawable.image2
        ,R.drawable.image3
        ,R.drawable.image4
        ,R.drawable.image5
        ,R.drawable.image6)
    var dropdownExpand by remember { mutableStateOf(false) }
    val text = remember { mutableStateOf("") }
    Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(roomName) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack() // Go back to the previous screen
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                           dropdownExpand = true
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Menu"
                            )

                        }
                        DropdownMenu(expanded = dropdownExpand,
                            onDismissRequest = {dropdownExpand = false}) {
                            background.forEachIndexed { index, backgrounds ->
                                DropdownMenuItem(
                                    text = {
                                        Text("change background${index+1}")
                                    },
                                    onClick = {
                                        changeBackgroundIndex = index
                                        dropdownExpand = false
                                    },
                                    leadingIcon = {
                                        Image(
                                            painter = painterResource(id = backgrounds),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                )
                            }
                        }
                    }
                )
            }
            ){innerpadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(innerpadding) // Fill the entire screen
        ) {
            // Static Background Image (Does not scroll)
            Crossfade(targetState = background[changeBackgroundIndex], label = "") { backgrounds->
                Image(
                    painter = painterResource(id = backgrounds), // Replace with your image
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize() // Ensures it takes up the full screen
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(1.dp)) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(message) { messages ->
                        ChatMessageItem(
                            message = messages.copy(
                                isSendByCurrentUser = messages.senderId == messageViewModel.currentUser.value?.email
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        label = { Text("Enter Message") },
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp, color = Color.Black ),
                        modifier = Modifier.weight(1f).padding(8.dp),
                    )
                    IconButton(onClick = {
                        if (text.value.isNotEmpty()) {
                            messageViewModel.sendMessage(text.value.trim())
                            text.value = ""
                        }
                        messageViewModel.loadMessage()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null
                            , modifier = Modifier.size(48.dp).clip(CircleShape).
                            background(Color.Black).padding(8.dp), tint = Color.White)
                    }
                }
            }
        }
    }

}