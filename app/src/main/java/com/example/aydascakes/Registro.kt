package com.example.aydascakes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.aydascakes.model.Usuario

class Registro : AppCompatActivity() {
    private lateinit var correo: EditText
    private lateinit var nombre: EditText
    private lateinit var telefono: EditText
    private lateinit var contrasena: EditText
    private lateinit var confContrasena: EditText
    private lateinit var registrar: Button
    private lateinit var cancelar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        supportActionBar?.hide()


        correo = findViewById(R.id.etCorreo)
        nombre = findViewById(R.id.etNombre)
        telefono = findViewById(R.id.etTelefono)
        contrasena = findViewById(R.id.etContrasena)
        confContrasena = findViewById(R.id.etConfContra)

        registrar = findViewById(R.id.btRegistrar)
        cancelar = findViewById(R.id.btCancelar)

        registrar.setOnClickListener{
            if(correo.text.toString().isNotEmpty() && nombre.text.toString().isNotEmpty() && telefono.text.toString().isNotEmpty() && contrasena.text.toString().isNotEmpty() && confContrasena.text.toString().isNotEmpty()){

                if (contrasena.text.toString() == confContrasena.text.toString()){
                    Usuario.postUsuario(nombre.text.toString(), correo.text.toString(), contrasena.text.toString(), telefono.text.toString())
                        .thenAccept { _ ->
                            Toast.makeText(this, "Se ha agregado un nuevo usuario!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()

                        }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            }

        }

        cancelar.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }







    }
}