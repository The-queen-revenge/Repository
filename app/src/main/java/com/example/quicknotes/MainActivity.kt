package com.example.quicknotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicknotes.data.Note
import com.example.quicknotes.data.NoteDatabase
import com.example.quicknotes.data.NoteRepository
import com.example.quicknotes.databinding.ActivityMainBinding
import com.example.quicknotes.ui.NotesAdapter
import com.example.quicknotes.ui.NoteDetailActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupDatabase()
        setupClickListeners()
        loadNotesFromDatabase()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter()

        notesAdapter.onNoteClick = { note ->
            val intent = NoteDetailActivity.newIntent(this, note.id)
            startActivity(intent)
        }

        notesAdapter.onNoteLongClick = { note ->
            showDeleteDialog(note)
        }

        binding.recyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupDatabase() {
        val database = NoteDatabase.getInstance(this)
        noteRepository = NoteRepository(database.noteDao())
    }

    private fun setupClickListeners() {
        binding.addNoteButton.setOnClickListener {
            val intent = NoteDetailActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun loadNotesFromDatabase() {
        lifecycleScope.launch {
            noteRepository.getAllNotes().collect { notes ->
                notesAdapter.submitList(notes)
            }
        }
    }

    private fun showDeleteDialog(note: Note) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.delete_note))
        builder.setMessage(getString(R.string.delete_confirmation))
        builder.setPositiveButton(getString(R.string.delete_confirm)) { dialog, which ->
            deleteNote(note)
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.show()
    }

    private fun deleteNote(note: Note) {
        lifecycleScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotesFromDatabase()
    }
}