package com.example.aydascakes.utilities

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecorator(espaciado: Int) : RecyclerView.ItemDecoration() {
    private val espaciado = espaciado

    override fun getItemOffsets(outRect : Rect, view : View, parent: RecyclerView, state : RecyclerView.State) {
        outRect.bottom = espaciado
        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = espaciado
    }
}