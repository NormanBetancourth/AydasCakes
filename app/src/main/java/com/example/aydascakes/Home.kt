package com.example.aydascakes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.utilities.ProductoAdapter
import com.example.aydascakes.model.Producto
import com.example.aydascakes.utilities.CustomItemDecorator

class Home : AppCompatActivity() {

    private lateinit var productos : List<Producto>

    private lateinit var recyclerView: RecyclerView

    private val productosTest : List<Producto> = listOf<Producto>(
        Producto("1", "Queque Chocolate", "lorem ipsum dolor", 12000.0, "queque_chocolate"),
        Producto("2", "Galletas de Chocolate", "lorem ipsum dolor", 4000.0, "galletas_chocolate"),
        Producto("3", "Torta Chilena", "lorem ipsum dolor", 4000.0, "torta_chilena"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        productos = Producto.getProductos().getNow(productosTest)

        recyclerView = findViewById(R.id.productosRecycler)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ProductoAdapter(productos, this)
        recyclerView.addItemDecoration(CustomItemDecorator(25))

    }
}

