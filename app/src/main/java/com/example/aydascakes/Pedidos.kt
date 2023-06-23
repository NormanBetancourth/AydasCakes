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
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.ElementoCarrito
import com.example.aydascakes.model.Pedido
import com.example.aydascakes.model.Producto
import com.example.aydascakes.model.Usuario
import com.example.aydascakes.service.SessionManager
import org.w3c.dom.Text
import kotlin.math.cos
import kotlin.streams.toList

class Pedidos : AppCompatActivity(){

    lateinit var pedidosRecyclerView : RecyclerView
    lateinit var productosArrayList : List<Pedido>
    lateinit var btnRegresar : ImageButton
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedidos)

        //Iinicializando views

        sessionManager = SessionManager(this)
        pedidosRecyclerView = findViewById(R.id.pedidosLista)
        btnRegresar = findViewById(R.id.btn_regresar)
        pedidosRecyclerView.layoutManager = LinearLayoutManager(this)

        pedidosRecyclerView.setHasFixedSize(true)

        productosArrayList = listOf<Pedido>()
        getPedidosData()

        //Cambia a pantalla de inicio
        btnRegresar.setOnClickListener {
            NavUtils.navigateUpFromSameTask(this)
            finishActivity(0)
        }
    }
    //Obtiene los pedidos del usuario logueado
    private fun getPedidosData(){
        val usuario = sessionManager.obtenerObjeto("usuario", Usuario::class.java) as Usuario

        Pedido.getPedidos().thenAccept { listaPedidos ->
            productosArrayList = listaPedidos.filter { pedido ->
                pedido.usuario == usuario.id
            }
            pedidosRecyclerView.adapter = PedidoAdapter(productosArrayList)
        }
    }
    private inner class PedidoAdapter(private val productosList : List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item,
                parent, false)
            return PedidoViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return productosList.size
        }

        override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
            val pedidoActual = productosList[position]
            holder.bind(pedidoActual)
        }

        inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fechaPedido : TextView = itemView.findViewById(R.id.tvFechaPedido)
            val costoTotal : TextView = itemView.findViewById(R.id.tvCostoItemPedido)
            val cantidad : TextView = itemView.findViewById(R.id.tvCantidad)

            fun bind(pedido : Pedido) {

                val idsProductos = pedido.productos.map { prd -> prd.id }

                Producto.getProductos().thenAccept { productosList ->
                    fechaPedido.text = pedido.fecha

                    val productosFiltrados = productosList.filter {
                        producto -> idsProductos.contains(producto.id)
                    }
                    var costoTotalProducto : Long = 0L
                    var contadorProductos = 0

                    productosFiltrados.forEach { producto ->
                        costoTotalProducto = costoTotalProducto + producto.costo
                        contadorProductos++
                    }
                    costoTotal.text = costoTotal.toString()
                    cantidad.text = contadorProductos.toString()
                }
            }
        }

    }
}