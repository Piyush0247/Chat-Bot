package com.example.chat_bot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    roomViewModel: RoomViewModel = viewModel(),
    onJoinClicked: (Rooms) -> Unit
) {
    val room by roomViewModel.rooms.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chat Room", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))

        LazyColumn {
            items(room) { rooms ->
                RoomItem(rooms) { onJoinClicked(it) }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            shape = RoundedCornerShape(20)
        ) {
            Text("Create Room")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create a new room") },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        placeholder = { Text("Enter room name") }
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            if (name.isNotBlank()) {
                                roomViewModel.createRoom(name)
                                showDialog = false
                                name = "" // Clear the input field
                            }
                        }) {
                            Text("Add")
                        }
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RoomItem(room: Rooms, onJoinClicked: (Rooms) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = room.name, fontSize = 16.sp, fontWeight = FontWeight.Normal)
        OutlinedButton(onClick = { onJoinClicked(room) }) {
            Text("Join")
        }
    }
}
