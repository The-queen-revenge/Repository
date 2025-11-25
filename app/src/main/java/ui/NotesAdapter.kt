package com.example.quicknotes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quicknotes.data.Note
import com.example.quicknotes.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter : ListAdapter<Note, NotesAdapter.NoteViewHolder>(DiffCallback) {

    var onNoteClick: ((Note) -> Unit)? = null
    var onNoteLongClick: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onNoteClick?.invoke(getItem(adapterPosition))
                }
            }

            binding.root.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onNoteLongClick?.invoke(getItem(adapterPosition))
                    true
                } else {
                    false
                }
            }
        }

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.noteCategory.text = note.category

            val categoryColor = when (note.category) {
                binding.root.context.getString(com.example.quicknotes.R.string.work) ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_work)
                binding.root.context.getString(com.example.quicknotes.R.string.personal) ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_personal)
                binding.root.context.getString(com.example.quicknotes.R.string.ideas) ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_ideas)
                binding.root.context.getString(com.example.quicknotes.R.string.shopping) ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_shopping)
                binding.root.context.getString(com.example.quicknotes.R.string.other) ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_other)
                else ->
                    ContextCompat.getColor(binding.root.context, com.example.quicknotes.R.color.category_default)
            }
            binding.noteCategory.setBackgroundColor(categoryColor)

            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = Date(note.createdAt)
            binding.noteDate.text = dateFormat.format(date)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}