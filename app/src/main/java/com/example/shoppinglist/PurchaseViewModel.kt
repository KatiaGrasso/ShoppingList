package com.example.shoppinglist


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel


class PurchaseViewModel: ViewModel() {
    var itemList = mutableListOf<PurchasableItem>() //Lista degli elementi
    var categories = mutableListOf<String>()

    fun addItem(description: String, category: String)
    {
        itemList.add(PurchasableItem(description, category))
    }
    fun addCategory(category: String)
    {
        var flag=false
        for (c in categories){
            if(c==category){
                flag=true
            }
        }
        if(!flag)
            categories.add(category)
    }
    fun updateItem(item: PurchasableItem){

    }

    //TODO
    // metodi: 4
    // aggiunta item
    // rimozione item
    // check item
    // aggiunta categoria (chiave) con items (valori) -> Lista

}
val viewModel=PurchaseViewModel()

// aggiunta desrizione
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDescription() {
    var text by remember { mutableStateOf("") }
    Row {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it }, //TODO aggiunta voce alla lista degli item
            label = { Text("Descrizione") },
        )
    }
}

//scelta categoria
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ChooseCategory() {

    var text by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    var category by remember {
        mutableStateOf("Categoria") //default: nessuna scelta
    }

    var categories by remember {
        mutableStateOf(viewModel.categories)
    }
    viewModel.addCategory("Frutta")
    viewModel.addCategory("Verdura")




    Row() {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
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
            )
            {
                categories.forEachIndexed { index, cat ->
                    DropdownMenuItem(
                        text = {
                            Text(text = cat)
                        },
                        onClick = {
                            category = cat
                            isExpanded =
                                false //se clicco su una categoria, chiudo il menù a tendina
                        })
                }

                DropdownMenuItem(
                    text = {
                        Text(text = "Aggiungi categoria")
                        //TODO aggiungere bottone
                    },
                    onClick = {
                        category = ""
                        isExpanded = false
                        showDialog = true
                        //TODO pop up per nome nuova categoria
                    })

                if (showDialog) {
                    Dialog(
                        onDismissRequest = {
                            // Chiudi il popup quando l'utente tocca all'esterno
                            showDialog = false
                        }
                    ) {
                        // Contenuto del popup
                        Box(
                            modifier = Modifier //specifiche estetiche del pop up
                                .width(600.dp)
                                .height(400.dp)
                                .padding(16.dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {

                            Row {
                                OutlinedTextField(
                                    value = text,
                                    onValueChange = {
                                        text = it
                                    }, //TODO aggiunta voce alla lista degli item
                                    label = { Text("Inserisci categoria") },
                                )
                            } //contenuto pop-up: richiamo l'analoga funzione in PurchaseViewModel
                            Button(
                                onClick = {
                                    showDialog = false;
                                    viewModel.addCategory(text);
                                    category = text; // Chiudo il popup premendo su chiudi
                                },
                                modifier = Modifier.align(Alignment.BottomEnd)
                            ) {
                                Text("Aggiungi")
                            }
                        }
                    }
                }
            }

        }
    }

}




@Composable
fun PopupMenu() {

    Box(
        modifier = Modifier.fillMaxSize(0.8F),
        contentAlignment = Alignment.Center
    ) {
        Column { // Utilizzo  Column per garantire che Categoria e Descrizione non si sovrappongano
            ChooseCategory()
            Spacer(modifier = Modifier.height(16.dp))
            AddDescription()
        }
    }
}