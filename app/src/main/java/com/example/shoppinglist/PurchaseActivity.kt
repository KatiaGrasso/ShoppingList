package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class PurchaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //TODO da inserire in un box sotto la voce descrizione
            DropDownMenu()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DropDownMenu() {

    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    var category by remember {
        mutableStateOf("") //default: nessuna scelta
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center )
    {
        ExposedDropdownMenuBox(
            expanded = isExpanded ,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    //icona di default per i menù a tendina
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Frutta")
                    },
                    onClick = {
                        category = "Frutta"
                        isExpanded = false //se clicco su una categoria, chiudo il menù a tendina
                    })

                DropdownMenuItem(
                    text = {
                        Text(text = "Verdure")
                    },
                    onClick = {
                        category = "Verdura"
                        isExpanded = false
                    })

                DropdownMenuItem(
                    text = {
                        Text(text = "Aggiungi categoria")
                        //TODO aggiungere bottone
                    },
                    onClick = {
                        category = ""
                        isExpanded = false
                        //TODO pop up per nome nuova categoria
                    })
            }
        }
    }
}