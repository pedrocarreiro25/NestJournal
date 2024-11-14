package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.data.NoteDatabase
import com.example.myapplication.ui.MainView
import com.example.myapplication.ui.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

// MainActivity is the entry point of the application
// It extends ComponentActivity and is used to initialize and set up the app's UI
class MainActivity : ComponentActivity() {

    // Lazy-initialized database instance
    // Creates a Room database instance named "NoteDatabase.db"
    private val db by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, "NoteDatabase.db").build()
    }

    // ViewModel initialization with custom factory
    val vm by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T // Creates the ViewModel, passing the DAO to it
                }
            }
        }
    )

    // onCreate method is called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge display, making the content stretch to the screen edges
        enableEdgeToEdge()

        // Sets the content of the activity with Jetpack Compose
        setContent {
            MyApplicationTheme {  // Applying the app's theme
                Surface(
                    modifier = Modifier.fillMaxSize(),  // Makes the Surface take up the whole screen
                    color = MaterialTheme.colorScheme.background  // Sets the background color based on the theme
                ) {
                    MainView(vm)  // Passes the ViewModel to MainView to display the UI
                }
            }
        }
    }
}
