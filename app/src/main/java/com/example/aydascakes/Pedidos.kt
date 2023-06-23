package com.example.aydascakes

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.Pedido
import com.example.aydascakes.model.Producto
import com.example.aydascakes.model.Usuario
import com.example.aydascakes.service.SessionManager
import java.util.*
import kotlin.collections.ArrayList

class Pedidos : AppCompatActivity(){

    lateinit var productosRecyclerView : RecyclerView
    lateinit var productosArrayList : List<Producto>
    lateinit var btnRegresar : ImageButton
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedidos)
        sessionManager = SessionManager(this)

        productosRecyclerView = findViewById(R.id.pedidosLista)
        btnRegresar = findViewById(R.id.btn_regresar)
        productosRecyclerView.layoutManager = LinearLayoutManager(this)

        productosRecyclerView.setHasFixedSize(true)

        productosArrayList = listOf<Producto>()
        getPedidosData()

        //Cambia a pantalla de inicio
        btnRegresar.setOnClickListener {
            val intent: Intent = Intent(this,ModificarDatos::class.java)
            startActivity(intent)
        }
    }
    //Obtiene los pedidos del usuario logueado
    private fun getPedidosData(){
        val usuario = sessionManager.obtenerObjeto("usuario", Usuario::class.java) as Usuario

        /*Pedido.getPedidos().
        thenAccept { pedidos ->
            pedidos.forEach { pedido ->
                if(pedido.usuario == usuario.id){
                    Log.d(ContentValues.TAG, "GET Pedido: $pedido" )
                    Producto.getProductos()
                        .thenAccept{productos ->
                            Log.d(ContentValues.TAG, "GET Productos:${productos}" )
                            pedido.productos.forEach { elementoCarrito ->
                                Log.d(ContentValues.TAG, "GET Producto:${elementoCarrito}" )
                                productos.forEach { producto ->
                                    if (elementoCarrito.id == producto.id){
                                         productosArrayList = productos
                                        Log.d(ContentValues.TAG, "GET Cantidad:${elementoCarrito.cantidad}" )
                                    }
                                }
                            }
                            productos.forEach { productosRecyclerView.adapter = MyAdapter(productosArrayList)}
                        }
                    }
                }
            }*/

        Pedido.getPedidos().
        thenAccept { pedidos ->
            Log.d(ContentValues.TAG, "GET Pedido: $pedidos" )
            pedidos.forEach { i ->
                if(i.usuario == usuario.id){
                    Producto.getProductos()
                        .thenAccept { valor ->
                            Log.d(ContentValues.TAG, "Get Productos: $valor" )
                            productosArrayList = valor
                            valor.forEach { productosRecyclerView.adapter = MyAdapter(productosArrayList)}
                        }
                }
            }
        }
    }
    private inner class MyAdapter(private val productosList : List<Producto>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item,
                parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return productosList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val currentites = productosList[position]
            holder.nomProducto.text = currentites.nombre
            holder.costo.text = "₡ ${currentites.costo}"
            //holder.cantidad.text = cantidad
            val resourceId = resources.getIdentifier("cake", "drawable", packageName)
            holder.imageView.setImageResource(resourceId)
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val nomProducto : TextView = itemView.findViewById(R.id.tv_nombreProducto)
            val costo : TextView = itemView.findViewById(R.id.tv_precio)
            val imageView: ImageView = itemView.findViewById(R.id.img_producto)
            val cantidad : TextView = itemView.findViewById(R.id.tv_cantidad)
        }
    }
}