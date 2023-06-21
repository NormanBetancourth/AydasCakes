package com.example.aydascakes.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun guardarObjeto(key: String, objeto: Any) {
        val objetoString = gson.toJson(objeto)
        sharedPreferences.edit().putString(key, objetoString).apply()
    }

    fun obtenerObjeto(key: String, clase: Class<*>): Any? {
        val objetoString = sharedPreferences.getString(key, null)
        return gson.fromJson(objetoString, clase)
    }

    fun eliminarObjeto(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}