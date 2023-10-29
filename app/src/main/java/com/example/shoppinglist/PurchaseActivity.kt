package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class PurchaseActivity : AppCompatActivity() {
    val viewModel=PurchaseViewModel()
    var itemList=viewModel.itemList
    lateinit var showItemsView: TextView


        override fun onCreate(savedInstanceState: Bundle?) {
        //TODO
        // interfaccia: 1 pagina con lista categorie e relativi items e pop up per combo box
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addProduct: Button=findViewById(R.id.addProduct)
        showItemsView=findViewById(R.id.showItems)

        viewModel.addItem("Carote", "Verdura")
        showItems()

        addProduct.setOnClickListener{

            addProductForm()
        }
    }

    private fun addProductForm(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.form)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val confirmAdd=dialog.findViewById<Button>(R.id.confirmAdd)
        val inputDescription: TextInputEditText =dialog.findViewById(R.id.inputDescription)
        val inputCategory: TextInputEditText=dialog.findViewById(R.id.inputCategory)


        confirmAdd.setOnClickListener(){
            val description = inputDescription.text.toString()
            val category=inputCategory.text.toString()
            viewModel.addItem(description, category)
            showItems()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showItems(){

        var t=""
        for (i in itemList){
            t+=i.description+" "+i.category+"\n"
        }
        showItemsView.text=t


    }



}