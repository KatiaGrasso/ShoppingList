package com.example.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PurchasableItem (val description:String, val category:String, var isPurchased: Boolean=false){

}


@Composable
fun ItemRow(itemDescription: String, itemCategory: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column ( modifier = Modifier.padding(2.dp)){
            Text(
                text = itemDescription,
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
            onClick = onDeleteClick,
            modifier = Modifier.padding(start = 8.dp),

        ) {
            Icon(imageVector = Icons.Outlined.Delete,
                contentDescription = "Elimina",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
            Modifier.drawWithContent { drawContent() }
    }
    }
}

