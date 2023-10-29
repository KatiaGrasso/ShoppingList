package com.example.shoppinglist


import androidx.lifecycle.ViewModel


class PurchaseViewModel: ViewModel() {
    val itemList = mutableListOf<PurchasableItem>() //Lista degli elementi

    fun addItem(description: String, category: String)
    {
        itemList.add(PurchasableItem(description, category))
    }

    //TODO
    // metodi: 4
    // aggiunta item
    // rimozione item
    // check item
    // aggiunta categoria (chiave) con items (valori) -> Lista

}