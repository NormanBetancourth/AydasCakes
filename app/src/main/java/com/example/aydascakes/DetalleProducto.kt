package com.example.aydascakes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NavUtils
import com.example.aydascakes.model.ElementoCarrito
import com.example.aydascakes.model.ElementoCarritoWrapper
import com.example.aydascakes.model.Producto
import com.example.aydascakes.service.SessionManager


class DetalleProducto : AppCompatActivity() {

    private lateinit var tvPrecio : TextView
    private lateinit var tvNombre : TextView
    private lateinit var tvDescripcion : TextView
    private lateinit var imageView: ImageView
    private lateinit var btnRegresar : Button

    private lateinit var btnAgregarAlCarrito : Button
    private lateinit var btnMas : Button
    private lateinit var btnMenos : Button
    private lateinit var tvCantidadItems : TextView
    private var cantidadElementos = 1

    private lateinit var productoSeleccionado : Producto
    private lateinit var session : SessionManager
    private lateinit var carrito : List<ElementoCarrito>

    @SuppressLint("MissingInflatedId", "DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)
        supportActionBar?.hide()

        //Inicialiazando elementos del View
        tvPrecio = findViewById(R.id.tvPrecioDetalle)
        tvNombre = findViewById(R.id.tvNombreProducto)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        imageView = findViewById(R.id.imagenDetalleProducto)

        btnRegresar = findViewById(R.id.btnRegresar)
        btnAgregarAlCarrito = findViewById(R.id.btnAgregarCarrito)
        btnMas = findViewById(R.id.btnMas)
        btnMenos = findViewById(R.id.btnMenos)
        tvCantidadItems = findViewById(R.id.tvCantidadProducto)

        // Configuracion de la sesion para el carrito
        session = SessionManager(this)
        carrito = (session.obtenerObjeto("carrito", ElementoCarritoWrapper::class.java) as ElementoCarritoWrapper).carrito

        productoSeleccionado = session.obtenerObjeto("productoSeleccionado", Producto::class.java) as Producto


        // Se setean los valores del producto
        val idImagen = resources.getIdentifier(productoSeleccionado.img, "drawable", packageName)
        imageView.setImageResource(idImagen)

        tvPrecio.text = productoSeleccionado.costo.toString()
        tvNombre.text = productoSeleccionado.nombre
        tvDescripcion.text = productoSeleccionado.descripcion

        btnMas.setOnClickListener {
            tvCantidadItems.text = (++cantidadElementos).toString()
        }

        btnMenos.setOnClickListener {
            if(cantidadElementos > 0)
                tvCantidadItems.text = (--cantidadElementos).toString()
        }

        btnAgregarAlCarrito.setOnClickListener {
            val nuevaLista = ArrayList<ElementoCarrito>()

            // Se agregan los items anteriores
            nuevaLista.addAll(carrito)

            // Se agrega el nuevo item
            nuevaLista.add(ElementoCarrito(productoSeleccionado.id, cantidadElementos))

            //Se reeplaza la lista anterior
            session.guardarObjeto("carrito", ElementoCarritoWrapper(nuevaLista))
            NavUtils.navigateUpFromSameTask(this)
            finishActivity(0)
        }

        btnRegresar.setOnClickListener {
            NavUtils.navigateUpFromSameTask(this)
            finishActivity(0)
        }

    }
}