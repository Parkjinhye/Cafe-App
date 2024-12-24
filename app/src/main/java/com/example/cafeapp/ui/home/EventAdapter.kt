package com.example.cafeapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

data class EventItem(val imageRes: Int, val title: String, val description: String)

class EventAdapter(private val items: List<EventItem>)
    : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleView: TextView = view.findViewById(R.id.titleView)
        val descriptionView: TextView = view.findViewById(R.id.descriptionView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = items[position]
        holder.imageView.setImageResource(event.imageRes)
        holder.titleView.text = event.title
        holder.descriptionView.text = event.description
    }

    override fun getItemCount(): Int = items.size
}