package com.example.aydascakes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aydascakes.model.Producto

class MyAdapter(private val productosList : ArrayList<Producto>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


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
        holder.costo.text = currentites.costo.toString()
        //holder.cantidad.text = productosList.size.toString()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val nomProducto : TextView = itemView.findViewById(R.id.tv_nombreProducto)
        val costo : TextView = itemView.findViewById(R.id.tv_precio)
        val cantidad : TextView = itemView.findViewById(R.id.tv_cantidad)
    }
}