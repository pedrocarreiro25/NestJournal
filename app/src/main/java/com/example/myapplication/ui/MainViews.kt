package com.example.myapplication.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.Note
import com.example.myapplication.ui.MainViewModel

// Composable for the main view of the app
@Composable
fun MainView(vm: MainViewModel) {
    val navController = rememberNavController()
    val state = vm.selectedSreen.collectAsState()

    // Scaffold is used to structure the main layout, including the bottom bar and main content
    Scaffold(
        bottomBar = { bottomBar(navController, state.value) },
        modifier = Modifier.padding(top = 40.dp)
    ) { paddingValues ->
        // Navigation host to manage different screens (Form, List, Favourites, etc.)
        NavHost(
            navController = navController,
            startDestination = "Form",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Form") {
                vm.selectPage("Form")
                FormView(vm)  // Composable for creating new notes
            }
            composable("List") {
                vm.selectPage("List")
                vm.getAllNotes()  // Fetch all notes from the database
                ListView(vm, navController)  // Display the note list
            }
            composable("Favourites") {
                vm.selectPage("Favourites")
                vm.getAllFavouriteNotes()  // Fetch favorite notes
                FavouriteView(vm, navController)  // Display the favorite notes
            }
            composable("EditScreen") {
                vm.selectPage("Edit")
                editScreen(vm, navController)  // Screen to edit an existing note
            }
        }
    }
}

// Composable for the bottom navigation bar
@Composable
fun bottomBar(nav: NavController, screen: String) {
    NavigationBar(
        modifier = Modifier.height(90.dp)
    ) {
        // Navigation bar items with icons and labels
        NavigationBarItem(
            selected = screen == "Form",
            onClick = { nav.navigate("Form") },
            label = {
                Text(
                    text = stringResource(R.string.new_note),
                    fontFamily = FontFamily.SansSerif
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = colorResource(id = R.color.black)
                )
            }
        )
        NavigationBarItem(
            selected = screen == "List",
            onClick = { nav.navigate("List") },
            label = {
                Text(
                    text = stringResource(R.string.all_notes),
                    fontFamily = FontFamily.SansSerif
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "List",
                    tint = colorResource(id = R.color.black)
                )
            }
        )
        NavigationBarItem(
            selected = screen == "Favourites",
            onClick = { nav.navigate("Favourites") },
            label = {
                Text(
                    text = stringResource(R.string.favourites),
                    fontFamily = FontFamily.SansSerif
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favourites",
                    tint = colorResource(id = R.color.black)
                )
            }
        )
    }
}

// Composable for the form where users can create new notes
@Composable
fun FormView(vm: MainViewModel) {
    // Variables to store note content and date
    var note: String by rememberSaveable { mutableStateOf("") }
    var date: String by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Heading for the form
        Text(
            text = stringResource(R.string.new_note),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 30.dp),
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        // Input field for the note content
        OutlinedTextField(
            modifier = Modifier.sizeIn(
                minWidth = 300.dp,
                maxWidth = 300.dp,
                minHeight = 50.dp,
                maxHeight = 100.dp
            ),
            value = note,
            onValueChange = { note = it },
            label = {
                Text(text = stringResource(R.string.note), fontFamily = FontFamily.SansSerif)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Input field for the date
        OutlinedTextField(
            modifier = Modifier.sizeIn(
                minWidth = 300.dp,
                maxWidth = 300.dp,
                minHeight = 50.dp,
                maxHeight = 100.dp
            ),
            value = date,
            onValueChange = { date = it },
            label = {
                Text(text = stringResource(R.string.date), fontFamily = FontFamily.SansSerif)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Button to save the note
        Button(
            modifier = Modifier.sizeIn(minWidth = 100.dp, minHeight = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.white)
            ),
            onClick = {
                vm.saveNote(Note(note, date))
                // Delete the form information for a new note after saving one
                note = ""
                date = ""
            }
        ) {
            Text(
                text = stringResource(R.string.save),
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp
            )
        }
    }
}

// Composable for the Edit screen to modify existing notes
@Composable
fun editScreen(vm: MainViewModel, nav: NavController) {
    // Retrieve the note ID from the ViewModel
    val noteID = vm.editNote!!.id
    val noteFavourite = vm.editNote!!.favourite

    // Variables to store the edited note content and date
    var note by rememberSaveable { mutableStateOf(vm.editNote?.note) }
    var date by rememberSaveable { mutableStateOf(vm.editNote?.date) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.edit_note),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 30.dp),
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            modifier = Modifier.sizeIn(
                minWidth = 300.dp, maxWidth = 300.dp,
                minHeight = 50.dp, maxHeight = 100.dp
            ),
            value = note!!,
            onValueChange = { note = it },
            label = { Text(text = stringResource(R.string.note)) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.sizeIn(minWidth = 300.dp, maxWidth = 300.dp, minHeight = 50.dp),
            value = date!!,
            onValueChange = { date = it },
            label = { Text(text = stringResource(R.string.date)) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.sizeIn(minWidth = 100.dp, minHeight = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.white)
            ),
            onClick = {
                vm.updateNote(note!!, date!!, noteID, noteFavourite)
                nav.navigate("List")  // After saving, navigate back to the list
            }
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

// Composable to display the list of all notes
@Composable
fun ListView(vm: MainViewModel, navController: NavController) {
    val state = vm.noteList.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.list_of_notes),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 30.dp),
                fontSize = 25.sp

            )
        }
        items(state.value) {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(10.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                )
                {
                    // Edit the appearance of the note in the note list and delete if a note
                    // does not have any written content
                    if (it.note == "") {
                        vm.deleteNote(it)
                    } else {
                        if (it.date == "") {
                            Text(text = it.note)
                        } else {
                            Text(text = it.note)
                            Text(text = it.date)
                        }
                    }
                }

                // Edit icon, navigates to the Edit screen
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        vm.editNote = it
                        navController.navigate("EditScreen")
                    }
                )
                // Favourite icon, toggles between favourite and not favourite
                if (it.favourite == 1) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favourite",
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                vm.editNote = it
                                vm.removeFavorite(it)
                                navController.navigate("List")
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Not Favourite",
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                vm.editNote = it
                                vm.addFavourite(it)
                                navController.navigate("List")
                            }
                    )
                }

                // Delete icon, deletes the note
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .clickable { vm.deleteNote(it) }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

// Composable for the favourite notes view
@Composable
fun FavouriteView(vm: MainViewModel, navController: NavController) {
    val state = vm.favouriteList.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.favourite_note),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 30.dp),
                fontSize = 25.sp
            )
        }
        items(state.value) {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)

                ) {   //Edit the view of the note appereance in the note list
                    if (it.note == "") {
                        vm.deleteNote(it)
                    } else {
                        if (it.date == "") {
                            Text(text = it.note)
                        } else {
                            Text(text = it.note)
                            Text(text = it.date)
                        }
                    }
                }

                // Favourite icon, toggles between favourite and not favourite
                if (it.favourite == 1) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favourite",
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable {
                                vm.editNote = it
                                vm.removeFavorite(it)
                                navController.navigate("Favourites")
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Not Favourite",
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable {
                                vm.editNote = it
                                vm.addFavourite(it)
                                navController.navigate("Favourites")
                            }
                    )
                }
                // Edit icon, navigates to the Edit screen
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        vm.editNote = it
                        navController.navigate("EditScreen")
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}



