package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Note
import com.example.myapplication.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// ViewModel for managing the UI-related data in a lifecycle-conscious way
class MainViewModel(private val dao: NoteDao) : ViewModel() {

    // StateFlow to manage the currently selected screen in the app
    private val _selectedScreen = MutableStateFlow("")
    val selectedSreen: StateFlow<String> = _selectedScreen.asStateFlow()

    // StateFlow to store the list of all notes from the database
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList: StateFlow<List<Note>> = _noteList.asStateFlow()

    // StateFlow to store the list of favorite notes
    private val _favouriteList = MutableStateFlow<List<Note>>(emptyList())
    val favouriteList: StateFlow<List<Note>> = _favouriteList.asStateFlow()


    // Variable to hold the note that is being edited
    var editNote: Note? = null

    // Function to set the selected screen for navigation or display
    fun selectPage(screen: String) {
        _selectedScreen.value = screen
    }

    // Function to save a new note to the database and then fetch all notes
    fun saveNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note) // Inserts the note into the database
            getAllNotes()         // Refreshes the list of notes
        }
    }

    // Function to delete a note from the database and refresh the list of notes
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note) // Deletes the note from the database
            getAllNotes()        // Refreshes the list of notes
        }
    }

    // Function to fetch all notes from the database and update the note list
    fun getAllNotes() {
        viewModelScope.launch {
            dao.getNotes().collect { data ->
                _noteList.value = data
            } // Collects notes from DAO and updates the state
        }
    }

    // Function to update an existing note with new values
    fun updateNote(noteEdit: String, dateEdit: String, idEdit: Int, favouriteEdit: Int) {
        val updateNote: Note = Note(
            note = noteEdit,
            date = dateEdit,
            id = idEdit,
            favourite = favouriteEdit
        )
        viewModelScope.launch {
            dao.updateNote(updateNote) // Updates the note in the database
        }
    }

    // Function to fetch all favorite notes from the database and update the favorite list
    fun getAllFavouriteNotes() {
        viewModelScope.launch {
            dao.getNotes().collect { data ->
                _favouriteList.value =
                    data.filter { it.favourite == 1 } // Filters notes to include only favorites
            }
        }
    }

    // Function to mark a note as a favorite
    fun addFavourite(note: Note) {
        var notetmp = note
        notetmp.favourite = 1 // Sets the note's favorite status to true (1)
        viewModelScope.launch {
            dao.updateNote(notetmp) // Updates the note in the database
        }
    }

    // Function to remove a note from the favorites
    fun removeFavorite(note: Note) {
        var tmpnote = note
        tmpnote.favourite = 0 // Sets the note's favorite status to false (0)
        viewModelScope.launch {
            dao.updateNote(tmpnote) // Updates the note in the database
        }
    }

}
