package com.example.n8nmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("n8n_mobile_prefs", Context.MODE_PRIVATE)

    fun getN8nUrl(): String = prefs.getString("n8n_url", "http://127.0.0.1:5678/") ?: "http://127.0.0.1:5678/"
    fun setN8nUrl(value: String) { prefs.edit().putString("n8n_url", value).apply() }

    var openAiApiKey: String?
        get() = prefs.getString("openai_api_key", null)
        set(value) { prefs.edit().putString("openai_api_key", value).apply() }

    fun getOpenAiBaseUrl(): String = prefs.getString("openai_base_url", "https://api.openai.com") ?: "https://api.openai.com"
    fun setOpenAiBaseUrl(value: String) { prefs.edit().putString("openai_base_url", value).apply() }

    var anthropicApiKey: String?
        get() = prefs.getString("anthropic_api_key", null)
        set(value) { prefs.edit().putString("anthropic_api_key", value).apply() }

    var geminiApiKey: String?
        get() = prefs.getString("gemini_api_key", null)
        set(value) { prefs.edit().putString("gemini_api_key", value).apply() }

    companion object {
        fun intent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}
