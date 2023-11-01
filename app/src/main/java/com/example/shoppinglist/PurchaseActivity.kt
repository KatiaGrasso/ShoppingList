package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
        PopupMenu()
    }



}
