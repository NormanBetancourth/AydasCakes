package com.example.aydascakes

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aydascakes.model.Pedido
import com.example.aydascakes.model.Usuario

class ModificarDatos : AppCompatActivity(){

    lateinit var nombre: EditText
    lateinit var correo: EditText
    lateinit var telefono: EditText
    lateinit var clave: EditText
    lateinit var btnModificar : ImageButton
    lateinit var btnRegresar : ImageButton
    lateinit var idUsuario : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_datos)

        nombre = findViewById(R.id.input_nom)
        correo = findViewById(R.id.input_correo)
        telefono = findViewById(R.id.input_tel)
        clave = findViewById(R.id.input_clave)
        btnModificar = findViewById(R.id.btn_modificar)
        btnRegresar = findViewById(R.id.btn_Inicio)


        //Muestra la info del usuario logueado
        Usuario.getUsuarios()
            .thenAccept { listaUsuarios ->
                if (listaUsuarios.isNotEmpty()) {
                    val usuario = listaUsuarios[0]
                    idUsuario = usuario.id
                    nombre.setText(usuario.nombre)
                    correo.setText(usuario.correo)
                    telefono.setText(usuario.telefono)
                    clave.setText(usuario.clave)
                } else {
                    Log.d(ContentValues.TAG, "La lista de usuarios está vacía.")
                }
            }

        //Modifica la información del usuario
        btnModificar.setOnClickListener {
            val nombreUsuario = nombre.text.toString()
            val correoUsuario = correo.text.toString()
            val telefonoUsuario = telefono.text.toString()
            val claveUsuario = clave.text.toString()

            val usuario = Usuario(idUsuario,nombreUsuario, correoUsuario, claveUsuario, telefonoUsuario)

            Usuario.putUsuario(usuario)
                .thenAccept { valor ->
                    Log.d(ContentValues.TAG, "PUT Usuario: $valor" )
                }
            Toast.makeText(applicationContext, "Modificado correctamente", Toast.LENGTH_SHORT).show()
        }

        //Cambia a pantalla de inicio
        btnRegresar.setOnClickListener {
            val intent: Intent = Intent(this,Pedidos::class.java)
            startActivity(intent)
        }


    }

}