package com.example.n8nmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.n8nmobile.databinding.FragmentN8nBinding

class N8nWebFragment : Fragment() {
    private var _binding: FragmentN8nBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentN8nBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding.webview.setOnTouchListener { v, event ->
                // Improve gesture handling for pinch-zoom and panning
                v.parent?.requestDisallowInterceptTouchEvent(true)
                false
            }
            binding.webview.settings.apply {
                builtInZoomControls = true
                displayZoomControls = false
                useWideViewPort = true
                loadWithOverviewMode = true
            }
            (activity as? MainActivity)?.setupWebView(binding.webview, binding.progress)
        } catch (t: Throwable) {
        }
    }

    fun getWebView(): WebView? = _binding?.webview

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
