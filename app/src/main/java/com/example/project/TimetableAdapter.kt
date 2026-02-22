package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimetableAdapter(private val items: List<String>) :
    RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    inner class TimetableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textItem: TextView = view.findViewById(R.id.textItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timetable, parent, false)
        return TimetableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        holder.textItem.text = items[position]
    }

    override fun getItemCount() = items.size
}
