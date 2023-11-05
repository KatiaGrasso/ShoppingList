package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class PurchaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addItem("Carote", "Verdura")
        viewModel.addItem("Zucchine", "Verdura")
        viewModel.addItem("Uva", "Frutta")
        viewModel.addCategory( "Frutta")
        viewModel.addCategory("Verdura")
        setContent {
            //TODO da inserire in un box sotto la voce descrizione
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen() {

    var itemList = viewModel.itemList
    var shoppingItems by remember { mutableStateOf(itemList) }
    var showDialog by remember { mutableStateOf(false) }
    var categories= viewModel.categories
    var mappa= viewModel.map
    var map by remember { mutableStateOf(mappa) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,


    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Lista della spesa", fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        map.forEach{item->
            var list=item.value
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .size(50.dp),
                shape = RoundedCornerShape(16.dp)
            ){ Text(text = item.key, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)}

            list.forEachIndexed{index, item ->
                var isChecked by remember { mutableStateOf(item.isPurchased) }
                ItemRow(
                    itemDescription = item.description,
                    itemCategory = item.category,
                    isChecked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        // Puoi anche gestire l'aggiornamento dell'elemento nel tuo ViewModel qui
                        viewModel.updateItem(item.copy(isPurchased = it))
                    },
                    onDeleteClick = {
                        // Gestisci l'eliminazione dell'elemento
                        shoppingItems = shoppingItems.toMutableList().apply {
                            removeAt(index)
                        }
                        list = list.toMutableList().apply {
                            removeAt(index)
                        }

                        viewModel.removeItem(item)


                        if(index!=shoppingItems.size)
                            isChecked=shoppingItems.get(index).isPurchased

                    }
                )


            }
        }



        //Text(text = itemList.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist
        //Text(text = shoppingItems.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist
        //Text(text = map.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist
        //Text(text = mappa.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter

    ) {
        Row {

            // Pulsante per aprire il popup
            Button(
                onClick = { showDialog = true }
                , Modifier.drawWithContent {
                    drawContent()
                }
            ) {
                Text("Aggiungi")
            }

            if (showDialog) {
                // Dialog per il popup
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
                            .height(300.dp)
                            .padding(16.dp)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        PopupMenu() //contenuto pop-up: richiamo l'analoga funzione in PurchaseViewModel
                        Button(
                            onClick = {
                                showDialog = false;
                                shoppingItems.add(PurchasableItem(description_toAdd, category_toAdd))// Chiudo il popup premendo su chiudi
                                viewModel.addItem(description_toAdd, category_toAdd)

                            },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Text("Chiudi")
                        }

                    }
                }
            }

            Button(
                onClick = {
                    shoppingItems = emptyList<PurchasableItem>().toMutableList()
                    itemList= emptyList<PurchasableItem>().toMutableList()
                    categories.clear()
                }, Modifier.drawWithContent {
                    drawContent()
                }
            ) {
                Text("Clear All")

            }
        }

    }

}

