package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window

class PurchaseActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        //TODO
        // interfaccia: 1 pagina con lista categorie e relativi items e pop up per combo box
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addProduct: Button=findViewById(R.id.addProduct)

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


        confirmAdd.setOnClickListener(){
            dialog.dismiss()
        }

        dialog.show()
    }


}