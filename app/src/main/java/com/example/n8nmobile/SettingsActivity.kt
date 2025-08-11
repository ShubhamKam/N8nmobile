package com.example.n8nmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.n8nmobile.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = Preferences(this)
        binding.n8nUrlInput.setText(prefs.getN8nUrl())
        binding.openaiKeyInput.setText(prefs.openAiApiKey)
        binding.openaiBaseUrlInput.setText(prefs.getOpenAiBaseUrl())
        binding.anthropicKeyInput.setText(prefs.anthropicApiKey)
        binding.geminiKeyInput.setText(prefs.geminiApiKey)

        binding.saveButton.setOnClickListener {
            prefs.setN8nUrl(binding.n8nUrlInput.text?.toString().orEmpty())
            prefs.openAiApiKey = binding.openaiKeyInput.text?.toString()
            prefs.setOpenAiBaseUrl(binding.openaiBaseUrlInput.text?.toString().orEmpty())
            prefs.anthropicApiKey = binding.anthropicKeyInput.text?.toString()
            prefs.geminiApiKey = binding.geminiKeyInput.text?.toString()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        fun intent(context: android.content.Context) = android.content.Intent(context, SettingsActivity::class.java)
    }
}
