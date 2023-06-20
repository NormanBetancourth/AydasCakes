package com.example.aydascakes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference = database.getReference("usuario")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        data class Persona(val clave: String, val correo: String, val nombre: String, val telefono: String)

        val persona = Persona("1234", "Jose@gmail.com", "Jose", "134234")

        val userId = auth.currentUser?.uid
        userId?.let {
            val personaRef = reference.child(it)
            personaRef.setValue(persona)
                .addOnSuccessListener {
                    // Datos guardados exitosamente
                    println("Funciona")


                }
                .addOnFailureListener {
                    // Error al guardar los datos
                    println("Error")
                }
        }

    }
}