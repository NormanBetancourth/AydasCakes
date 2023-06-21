package com.example.aydascakes.model

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.concurrent.CompletableFuture


class Usuario(val id : String,
              val nombre: String,
              val correo: String,
              val clave: String,
              val telefono: String)
{

    override fun toString(): String {
        return "Usuario [id=$id, nombre=$nombre, correo=$correo, clave=$clave, telefono=$telefono]"
    }

    companion object{

        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val CollectionUsuario = db.collection("usuario")



        fun login(correo: String, clave: String): CompletableFuture<Usuario>{
            val completableFuture = CompletableFuture<Usuario>()

             CollectionUsuario.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            if (document.data.get("clave") == clave && document.data.get("correo") == correo ){
                                completableFuture.complete(
                                    Usuario(document.id,document.get("nombre") as String,
                                        document.get("correo") as String,
                                        document.get("clave") as String,
                                        document.get("telefono") as String)
                                )
                            }
                        }
                        completableFuture.complete(null)
                    }
                }

            return completableFuture
        }

        fun getUsuarios(): CompletableFuture<List<Usuario>>{
            val completableFuture = CompletableFuture<List<Usuario>>()

            CollectionUsuario.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var usr = ArrayList<Usuario>()
                        for (document in task.result) {
                            usr.add(Usuario(document.id, document.get("nombre") as String, document.get("correo") as String, document.get("clave") as String, document.get("telefono") as String))
                        }
                        completableFuture.complete(usr)
                    }
                }

            return completableFuture
        }


        fun postUsuario(nombre: String, correo: String, clave: String, telefono: String): CompletableFuture<Usuario>{
            val completableFuture = CompletableFuture<Usuario>()
            val user: MutableMap<String, Any> = HashMap()
            user["clave"] = clave
            user["correo"] = correo
            user["nombre"] = nombre
            user["telefono"] = telefono

            // Add a new document with a generated ID
            CollectionUsuario
                .add(user)
                .addOnSuccessListener { documentReference ->
                    completableFuture.complete(Usuario(documentReference.id, nombre, correo, clave, telefono))
                }
                .addOnFailureListener {
                        e -> Log.w(TAG, "Error adding document", e)
                        completableFuture.complete(null)
                }

            return completableFuture
        }

        fun putUsuario(usuario: Usuario): CompletableFuture<Usuario>{

            val completableFuture = CompletableFuture<Usuario>()
            val user: MutableMap<String, Any> = HashMap()
            user["clave"] = usuario.clave
            user["correo"] = usuario.correo
            user["nombre"] = usuario.nombre
            user["telefono"] = usuario.telefono

            // Add a new document with a generated ID
            CollectionUsuario.document(usuario.id)
                .update(user)
                .addOnSuccessListener { _ ->
                    completableFuture.complete(usuario)
                }
                .addOnFailureListener {
                        e -> Log.w(TAG, "Error adding document", e)
                    completableFuture.complete(null)
                }

            return completableFuture


        }


    }
}