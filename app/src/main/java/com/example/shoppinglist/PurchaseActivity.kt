package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PurchaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //TODO da inserire in un box sotto la voce descrizione
            //DropDownMenu()
            MainScreen()
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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(){
    var shoppingItems by remember { mutableStateOf(listOf("Pane", "Latte", "Uova")) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Lista della spesa", fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        shoppingItems.forEachIndexed { index, item ->
            ItemRow(
                itemText = item,
                isChecked = false,
                onCheckedChange = { isChecked ->
                    // Gestisci il cambiamento di stato della checkbox
                },
                onDeleteClick = {
                    // Gestisci l'eliminazione dell'elemento
                    shoppingItems = shoppingItems.toMutableList().apply {
                        removeAt(index)
                    }
                }
            )
        }
        DropDownMenu()
    }



}

@Composable
fun ItemRow(itemText: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Testo dell'elemento
        Text(
            text = itemText,
            fontSize = 20.sp
        )

        // Checkbox
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) }
        )

        // Bottone Elimina
        Button(
            onClick = onDeleteClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "Elimina") //qui sarebbe carino mettere l'icona del cestino
        }
    }
}



