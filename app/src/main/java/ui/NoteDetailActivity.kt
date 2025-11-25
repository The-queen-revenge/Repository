package com.example.quicknotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.quicknotes.data.Categories
import com.example.quicknotes.data.Note
import com.example.quicknotes.data.NoteDatabase
import com.example.quicknotes.data.NoteRepository
import com.example.quicknotes.databinding.ActivityNoteDetailBinding
import kotlinx.coroutines.launch
import com.example.quicknotes.R

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var noteRepository: NoteRepository
    private var currentNote: Note? = null
    private var selectedCategory: String = Categories.NO_CATEGORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = NoteDatabase.getInstance(this)
        noteRepository = NoteRepository(database.noteDao())

        setupToolbar()
        setupCategorySpinner()
        setupClickListeners()
        loadNoteData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupCategorySpinner() {
        val categories = Categories.getLocalized(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        binding.categorySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            saveNote()
        }

        binding.deleteButton.setOnClickListener {
            deleteNote()
        }
    }

    private fun loadNoteData() {
        val noteId = intent.getLongExtra("note_id", -1L)

        if (noteId == -1L) {
            binding.toolbar.title = getString(R.string.new_note)
            binding.deleteButton.visibility = android.view.View.GONE
        } else {
            binding.toolbar.title = getString(R.string.edit_note)
            binding.deleteButton.visibility = android.view.View.VISIBLE

            lifecycleScope.launch {
                val note = noteRepository.getNoteById(noteId)
                note?.let {
                    currentNote = it
                    binding.titleEditText.setText(it.title)
                    binding.contentEditText.setText(it.content)


                    val categories = Categories.getLocalized(this@NoteDetailActivity)
                    val categoryIndex = categories.indexOfFirst { category ->
                        category == it.category
                    }.takeIf { it != -1 } ?: 0
                    binding.categorySpinner.setSelection(categoryIndex)
                }
            }
        }
    }

    private fun saveNote() {
        val title = binding.titleEditText.text.toString().trim()
        val content = binding.contentEditText.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_title), Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            if (currentNote == null) {
                val newNote = Note(
                    title = title,
                    content = content,
                    category = selectedCategory
                )
                noteRepository.insertNote(newNote)
                Toast.makeText(this@NoteDetailActivity, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
            } else {
                val updatedNote = currentNote!!.copy(
                    title = title,
                    content = content,
                    category = selectedCategory
                )
                noteRepository.updateNote(updatedNote)
                Toast.makeText(this@NoteDetailActivity, getString(R.string.note_updated), Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun deleteNote() {
        currentNote?.let { note ->
            lifecycleScope.launch {
                noteRepository.deleteNote(note)
                Toast.makeText(this@NoteDetailActivity, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        fun newIntent(context: android.content.Context, noteId: Long = -1L): android.content.Intent {
            return android.content.Intent(context, NoteDetailActivity::class.java).apply {
                putExtra("note_id", noteId)
            }
        }
    }
}