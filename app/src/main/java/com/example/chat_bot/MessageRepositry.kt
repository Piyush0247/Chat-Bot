package com.example.chat_bot

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepositry(private val firebase:FirebaseFirestore) {
    suspend fun sendMessage(roomId:String,message: Message):Result<Unit> = try{
        firebase.collection("rooms").
        document(roomId).collection("message").add(message).await()
        Result.Success(Unit)
    }catch (e:Exception){
        Result.Error(e)
    }
    fun getChatMessage(roomId:String):Flow<List<Message>> = callbackFlow {
        val subscription = firebase.collection("rooms").document(roomId)
            .collection("message").orderBy("timeStamp")
            .addSnapshotListener{querySnapshot ,_->
                querySnapshot?.let {
                    trySend(it.documents.map{doc ->
                        doc.toObject(Message::class.java)!!.copy()
                    }).isSuccess
                }
            }
        awaitClose{subscription.remove()}
    }
}