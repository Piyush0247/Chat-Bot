package com.example.chat_bot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val userRepository: UserRepository

    object Injection {
        private val instance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }

        fun instance(): FirebaseFirestore {
            return instance
        }
    }

    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, firstName: String, lastName: String){
        viewModelScope.launch {
            val result = userRepository.signUp(email, firstName, lastName, password)
            _authResult.value = result
        }
    }
    fun login(email:String,password:String){
        viewModelScope.launch {
            _authResult.value = userRepository.logIn(email, password)
        }
    }
}
class RoomViewModel :ViewModel() {
    private val _rooms = MutableLiveData<List<Rooms>>()
    val rooms : LiveData<List<Rooms>> get() = _rooms

    private val roomRepository : RoomRespository = RoomRespository(AuthViewModel.Injection.instance())

    init {
        loadRooms()
    }
    fun createRoom(name:String){
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }
    }
    fun loadRooms(){
        viewModelScope.launch {
            when(val result = roomRepository.getRoom()){
                is Result.Success -> _rooms.value = result.data
                is Result.Error -> {

                }
            }
        }
    }
}

class MessageViewModel:ViewModel(){
    private val _roomName  = MutableLiveData<String>()
    val roomsName: LiveData<String>get() = _roomName
    private val messageRepository:MessageRepositry
    private val userRepository:UserRepository
    init {
        messageRepository = MessageRepositry(AuthViewModel.Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(),AuthViewModel.Injection.instance())
        loadCurrentUser()
    }
    fun fetchRoomName(roomId:String){
        _roomName.value = roomId

    }

    fun setRoomId(roomId:String){
        _roomId.value = roomId
        loadMessage()
    }
    private val _message = MutableLiveData<List<Message>>()
    val message:LiveData<List<Message>>get() = _message
    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<Users>()
    val currentUser : LiveData<Users>get() = _currentUser

    private fun loadCurrentUser(){
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()){
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {

                }
            }
        }
    }
    fun loadMessage(){
        viewModelScope.launch {
            if (_roomId != null){
                messageRepository.getChatMessage(roomId = _roomId.value.toString())
                    .collect{_message.value = it}
            }
        }
    }
    fun sendMessage(text:String){
        if (_currentUser.value != null){
            val message = Message(
                senderId = _currentUser.value!!.email,
                senderFirstName = _currentUser.value!!.firstName,
                text = text
            )
            viewModelScope.launch {
                when(messageRepository.sendMessage(_roomId.value.toString(),message)){
                    is Result.Success -> Unit
                    is Result.Error -> {

                    }
                }
            }
        }
    }
    fun seenMessage(content:String){
        val messages = Message(
            id = UUID.randomUUID().toString(),
            text = content,
            senderId = _currentUser.value?.id?:"",
            receiverId = _currentUser.value.toString(),
            timeStamp = System.currentTimeMillis(),
            status = "sent"
        )
        val updateMessage = _message.value.orEmpty()+messages
        _message.value = updateMessage
        viewModelScope.launch {
            delay(1000)
           updateMessageStatus(messages.id,"delivered")
        }


    }
    private fun updateMessageStatus(messageId: String,status: String){
        _message.value = _message.value?.map {
                msg->
            if (msg.id == messageId) msg.copy(status = status) else msg
        }
    }
}