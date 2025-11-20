package com.saim.curify.chat

import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val service: ChatService
) {
    suspend fun complete(prompt: String): String {
        return try {
            val resp = service.complete(ChatRequest(messages = listOf(ChatMessage(role = "user", content = prompt))))
            resp.choices.firstOrNull()?.message?.content?.trim().orEmpty().ifEmpty { "I couldn't find an answer to that." }
        } catch (t: Throwable) {
            val message = t.message ?: t.toString()
            "Error: ${message}"
        }
    }
}

