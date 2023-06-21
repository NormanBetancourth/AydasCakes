package com.example.aydascakes.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.CompletableFuture

class Pedido(val id : String,
             val usuario: String,
             val fecha: String,
             val productos: List<String>)
{



    override fun toString(): String {
        return "Pedido [id=$id, usuario=${usuario}, fecha=${fecha}, productos=${productos}]"
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val CollectionPedido = db.collection("pedido")


        fun postPedido(usuario: String, fecha: Date, productos: List<String>): CompletableFuture<Pedido> {
            val completableFuture = CompletableFuture<Pedido>()
            val user: MutableMap<String, Any> = HashMap()
            user["usuario"] = usuario
            user["fecha"] = fecha.toString()
            user["productos"] = productos

            // Add a new document with a generated ID
            CollectionPedido
                .add(user)
                .addOnSuccessListener { documentReference ->
                    completableFuture.complete(Pedido(documentReference.id, usuario, fecha.toString(), productos))
                }
                .addOnFailureListener {
                        e -> Log.w(ContentValues.TAG, "Error adding document", e)
                        completableFuture.complete(null)
                }

            return completableFuture
        }



        fun getPedidos(): CompletableFuture<List<Pedido>>{
            val completableFuture = CompletableFuture<List<Pedido>>()

            CollectionPedido.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val usr = ArrayList<Pedido>()
                        for (document in task.result) {
                            usr.add(Pedido(document.id,
                                document.get("usuario") as String,
                                document.get("fecha") as String,
                                document.get("productos") as List<String>)
                            )
                        }
                        completableFuture.complete(usr)
                    }
                }

            return completableFuture
        }
    }

}