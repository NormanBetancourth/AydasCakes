package com.example.aydascakes

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.aydascakes.model.Usuario
import com.example.aydascakes.service.SessionManager

class ModificarDatos : AppCompatActivity(){

    lateinit var nombre: EditText
    lateinit var correo: EditText
    lateinit var telefono: EditText
    lateinit var clave: EditText
    lateinit var btnModificar : ImageButton
    lateinit var btnRegresar : ImageButton
    lateinit var btnVerPedidos : Button
    lateinit var idUsuario : String
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_datos)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)

        nombre = findViewById(R.id.input_nom)
        correo = findViewById(R.id.input_correo)
        telefono = findViewById(R.id.input_tel)
        clave = findViewById(R.id.input_clave)
        btnModificar = findViewById(R.id.btn_modificar)
        btnRegresar = findViewById(R.id.btn_Inicio)
        btnVerPedidos = findViewById(R.id.btnVerPedidos)


        //Muestra la info del usuario logueado
        val user = sessionManager.obtenerObjeto("usuario", Usuario::class.java) as Usuario
        Usuario.getUsuarios()
            .thenAccept { valor ->
                valor.forEach { usuario ->
                    if (usuario.id == user.id){
                        nombre.setText(usuario.nombre)
                        correo.setText(usuario.correo)
                        telefono.setText(usuario.telefono)
                        clave.setText(usuario.clave)
                        Log.d(ContentValues.TAG, "Get Usuario: $usuario" )
                    }
                }
            }

        //Modifica la información del usuario
        btnModificar.setOnClickListener {
            val nombreUsuario = nombre.text.toString()
            val correoUsuario = correo.text.toString()
            val telefonoUsuario = telefono.text.toString()
            val claveUsuario = clave.text.toString()

            Usuario.putUsuario(Usuario(user.id,nombreUsuario, correoUsuario, claveUsuario, telefonoUsuario))
                .thenAccept { valor ->
                    Log.d(ContentValues.TAG, "PUT Usuario: $valor" )
                }
            Toast.makeText(applicationContext, "Modificado correctamente", Toast.LENGTH_SHORT).show()
        }

        //Cambia a pantalla de inicio
        btnRegresar.setOnClickListener {
            NavUtils.navigateUpFromSameTask(this)
            finishActivity(0)
        }

        btnVerPedidos.setOnClickListener {
            intent = Intent(this, Pedidos::class.java)
            startActivity(intent)
        }

    }

}