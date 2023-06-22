package com.example.aydascakes.model

class ElementoCarrito(val id: String, val cantidad: Int) {
    override fun toString(): String {
       return "Elemento [id=$id, cantidad=${cantidad}]"
    }
}
class ElementoCarritoWrapper(val carrito: List<ElementoCarrito>) {}