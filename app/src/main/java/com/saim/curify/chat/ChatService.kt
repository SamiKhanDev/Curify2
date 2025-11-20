package com.saim.curify.chat

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ChatMessage(val role: String, val content: String)
data class ChatRequest(val model: String = "gpt-4o-mini", val messages: List<ChatMessage>)
data class ChatChoice(val index: Int, val message: ChatMessage)
data class ChatResponse(val choices: List<ChatChoice>)

interface ChatService {
    @Headers("Content-Type: application/json")
    @POST("/v1/chat/completions")
    suspend fun complete(@Body req: ChatRequest): ChatResponse
}

