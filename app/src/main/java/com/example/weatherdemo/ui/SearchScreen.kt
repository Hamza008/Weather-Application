package com.example.weatherdemo.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherdemo.navigation.NavigationGraph
import com.example.weatherdemo.models.Zila
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    val zilaList: List<Zila> = remember { loadZilaList(context) } // Load Zila list
    val query = remember { mutableStateOf("") } // Search query

    val filteredZilas = remember(query.value) {
        zilaList.filter {
            it.name.contains(query.value, ignoreCase = true)
        } // Filter Zilas by query
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TextField for Search
            TextField(
                value = query.value,
                onValueChange = { query.value = it },
                label = { Text("Search Zila") },
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    if (query.value.isNotBlank()) {
                        IconButton(onClick = { query.value = "" }) {//Clear search query
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (query.value.isNotBlank()) {
                        val isZilaValid =
                            zilaList.any { it.name.equals(query.value, ignoreCase = true) }
                        if (isZilaValid) {
                            navController.navigate(NavigationGraph.Home.createRoute(query.value)) {
                                popUpTo(NavigationGraph.Home.route) {
                                    inclusive = true
                                } // Remove Search screen from the stack
                            }
                        } else {
                            // If the zila is not valid, show a Toast
                            Toast.makeText(
                                context,
                                "Please provide a valid zila name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Search Button
            Button(
                onClick = {
                    if (query.value.isNotBlank()) {
                        val isZilaValid =
                            zilaList.any { it.name.equals(query.value, ignoreCase = true) }
                        if (isZilaValid) {
                            navController.navigate(NavigationGraph.Home.createRoute(query.value)) {
                                popUpTo(NavigationGraph.Home.route) {
                                    inclusive = true
                                } // Remove Search screen from the stack
                            }
                        } else {
                            // If the zila is not valid, show a Toast
                            Toast.makeText(
                                context,
                                "Please provide a valid zila name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.height(56.dp) // Align height with TextField
            ) {
                Text("Search")
            }
        }

        // Dynamic List of Zilas
        if (filteredZilas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No matching zilas found",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                items(filteredZilas.size) { index ->
                    // Zila Card for Each Result
                    val isSelected = query.value == filteredZilas[index].name

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colors.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { query.value = filteredZilas[index].name },
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Zila Icon",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            HighlightedText(
                                fullText = filteredZilas[index].name,
                                query = query.value
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HighlightedText(fullText: String, query: String) {
    val parts = fullText.split(query, ignoreCase = true)
    Log.i("weatherApp", "parts = $parts")
    val annotatedString = buildAnnotatedString {
        for (i in parts.indices) {
            append(parts[i])
            if (i != parts.lastIndex) {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append(query)
                }
            }
        }
    }
    Text(annotatedString, style = MaterialTheme.typography.body1)
}


private fun loadZilaList(context: Context): List<Zila> {
    val json = context.assets.open("ZilaList.json").bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Zila>>() {}.type
    return Gson().fromJson(json, listType)
}