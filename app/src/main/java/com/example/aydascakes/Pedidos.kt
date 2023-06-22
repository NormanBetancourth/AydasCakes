package com.example.aydascakes

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.Pedido
import com.example.aydascakes.model.Producto

class Pedidos : AppCompatActivity(){

    lateinit var productosRecyclerView : RecyclerView
    lateinit var productosArrayList :  ArrayList<Producto>
    lateinit var btnRegresar : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedidos)

        productosRecyclerView = findViewById(R.id.pedidosLista)
        btnRegresar = findViewById(R.id.btn_regresar)
        productosRecyclerView.layoutManager = LinearLayoutManager(this)
        productosRecyclerView.setHasFixedSize(true)

        productosArrayList = arrayListOf<Producto>()
        productosRecyclerView.adapter = MyAdapter(productosArrayList)
        //getPedidosData()

        //Cambia a pantalla de inicio
        btnRegresar.setOnClickListener {
            val intent: Intent = Intent(this,ModificarDatos::class.java)
            startActivity(intent)
        }
    }
    private fun getPedidosData(){
        Pedido.getPedidos().
        thenAccept { listaPedidos ->
            if (listaPedidos.isNotEmpty()){
                listaPedidos.forEach { i ->
                    if(i.usuario == "1YJnzL8oqLFdukaLDs88"){
                        Log.d(ContentValues.TAG, "GET Pedido: $listaPedidos" )
                    }
                }
                productosRecyclerView.adapter = MyAdapter(productosArrayList)
            }

        }
    }
}