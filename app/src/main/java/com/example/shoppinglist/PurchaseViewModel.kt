package com.example.shoppinglist


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PurchaseViewModel: ViewModel() {

    var map= MutableLiveData<Map<String, List<PurchasableItem>>>(
        emptyMap()
    )
    var items=MutableLiveData<List<PurchasableItem>>(emptyList())
    var categories = MutableLiveData<List<String>>(emptyList())

    fun addItem(description: String, category: String)
    {
        var item=PurchasableItem(description, category)
        if(description!= "" && category!= ""){
            items.value = items.value?.plus(item) ?: listOf(item)
            map.value = items.value?.groupBy { it.category } ?: emptyMap()
            if(categories.value?.contains(category) == false){
                categories.value = categories.value?.plus(category) ?: listOf(category)
            }
        }


    }
    fun addCategory(category: String): Boolean{
        var flag=false
        if(categories.value?.contains(category) == false && category!=null && category!=""){
            categories.value = categories.value?.plus(category) ?: listOf(category)
        }
        else
            flag=true
        return flag
    }

    fun modifyCategory(categoriaOriginale: String, categoriaModificata: String){
        if (categoriaModificata.isNotEmpty()) {
            categories.value = categories.value?.map {
                if (it == categoriaOriginale) {
                    categoriaModificata
                } else {
                    it
                }
            }
        }
    }

    fun checkItem(item: PurchasableItem, index: Int){
        var map_copy=map
        if(item.isPurchased){
            item.isPurchased=false
            map.value?.get(item.category)?.get(index)?.isPurchased=false

        }
        else{
            item.isPurchased=true
            map.value?.get(item.category)?.get(index)?.isPurchased=true

        }
    }

    fun removeItem(item: PurchasableItem){

        items.value = items.value?.filter { it != item } ?: emptyList()
        map.value = items.value?.groupBy { it.category } ?: emptyMap()
    }

    fun clearItems() {
        items.value = emptyList()
        map.value = emptyMap()
    }

    //rimuovere da side menu e drop down menu
    fun removeCategory(category: String) {
        categories.value =  categories.value?.filter { it != category } ?: emptyList()
    }

    //da view Model
    fun removeCategoryAndItems(category: String) {
        items.value = items.value?.filter { it.category != category } ?: emptyList()
        map.value = items.value?.groupBy { it.category } ?: emptyMap()
    }


}

var description_toAdd =""
var category_toAdd= ""

// aggiunta desrizione
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDescription() {
    var text by remember {
        mutableStateOf("")
    }
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
fun ChooseCategory(viewModel: PurchaseViewModel) {

    var text by remember { mutableStateOf("") }
    var isExpanded by remember {
        mutableStateOf(false) //default: menù chiuso
    }
    var category by remember {
        mutableStateOf("Categoria") //default: nessuna scelta
    }

    val categories by viewModel.categories.observeAsState()

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
                         ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) //icona di default per i menù a tendina
                       }, modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                )
                {
                    categories?.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Text(text = cat)
                            },
                            onClick = {
                                category = cat
                                category_toAdd = ""
                                text = cat
                                category_toAdd = text
                                isExpanded = false //se clicco su una categoria, chiudo il menù a tendina
                            })
                    }

                    DropdownMenuItem(
                        text = {
                            Text(text = "Aggiungi categoria")

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
fun PopupMenu(viewModel: PurchaseViewModel) {

    Box(
        modifier = Modifier.fillMaxSize(0.8F),
        contentAlignment = Alignment.Center
    ) {
        Column { // Utilizzo  Column per garantire che Categoria e Descrizione non si sovrappongano
            ChooseCategory(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            AddDescription()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategory(viewModel: PurchaseViewModel) {

    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it;category_toAdd=text },
        label = { Text("Inserisci categoria") },
        modifier = Modifier.fillMaxWidth()

    )


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyCategory(viewModel: PurchaseViewModel, cat: String) {

    var text by remember { mutableStateOf(cat) }

    TextField(
        value = text,
        onValueChange = { text = it;category_toAdd=text },
        modifier = Modifier.fillMaxWidth()

    )


}

