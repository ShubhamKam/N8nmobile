package com.example.n8nmobile

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class ChatService(private val context: Context) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun sendMessage(provider: String, message: String): String = withContext(Dispatchers.IO) {
        val prefs = Preferences(context)
        return@withContext when (provider.lowercase()) {
            "openai", "grok" -> callOpenAICompat(prefs.openAiApiKey, prefs.getOpenAiBaseUrl(), message)
            "claude" -> callClaude(prefs.anthropicApiKey, message)
            "gemini" -> callGemini(prefs.geminiApiKey, message)
            else -> "Unknown provider: $provider\n"
        }
    }

    private fun callOpenAICompat(apiKey: String?, baseUrl: String, message: String): String {
        if (apiKey.isNullOrBlank()) return "OpenAI-compatible API key missing. Configure in Settings.\n"
        val url = "$baseUrl/v1/chat/completions"
        val body = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", listOf(
                JSONObject().put("role", "user").put("content", message)
            ))
            put("temperature", 0.7)
        }.toString()
        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return "HTTP ${resp.code}: ${resp.body?.string()}\n"
            val json = JSONObject(resp.body?.string().orEmpty())
            val choices = json.optJSONArray("choices")
            val content = choices?.optJSONObject(0)?.optJSONObject("message")?.optString("content")
            return content ?: json.toString(2)
        }
    }

    private fun callClaude(apiKey: String?, message: String): String {
        if (apiKey.isNullOrBlank()) return "Anthropic API key missing. Configure in Settings.\n"
        val url = "https://api.anthropic.com/v1/messages"
        val body = JSONObject().apply {
            put("model", "claude-3-5-sonnet-20240620")
            put("max_tokens", 512)
            put("system", "You are a helpful assistant.")
            val contentBlocks = JSONArray().apply {
                put(JSONObject().put("type", "text").put("text", message))
            }
            val userMessage = JSONObject().apply {
                put("role", "user")
                put("content", contentBlocks)
            }
            val messages = JSONArray().apply { put(userMessage) }
            put("messages", messages)
        }.toString()
        val req = Request.Builder()
            .url(url)
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return "HTTP ${resp.code}: ${resp.body?.string()}\n"
            val json = JSONObject(resp.body?.string().orEmpty())
            val arr = json.optJSONArray("content")
            val first = arr?.optJSONObject(0)?.optString("text")
            return first ?: json.toString(2)
        }
    }

    private fun callGemini(apiKey: String?, message: String): String {
        if (apiKey.isNullOrBlank()) return "Gemini API key missing. Configure in Settings.\n"
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=$apiKey"
        val body = JSONObject().apply {
            put("contents", listOf(
                JSONObject().put("parts", listOf(
                    JSONObject().put("text", message)
                ))
            ))
        }.toString()
        val req = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return "HTTP ${resp.code}: ${resp.body?.string()}\n"
            val json = JSONObject(resp.body?.string().orEmpty())
            val candidates = json.optJSONArray("candidates")
            val content = candidates?.optJSONObject(0)?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")
            return text ?: json.toString(2)
        }
    }

    suspend fun triggerN8nPublicUrl(url: String): Boolean = withContext(Dispatchers.IO) {
        if (url.isBlank()) return@withContext false
        val req = Request.Builder().url(url).get().build()
        client.newCall(req).execute().use { resp ->
            return@withContext resp.isSuccessful
        }
    }
}
