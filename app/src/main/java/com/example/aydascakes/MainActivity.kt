package com.example.aydascakes

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aydascakes.model.Pedido
import com.example.aydascakes.model.Producto
import com.example.aydascakes.model.Usuario
import com.example.aydascakes.service.SessionManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)




        //USO DE FUNCIONALIDADES ASINCRONAS


        //------------------------------------------------Usuarios------------------------------------------------

        //OJO TENER EN CUENTA QUE ESTO ES ASINCRONO
        Usuario.login("Jose@gmail.com", "1234")
        .thenAccept { valor ->
            //Ojo retorna null
            Log.d(TAG, "Valor obtenido de Firebase: $valor" )
        }


        //obtener la coleccion de usuarios
        Usuario.getUsuarios()
        .thenAccept { valor ->
            Log.d(TAG, "Get Usuarios: $valor" )
        }

        //Guardar usuario en DB
        //Usuario.postUsuario("Juan", "Juan@hotmail", "666", "12345678")
        //.thenAccept { valor ->
            //Log.d(TAG, "Post Usuario: $valor" )
        //}

        //Actualizar usuario en DB
        Usuario.putUsuario(Usuario("1YJnzL8oqLFdukaLDs88","TEST", "TEST@hotmail", "666", "12345678"))
        .thenAccept { valor ->
            Log.d(TAG, "PUT Usuario: $valor" )
        }

        //------------------------------------------------Productos------------------------------------------------


        //Listar los productos
        Producto.getProductos()
        .thenAccept { valor ->
            Log.d(TAG, "Get Productos: $valor" )
            //valor.forEach { e -> e.costo }
        }


        //Fechas en kotlin
        Log.d(TAG, "Fecha: "+ Date("Tue Jun 20 07:25:26 GMT 2023").toString() )
        Log.d(TAG, "Fecha: "+ Date().toString() )

        //------------------------------------------------Pedidos------------------------------------------------


//        Pedido.postPedido("qni94Oe6WWsJ10URUHKH", Date(), listOf("e0kyvR9ehXR1Kx64rX7Z")).
//        thenAccept { valor ->
//            Log.d(TAG, "Post Pedido: $valor" )
//        }

        val id = "12"

        Pedido.getPedidos().
        thenAccept { valor ->
            Log.d(TAG, "GET Pedido: $valor" )

//            valor.forEach { i ->
//                if(i.usuario == id){
//
//                }
//            }
        }



        val usuarioGuardado = sessionManager.obtenerObjeto("usuario", Usuario::class.java) as? Usuario
        if (usuarioGuardado != null) {
            // Hacer algo con el objeto recuperado
            Log.d("MainActivity", "Nombre: ${usuarioGuardado.nombre}, Correo: ${usuarioGuardado.correo}")
        }else{
            Log.d("MainActivity", "Error no se pasa")

        }




    }
}