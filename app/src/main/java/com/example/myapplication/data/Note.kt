package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define this data class as a database entity with the table name "notes"
@Entity(tableName = "notes")
data class Note(

    // Represents the content of the note as a string
    val note: String = "",

    // Represents the date associated with the note as a string
    // Consider changing the type to Date or similar for better date handling
    val date: String = "",

    // Marks if the note is a favorite; 1 indicates true, 0 indicates false
    var favourite: Int = 0,

    // Represents the primary key for the note entity; auto-generated by Room
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
