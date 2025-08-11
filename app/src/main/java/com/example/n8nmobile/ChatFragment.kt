package com.example.n8nmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.n8nmobile.databinding.FragmentChatBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatService: ChatService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        chatService = ChatService(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendButton.setOnClickListener {
            val provider = binding.providerSpinner.selectedItem.toString()
            val message = binding.messageInput.text?.toString().orEmpty()
            if (message.isBlank()) return@setOnClickListener
            binding.chatLog.append("\nYou: $message\n")
            binding.messageInput.setText("")
            lifecycleScope.launch {
                try {
                    val reply = chatService.sendMessage(provider, message)
                    withContext(Dispatchers.Main) {
                        binding.chatLog.append("Assistant: ${'$'}reply\n")
                    }
                } catch (t: Throwable) {
                    withContext(Dispatchers.Main) {
                        binding.chatLog.append("Error: ${'$'}{t.message}\n")
                    }
                }
            }
        }
        binding.triggerButton.setOnClickListener {
            val url = binding.n8nPublicUrlInput.text?.toString().orEmpty()
            lifecycleScope.launch {
                val ok = chatService.triggerN8nPublicUrl(url)
                binding.chatLog.append(if (ok) "Triggered: ${'$'}url\n" else "Failed: ${'$'}url\n")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
