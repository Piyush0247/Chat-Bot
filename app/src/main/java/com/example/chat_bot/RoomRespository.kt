package com.example.chat_bot

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RoomRespository(private val fireStore:FirebaseFirestore) {
    suspend fun createRoom(name:String):Result<Unit> = try
        {
        val  room = Rooms(name = name)
        fireStore.collection("rooms").add(room).await()
        Result.Success(Unit)
    }catch (e:Exception){
        Result.Error(e)
    }

    suspend fun getRoom():Result<List<Rooms>> = try {
        val querySnapshot = fireStore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Rooms::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    }catch (e:Exception){
        Result.Error(e)
    }
}