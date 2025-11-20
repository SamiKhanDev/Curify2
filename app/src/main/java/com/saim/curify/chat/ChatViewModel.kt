package com.saim.curify.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessageUi(val id: String, val role: String, val text: String)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessageUi>>(emptyList())
    val messages: StateFlow<List<ChatMessageUi>> = _messages.asStateFlow()
    val isSending = MutableStateFlow(false)

    fun send(prompt: String) {
        isSending.value = true
        val current = _messages.value.toMutableList()
        val userMsg = ChatMessageUi(id = System.currentTimeMillis().toString(), role = "User", text = prompt)
        current.add(userMsg)
        _messages.value = current
        viewModelScope.launch {
            val reply = repo.complete(prompt)
            val aiMsg = ChatMessageUi(id = System.currentTimeMillis().toString(), role = "Dr.Saim", text = reply)
            _messages.value = _messages.value + aiMsg
            isSending.value = false
        }
    }
}

