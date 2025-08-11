package com.example.n8nmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.n8nmobile.databinding.FragmentArtifactsBinding
import java.io.File

class ArtifactsFragment : Fragment() {
    private var _binding: FragmentArtifactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtifactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = ArtifactsListAdapter(loadArtifactsDir())
    }

    private fun loadArtifactsDir(): File {
        val dir = File(requireContext().filesDir, "artifacts")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
