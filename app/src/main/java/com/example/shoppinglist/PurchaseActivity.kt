package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
        setContent {
            //TODO da inserire in un box sotto la voce descrizione
            MainScreen()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var mappaVista by remember { mutableStateOf(viewModel.map) }
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

        mappaVista.forEach{item->
            var list by remember { mutableStateOf(item.value) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .size(50.dp),
                shape = RoundedCornerShape(16.dp)
            ){ Text(text = item.key, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            }

            list.forEachIndexed{index, itemOfList ->
                var isChecked by remember { mutableStateOf(itemOfList.isPurchased) }
                ItemRow(
                    item=itemOfList,
                    isChecked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        itemOfList.isPurchased=it
                    },
                    onDeleteClick = {
                        viewModel.removeItem(itemOfList);

                    }, mappa=mappaVista

                )


            }
        }

        Text(text = viewModel.map.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist
        Text(text = mappaVista.toString()) //serviva per controllare che gli elementi di shoppingList corrispondessero a quelli in viewmodel.itemlist

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
                    viewModel.map=mutableMapOf<String, MutableList<PurchasableItem>>()
                    mappaVista= viewModel.map
                }, Modifier.drawWithContent { drawContent() }
            ) {
                Text("Clear All")

            }
        }

    }


}

@Composable
fun ItemRow(item: PurchasableItem,
            isChecked: Boolean,
            onCheckedChange: (Boolean) -> Unit,
            onDeleteClick: () -> Unit,
            mappa: MutableMap<String, MutableList<PurchasableItem>>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column ( modifier = Modifier.padding(2.dp)){
            Text(
                text = item.description,
                fontSize = 18.sp
            )
        }

        Row( verticalAlignment = Alignment.CenterVertically) {
            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) }
            )

            // Bottone Elimina
            FloatingActionButton(
                onClick = { onDeleteClick;
                    mappa[item.category]?.remove(item);
                    if(mappa[item.category].isNullOrEmpty()){
                        mappa.remove(item.category)
                    }},
                modifier = Modifier.padding(start = 8.dp),


                ) {
                Icon(imageVector = Icons.Outlined.Delete,
                    contentDescription = "Elimina",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

            }
        }
    }

}



