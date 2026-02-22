package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val events: List<EventModel>,
    private val onItemClick: (EventModel) -> Unit,
    private val onItemLongClick: (EventModel) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvEventTitle)
        val host = view.findViewById<TextView>(R.id.tvEventHost)
        val date = view.findViewById<TextView>(R.id.tvEventDate)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(events[position])
                }
            }
            view.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(events[position])
                    true
                } else false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val e = events[position]
        holder.title.text = e.title
        holder.host.text = "Hosted by ${e.host}"
        holder.date.text = "${e.date} • ${e.time}"
    }

    override fun getItemCount() = events.size
}
