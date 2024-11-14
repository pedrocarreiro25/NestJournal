package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for the Note entity
// Defines the database operations that can be performed on the "notes" table.
@Dao
interface NoteDao {

    // Inserts a new note into the "notes" table.
    @Insert
    suspend fun insertNote(note: Note)

    // Updates an existing note in the "notes" table.
    @Update
    suspend fun updateNote(note: Note)

    // Deletes a specific note from the "notes" table.
    @Delete
    suspend fun deleteNote(note: Note)

    // Retrieves all notes from the "notes" table, ordered by date (descending) and then by note
    // content (descending).
    @Query("SELECT * FROM notes ORDER BY date DESC, note DESC")
    fun getNotes(): Flow<List<Note>>
}
