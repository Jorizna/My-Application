package com.jorge.colorapplication

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jorge.colorapplication.R

class BarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardBar = view.findViewById<CardView>(R.id.cardBar)
    val tvLabel = view.findViewById<TextView>(R.id.tvLabel)

    fun render(colorCard: ColorCard) {
        tvLabel.text = colorCard.name + " (" + colorCard.opacity + "%)"
        val colorRes = colorCard.color.getRes(colorCard.opacity)
        cardBar.setCardBackgroundColor(ContextCompat.getColor(cardBar.context, colorRes))
    }
}