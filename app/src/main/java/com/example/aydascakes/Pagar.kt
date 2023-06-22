package com.example.aydascakes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.ElementoCarrito
import com.example.aydascakes.model.Producto
import com.example.aydascakes.service.SessionManager


class Pagar : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayout: LinearLayout
    private lateinit var tvproduct_name: TextView
    private lateinit var tvproduct_price: TextView
    private lateinit var tvcantidad: TextView
    private lateinit var total: TextView
    private lateinit var btnPagar: Button
    private lateinit var btnRegresar: Button
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button
    private lateinit var adapter: ElementosAdapter
    private lateinit var sessionManager: SessionManager
    private var carrito: List<ElementoCarrito> = listOf(
        ElementoCarrito("1MpR9cB82EsHnj76HGmQ", 2), ElementoCarrito( "wLqvApHQ9DHUPyvOpZuC", 1)
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagar)

        sessionManager = SessionManager(this)

        Producto.getProductos().thenAccept{ productos ->

            val productosDelCarrito = ArrayList<Producto>()

            carrito.forEach { car ->
                productos.forEach { producto ->
                    if (car.id == producto.id){
                        productosDelCarrito.add(producto)
                    }
                }
            }

            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = ElementosAdapter(productosDelCarrito)
            recyclerView.adapter = adapter

            linearLayout =  findViewById(R.id.botoneraPagar)
            linearLayout.visibility = View.INVISIBLE



            //TODO add funcionalidad
            btnPagar = findViewById(R.id.btnPagarP)
            btnRegresar = findViewById(R.id.btnCancelarP)

            actualizarTotal()
        }




    }


    private inner class ElementosAdapter(private val elementos: List<Producto>) :
        RecyclerView.Adapter<ElementosAdapter.ElementoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementoViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pagar, parent, false)
            return ElementoViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ElementoViewHolder, position: Int) {
            val elemento = elementos[position]

            holder.bind(elemento)
        }

        override fun getItemCount(): Int {
            return elementos.size
        }

        inner class ElementoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvNombre: TextView = itemView.findViewById(R.id.nombre)
            private val tvCosto: TextView = itemView.findViewById(R.id.costo)
            private val imageView: ImageView = itemView.findViewById(R.id.foto_producto)


            @SuppressLint("DiscouragedApi")
            fun bind(elemento: Producto) {
                tvNombre.text = elemento.nombre
                tvCosto.text = "Costo: ${elemento.costo}"
                //Nota la img no debe tener extension
                //TODO: ajustar imagen con nombre de producto

                tvNombre.setTag(elemento.id)

                tvNombre.setOnClickListener {
                    CargarBotonera( tvNombre.getTag() as String )
                }


                val resourceId = resources.getIdentifier("cake", "drawable", packageName)
                imageView.setImageResource(resourceId)
            }
        }
    }

    private fun CargarBotonera(id: String) {


        Producto.getProductos().thenAccept{ productos ->

            sessionManager.guardarObjeto("idProductoBotonera", id)

            linearLayout =  findViewById(R.id.botoneraPagar)
            tvproduct_name = findViewById(R.id.product_name)
            tvproduct_price =  findViewById(R.id.product_price)
            tvcantidad =  findViewById(R.id.cantidad)

            linearLayout.visibility = View.VISIBLE

            val res = productos.find { e -> e.id == id }
            val rescarrito = carrito.find { e -> e.id == id }

            //setear textos
            tvproduct_name.text = res?.nombre ?: ""
            tvproduct_price.text = res?.costo ?: ""
            tvcantidad.text = (rescarrito?.cantidad ?: "").toString()



            btnRemove = findViewById(R.id.btnRemove)
            btnAdd =  findViewById(R.id.btnAdd)



            //TODO: Guardar carrito en el session

            btnAdd.setOnClickListener {
                tvcantidad =  findViewById(R.id.cantidad)
                val idAction = sessionManager.obtenerObjeto("idProductoBotonera", String::class.java) as? String
                val rescarritoAction = carrito.find { e -> e.id == idAction }

                carrito = carrito.map { carr ->
                    if (carr.id == idAction) ElementoCarrito(carr.id, carr.cantidad+1)  else carr
                }

                tvcantidad.text = (((rescarritoAction?.cantidad?.plus(1)) ?: "")).toString()

                actualizarTotal()



            }


            btnRemove.setOnClickListener {

                tvcantidad =  findViewById(R.id.cantidad)
                val idAction = sessionManager.obtenerObjeto("idProductoBotonera", String::class.java) as? String
                val rescarritoAction = carrito.find { e -> e.id == idAction }

                if (rescarritoAction != null) {
                    if (rescarritoAction.cantidad >= 1){
                        carrito = carrito.map { carr ->
                            if (carr.id == idAction) ElementoCarrito(carr.id, carr.cantidad-1)  else carr
                        }

                        tvcantidad.text = (((rescarritoAction?.cantidad?.minus(1)) ?: "")).toString()

                        actualizarTotal()
                    }
                }

            }





        }


    }

    private fun actualizarTotal() {


        Producto.getProductos().thenAccept { productos ->
            total = findViewById(R.id.total)
            total.text = carrito.map { carr ->
                (carr.cantidad *(productos.find { producto -> producto.id == carr.id }!!.costo).toDouble())
            }
                .reduce{ acc, curr -> acc + curr}
                .toString()
        }




    }

}

