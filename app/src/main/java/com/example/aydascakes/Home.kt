package com.example.aydascakes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.ElementoCarrito
import com.example.aydascakes.model.ElementoCarritoWrapper
import com.example.aydascakes.model.Producto
import com.example.aydascakes.service.SessionManager
import com.example.aydascakes.utilities.CustomItemDecorator
import java.util.stream.Stream
import kotlin.streams.toList

class Home : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var editPrecioMax : EditText

    private lateinit var editPrecioMin : EditText

    private lateinit var editTextoBusqueda : EditText

    private lateinit var adapter : ProductosAdapter

    private lateinit var listaProductos : List<Producto>

    private lateinit var session : SessionManager

    private lateinit var carrito : List<ElementoCarrito>

    private var carritoInicializado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        editPrecioMax = findViewById(R.id.maxValue)
        editPrecioMin = findViewById(R.id.minValue)
        editTextoBusqueda = findViewById(R.id.textoBusqueda)

        session = SessionManager(this)
        if(carritoInicializado)
            carrito = (session.obtenerObjeto("carrito", ElementoCarritoWrapper::class.java) as ElementoCarritoWrapper).carrito
        else {
            carrito = ArrayList()
            carritoInicializado = true
            session.guardarObjeto("carrito", ElementoCarritoWrapper(carrito))
        }

        inicializarRecycler()

        editTextoBusqueda.addTextChangedListener {filtro ->
            if(filtro.toString() != "")
                filtrarProductos()
        }

        editPrecioMax.addTextChangedListener { filtro ->
            if(filtro.toString() != "")
                filtrarProductos()
        }

        editPrecioMin.addTextChangedListener { filtro ->
            if(filtro.toString() != "")
                filtrarProductos()
        }

        // Finalmente se configura el boton para ejecutar la busqueda cada vez que sea presionado
        findViewById<Button>(R.id.botonBusqueda).setOnClickListener {
            filtrarProductos()
        }

        editTextoBusqueda.addTextChangedListener { texto ->
            if(texto.toString() != "") {
                filtrarProductos()
            }
        }
    }

    private fun filtrarProductos() {
        val listaFiltrada = Filtrador(listaProductos.stream())
            .conMaximo(editPrecioMax.text.toString().toLongOrNull())
            .conMinimo(editPrecioMin.text.toString().toLongOrNull())
            .conFiltroNombre(editTextoBusqueda.text.toString())
            .build()
        adapter.actualizarProductos(listaFiltrada)
    }

    private fun inicializarRecycler() {
        recyclerView = findViewById(R.id.productosRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(CustomItemDecorator(25))

        Producto.getProductos().thenAccept { lista ->
            listaProductos = lista.toMutableList()
            adapter = ProductosAdapter(listaProductos) { producto -> onItemSelected(producto) }
            recyclerView.adapter = adapter
        }
    }
    private fun onItemSelected(producto : Producto) {
        session.guardarObjeto("productoSeleccionado", producto)
        intent = Intent(this, DetalleProducto::class.java)
        startActivity(intent)
    }


    private inner class Filtrador(var listaAFiltrar: Stream<Producto>) {

        fun conFiltroNombre(nombre : String) : Filtrador {
            if(nombre != "") {
                listaAFiltrar = listaAFiltrar.filter { producto ->
                    producto.nombre.contains(nombre) || producto.descripcion.contains(nombre)

                }
            }
            return this
        }

        fun conMinimo(minimo : Long?) : Filtrador {
            if(minimo != null) {
                listaAFiltrar = listaAFiltrar.filter { producto ->
                    producto.costo >= minimo
                }
            }
            return this
        }

        fun conMaximo(maximo : Long?) : Filtrador {
            if(maximo != null) {
                listaAFiltrar = listaAFiltrar.filter { producto ->
                    producto.costo <= maximo
                }
            }
            return this
        }

        fun build() : List<Producto> { return this.listaAFiltrar.toList() }
    }

    private inner class ProductosAdapter(var lista : List<Producto>, val onClickListener: (Producto) -> Unit) :
        RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_producto, parent, false)

            return ProductoViewHolder(view)
        }

        override fun getItemCount() : Int { return lista.size }

        override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
            holder.bind(lista[position], onClickListener)
        }

        fun actualizarProductos(productos : List<Producto>) {
            this.lista = productos
            notifyDataSetChanged()
        }

        inner class ProductoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            private val tvCosto = itemView.findViewById<TextView>(R.id.precioProducto)
            private val tvNombre = itemView.findViewById<TextView>(R.id.nombreProducto)
            private val tvDescripcion = itemView.findViewById<TextView>(R.id.descripcionProducto)
            private val image = itemView.findViewById<ImageView>(R.id.imagenProducto)

            @SuppressLint("DiscouragedApi")
            fun bind(producto: Producto, onClickListener: (Producto) -> Unit) {
                tvCosto.text = producto.costo.toString()
                tvNombre.text = producto.nombre
                tvNombre.tag = producto.id
                tvDescripcion.text = producto.descripcion

                val idImagen = resources.getIdentifier(producto.img, "drawable", packageName)
                image.setImageResource(idImagen)

                itemView.setOnClickListener { onClickListener(producto) }
            }

        }
    }
}

