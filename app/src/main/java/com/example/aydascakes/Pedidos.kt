package com.example.aydascakes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

class Pedidos : AppCompatActivity(){

    lateinit var pedidosRecyclerView : RecyclerView
    lateinit var pedidos : List<Pedido>
    lateinit var copiaProductos : List<Producto>
    lateinit var btnRegresar : ImageButton
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: PedidoAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedidos)

        //Iinicializando views

        sessionManager = SessionManager(this)
        pedidosRecyclerView = findViewById(R.id.pedidosLista)
        btnRegresar = findViewById(R.id.btn_regresar)
        pedidosRecyclerView.layoutManager = LinearLayoutManager(this)

        pedidosRecyclerView.setHasFixedSize(true)
        pedidos = ArrayList()
        adapter = PedidoAdapter(pedidos)
        pedidosRecyclerView.adapter = adapter

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

        Producto.getProductos().thenAccept { listaProductos -> copiaProductos = listaProductos }

        Pedido.getPedidos().thenAccept { listaPedidos ->
            pedidos = listaPedidos.filter { pedido ->
                pedido.usuario == usuario.id
            }
            adapter.actualizarLista(pedidos)
        }


    }
    private inner class PedidoAdapter(private var listaPedidos : List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item,
                parent, false)
            return PedidoViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return listaPedidos.size
        }

        override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
            val pedidoActual = listaPedidos[position]
            holder.bind(pedidoActual)
        }

        fun actualizarLista(nuevaLista : List<Pedido>) {
            this.listaPedidos = nuevaLista
            this.notifyDataSetChanged()
        }

        inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fechaPedido : TextView = itemView.findViewById(R.id.tvFechaPedido)
            val costoTotal : TextView = itemView.findViewById(R.id.tvCostoItemPedido)
            val cantidad : TextView = itemView.findViewById(R.id.tvCantidad)

            fun bind(pedido : Pedido) {
                val textoCargando = "Cargando..."
                costoTotal.text = textoCargando
                cantidad.text = textoCargando
                fechaPedido.text = textoCargando

                var costoTotalPedido = 0L
                var contadorProductos = 0
                try {
                    for(elemento in pedido.productos) {
                        for(prod in copiaProductos) {
                            if(elemento.id == prod.id) {
                                costoTotalPedido += prod.costo
                                contadorProductos++
                            }
                        }
                    }
                } catch (ex : Exception) {
                    Log.w(Log.DEBUG.toString(), "Error desconocido=$ex", ex)
                }
                pedido.fecha.also { fechaPedido.text = it}
                "₡ $costoTotalPedido".also { costoTotal.text = it }
                contadorProductos.toString().also { cantidad.text = it }
            }
        }

    }
}