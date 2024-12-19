package com.example.chat_bot

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val auth: FirebaseAuth,
    private var fireStore:FirebaseFirestore) {

    suspend fun signUp(
        email: String,
        firstName: String,
        lastName: String,
        password: String
    ):Result<Boolean>  =
         try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = Users(firstName, lastName, email)
            saveUserToFirebase(user = user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    private suspend fun saveUserToFirebase(user:Users){
        fireStore.collection("users").document(user.email).set(user).await()
    }
    suspend fun logIn(email:String,password:String):Result<Boolean> = try {
        auth.signInWithEmailAndPassword(email,password).await()
        Result.Success(true)
    }catch (e:Exception) {
    Result.Error(e)
    }

    suspend fun getCurrentUser(): Result<Users> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = fireStore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(Users::class.java)
            if (user != null) {
                Log.d("user2","$uid")
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }


}