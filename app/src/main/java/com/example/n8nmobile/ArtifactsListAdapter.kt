package com.example.n8nmobile

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ArtifactsListAdapter(private val dir: File) : RecyclerView.Adapter<ArtifactsListAdapter.Holder>() {

    private val files: MutableList<File> = dir.listFiles()?.sortedByDescending { it.lastModified() }?.toMutableList() ?: mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val file = files[position]
        val title = holder.itemView.findViewById<TextView>(android.R.id.text1)
        val subtitle = holder.itemView.findViewById<TextView>(android.R.id.text2)
        title.text = file.name
        subtitle.text = "${file.length()} bytes"
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, guessMime(file))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            file.delete()
            files.removeAt(position)
            notifyItemRemoved(position)
            true
        }
    }

    private fun guessMime(file: File): String = when (file.extension.lowercase()) {
        "txt", "md", "log" -> "text/plain"
        "json" -> "application/json"
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "pdf" -> "application/pdf"
        else -> "application/octet-stream"
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v)
}
