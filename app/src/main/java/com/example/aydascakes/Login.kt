package com.example.aydascakes

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.aydascakes.model.Usuario
import com.example.aydascakes.service.SessionManager

class Login : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    private lateinit var correoEditText: EditText
    private lateinit var claveEditText: EditText
    private lateinit var btnIngresar: Button
    private lateinit var tvRegisHere: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        sessionManager = SessionManager(this)


        correoEditText = findViewById(R.id.editCorreo)
        claveEditText = findViewById(R.id.editClave)
        btnIngresar = findViewById(R.id.btIngresar)
        tvRegisHere = findViewById(R.id.tvRegisHere)

        btnIngresar.setOnClickListener{
            val correo = correoEditText.text.toString()
            val clave = claveEditText.text.toString()



            if (correo.isNotEmpty() && clave.isNotEmpty()){
                Usuario.login(correo, clave)
                    .thenAccept { usuario ->

                        if (usuario != null) {
                            Toast.makeText(this, "Inicio de sesión Exitoso", Toast.LENGTH_SHORT).show()

                            //TODO cambiar a home
                            sessionManager.guardarObjeto("usuario", usuario)
                            val intent = Intent(this, Home::class.java)
                            startActivity(intent)
                            finish()

                        }else{
                            Toast.makeText(this, "Credenciales Inválidas", Toast.LENGTH_SHORT).show()
                        }

                    }
            }else{
                Toast.makeText(this, "Correo y clave son requeridos", Toast.LENGTH_SHORT).show()
            }


        }

        tvRegisHere.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
            finish()
        }



    }
}