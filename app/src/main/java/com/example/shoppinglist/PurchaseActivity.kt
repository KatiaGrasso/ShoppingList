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
import androidx.compose.material3.OutlinedTextField
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
fun ChooseCategory() {

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
    val viewModel=PurchaseViewModel()
    var itemList=viewModel.itemList
    var shoppingItems by remember { mutableStateOf(itemList) }

    viewModel.addItem("Carote", "Verdura")
    viewModel.addItem("Zucchine", "Verdura")
    viewModel.addItem("Uva", "Frutta")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Lista della spesa", fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        shoppingItems.forEachIndexed { index, item ->
            var isChecked by remember { mutableStateOf(item.isPurchased) }
            ItemRow(
                itemDescription = item.description,
                itemCategory = item.category,
                isChecked = isChecked,
                onCheckedChange = { isChecked = it
                    // Puoi anche gestire l'aggiornamento dell'elemento nel tuo ViewModel qui
                    viewModel.updateItem(item.copy(isPurchased = it))
                },
                onDeleteClick = {
                    // Gestisci l'eliminazione dell'elemento
                    shoppingItems = shoppingItems.toMutableList().apply {
                        removeAt(index)
                    }
                }
            )
        }
        AddDescription()
        ChooseCategory()

    }



}

@Composable
fun ItemRow(itemDescription: String, itemCategory: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column ( modifier = Modifier.padding(2.dp),){
            Text(
                text = itemDescription,
                fontSize = 20.sp
            )
            Text(
                text = itemCategory,
                fontSize = 12.sp
            )

        }


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDescription() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text= it }, //TODO aggiunta voce alla lista degli item
        label = { Text("Descrizione") },
    )
}



