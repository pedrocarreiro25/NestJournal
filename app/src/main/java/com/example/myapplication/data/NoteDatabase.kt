package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Defines the Room database configuration for the "Note" entity.
// This class serves as the main access point to the underlying database connection.
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val dao: NoteDao
}
