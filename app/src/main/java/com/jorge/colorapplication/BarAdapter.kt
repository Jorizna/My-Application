package com.jorge.colorapplication
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jorge.colorapplication.R

class BarAdapter(private val cards: List<ColorCard>) : RecyclerView.Adapter<BarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bar, parent, false)
        return BarViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        holder.render(cards[position])
    }

    override fun getItemCount() = cards.size
}