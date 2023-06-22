package com.example.aydascakes.model

import android.annotation.SuppressLint
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.CompletableFuture

class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val costo: Long,
    val img: String
)
{
    override fun toString(): String {
        return "Producto [id=$id, nombre=$nombre, descripcion=$descripcion, costo=$costo, img=$img]"
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        val db = FirebaseFirestore.getInstance()
        val CollectionProducto = db.collection("producto")

        fun getProductos(): CompletableFuture<List<Producto>> {
            val completableFuture = CompletableFuture<List<Producto>>()

            CollectionProducto
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val prd = ArrayList<Producto>()
                        for (document in task.result) {
                            prd.add(Producto(document.id,
                                document.get("nombre") as String,
                                document.get("descripcion") as String,
                                document.get("costo") as Long,
                                document.get("img") as String))
                        }
                        completableFuture.complete(prd)
                    }
                }
            return completableFuture
        }

        fun filtrarProductos(filtro : Filter) : CompletableFuture<List<Producto>> {
            val completableFuture = CompletableFuture<List<Producto>>()

            CollectionProducto
                .where(filtro)
                .get()
                .addOnCompleteListener { task ->
                    val productos = ArrayList<Producto>()
                    for (documento in task.result) {
                        productos.add(Producto(
                            documento.id,
                            documento.get("nombre") as String,
                            documento.get("descripcion") as String,
                            documento.get("costo") as Long,
                            documento.get("img") as String)
                        )
                    }
                    completableFuture.complete(productos)
                }
            return completableFuture
        }
    }
}