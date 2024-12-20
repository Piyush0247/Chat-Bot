package com.example.chat_bot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(
    authViewModel:AuthViewModel,
    OnNavigationToSignUp : () -> Unit){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Column (modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = email,
            onValueChange = {email = it},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().
        padding(8.dp),
            textStyle = TextStyle(color = Color.Black)
        )


        OutlinedTextField(value = password,
            onValueChange = {password = it},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().
            padding(8.dp),
            textStyle = TextStyle(color = Color.Black))

        OutlinedTextField(value = firstName,
            onValueChange = {firstName = it},
            label = { Text("FirstName") },
            modifier = Modifier.fillMaxWidth().
            padding(8.dp),
            textStyle = TextStyle(color = Color.Black))

        OutlinedTextField(value = lastName,
            onValueChange = {lastName = it},
            label = { Text("LastName") },
            modifier = Modifier.fillMaxWidth().
            padding(8.dp),
            textStyle = TextStyle(color = Color.Black))

        Button(onClick = {
            authViewModel.signUp(email, password, firstName, lastName)
            email = ""
            password = ""
            firstName = ""
            lastName = ""
        },Modifier.fillMaxWidth().padding(8.dp), shape = RoundedCornerShape(20)) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Already have an account ? LogIn.", modifier = Modifier.clickable {OnNavigationToSignUp()  })
    }

}
