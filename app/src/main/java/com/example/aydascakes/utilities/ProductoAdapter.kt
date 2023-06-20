package com.example.aydascakes.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.Producto
import com.example.aydascakes.R

class ProductoAdapter(val list: List<Producto>, val context : Context) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imagen : ImageView = itemView.findViewById(R.id.imagenProducto)
        val nombreProducto : TextView = itemView.findViewById(R.id.nombreProducto)
        val descripcionProducto : TextView = itemView.findViewById(R.id.descripcionProducto)
        val precioProducto : TextView = itemView.findViewById(R.id.precioProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_producto, parent,
            false)

        return ProductoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val currentItem = list[position]
        val id = context.resources.getIdentifier(currentItem.img, "drawable", context.packageName)
        holder.imagen.setImageResource(id)
        holder.nombreProducto.text = currentItem.nombre
        holder.descripcionProducto.text = currentItem.descripcion
        holder.precioProducto.text = currentItem.costo.toString()
    }

}