package com.example.aydascakes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.NavUtils
import com.example.aydascakes.model.*
import com.example.aydascakes.service.SessionManager
import java.util.*

class Factura : AppCompatActivity() {
    private lateinit var carrito: List<ElementoCarrito>
    private lateinit var sessionManager: SessionManager
    private lateinit var tvFactura: TextView
    private lateinit var btVolvIni: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facturas)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)

        tvFactura = findViewById(R.id.tvFactura)

        carrito = (sessionManager.obtenerObjeto("carrito", ElementoCarritoWrapper::class.java) as ElementoCarritoWrapper).carrito



        sessionManager.eliminarObjeto("carrito")



        val usuario = sessionManager.obtenerObjeto("usuario", Usuario::class.java) as Usuario
        Pedido.postPedido(usuario.id, Date(), carrito).
        thenAccept { pedido ->
            Producto.getProductos()
                .thenAccept{
                    productos ->

                    var content = "Pedido \n \tUsuario: ${usuario.nombre} \n \tCorreo: ${usuario.correo} \n\n \tFecha: ${pedido.fecha}\n\n"
                    var total = 0.0

                    pedido.productos.forEach { elementoCarrito ->
                        productos.forEach { producto ->
                            if (elementoCarrito.id == producto.id){
                                content += "\t- Producto: ${producto.nombre} Precio: ${producto.costo} x ${elementoCarrito.cantidad}\n\n"
                                total += producto.costo.toDouble() * elementoCarrito.cantidad
                            }
                        }
                    }

                    content += "Total: ${total}$"

                    tvFactura.text = content



                }

        }


        btVolvIni = findViewById(R.id.btVolvIni)
        btVolvIni.setOnClickListener {
            NavUtils.navigateUpFromSameTask(this)
            finishActivity(0)
        }



    }







}