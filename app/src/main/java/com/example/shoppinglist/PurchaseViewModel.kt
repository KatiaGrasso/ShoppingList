package com.example.shoppinglist


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

    var map= mutableMapOf<String, MutableList<PurchasableItem>>()
    fun addItem(description: String, category: String)
    {
        var item=PurchasableItem(description, category)

        if(map.containsKey(category)){
            map[category]?.add(item)
        }
        else{
            var startList= mutableListOf<PurchasableItem>()
            startList.add(item)
            map.put(category, startList)
        }
    }
    fun updateItem(item: PurchasableItem){

    }
    fun removeItem(item: PurchasableItem){
        var categoryList = map[item.category]
        if(categoryList?.size==1){
            map.remove(item.category)
        }
        else{
            categoryList?.remove(item)
        }
    }

}
val viewModel=PurchaseViewModel()
var description_toAdd =""
var category_toAdd= ""

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
    description_toAdd=""
    description_toAdd=text
}

//scelta categoria
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ChooseCategory() {

    var text by remember { mutableStateOf("") }
    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    var category by remember {
        mutableStateOf("Categoria") //default: nessuna scelta
    }

    Row() {
        if (category == "Aggiungi categoria") {
            TextField(
                value = text,
                onValueChange = { text = it;category_toAdd=text },
                label = { Text("Inserisci categoria") },
                modifier = Modifier.fillMaxWidth()

            )
        } else {
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
                    viewModel.map.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Text(text = cat.key)
                            },
                            onClick = {
                                category = cat.key
                                category_toAdd = ""
                                text = cat.key
                                category_toAdd = text
                                isExpanded = false //se clicco su una categoria, chiudo il menù a tendina

                            })
                    }

                    DropdownMenuItem(
                        text = {
                            Text(text = "Aggiungi categoria")
                            //TODO aggiungere bottone
                        },
                        onClick = {
                            category = "Aggiungi categoria"

                        })


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