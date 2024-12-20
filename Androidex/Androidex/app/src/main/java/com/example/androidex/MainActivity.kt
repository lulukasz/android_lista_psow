package com.example.androidex

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Delete
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psy.ui.theme.PsyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PsyTheme {
                DogListScreen()
            }
        }
    }
}

@Composable
fun DogListScreen() {
    var dogName by remember { mutableStateOf(TextFieldValue("")) }
    var dogs by remember { mutableStateOf(listOf<String>()) }
    var favoriteDogs by remember { mutableStateOf(setOf<String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "")

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = dogName,
                onValueChange = {
                    dogName = it
                    errorMessage = null
                },
                label = { Text("Poszukaj lub dodaj pieska 🐕") },
                modifier = Modifier
                    .weight(1f)
                    .background(if (errorMessage != null) Color(0xFFFFCDD2) else Color.Transparent),
                singleLine = true,
                isError = errorMessage != null
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    val trimmedName = dogName.text.trim()
                    if (trimmedName.isNotBlank()) {
                        if (!dogs.contains(trimmedName)) {
                            dogs = listOf(trimmedName) + dogs
                            dogName = TextFieldValue("")
                        } else {
                            errorMessage = "Piesek już istnieje na liście!"
                        }
                    }
                },
                enabled = dogName.text.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Dog")
            }

            IconButton(
                onClick = {},
                enabled = dogName.text.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Dog")
            }
        }

        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(text = "🐶: ${dogs.size}", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "💜: ${favoriteDogs.size}", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(dogs) { dog ->
                DogItem(
                    name = dog,
                    isFavorite = favoriteDogs.contains(dog),
                    onFavoriteClick = {
                        favoriteDogs = if (favoriteDogs.contains(dog)) {
                            favoriteDogs - dog
                        } else {
                            favoriteDogs + dog
                        }
                        dogs = dogs.sortedByDescending { favoriteDogs.contains(it) }
                    },
                    onDeleteClick = {
                        dogs = dogs - dog
                        favoriteDogs = favoriteDogs - dog
                    }
                )
            }
        }
    }
}

@Composable
fun DogItem(name: String, isFavorite: Boolean, onFavoriteClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, fontSize = 18.sp, modifier = Modifier.weight(1f))

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DogListScreenPreview() {
    PsyTheme {
        DogListScreen()
    }
}
