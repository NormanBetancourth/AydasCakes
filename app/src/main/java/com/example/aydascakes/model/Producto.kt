package com.example.aydascakes.model

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.CompletableFuture

class Producto(val id : String,
               val nombre: String,
               val descripcion: String,
               val costo: String,
               val img : String)
{
    override fun toString(): String {
        return "Producto [id=$id, nombre=$nombre, descripcion=$descripcion, costo=$costo, img=$img]"
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val CollectionProducto = db.collection("producto")

        fun getProductos(): CompletableFuture<List<Producto>> {
            val completableFuture = CompletableFuture<List<Producto>>()

            CollectionProducto.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val prd = ArrayList<Producto>()
                        for (document in task.result) {
                            prd.add(Producto(document.id,
                                document.get("nombre") as String,
                                document.get("descripcion") as String,
                                (document.get("costo") as Any).toString(),
                                document.get("img") as String))
                        }
                        completableFuture.complete(prd)
                    }
                }

            return completableFuture
        }

    }
}