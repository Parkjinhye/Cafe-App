package com.example.cafeapp.ui.menu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.data.Menu

class MenuAdapter(private val menuList: List<Menu>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var onItemClickListener: ((Menu) -> Unit)? = null

    // 리스너 설정 메서드
    fun setOnItemClickListener(listener: (Menu) -> Unit) {
        onItemClickListener = listener
    }

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.text_menu_name)
        val priceTextView: TextView = view.findViewById(R.id.text_menu_price)
        val soldTextView: TextView = view.findViewById(R.id.text_menu_sold)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(menuList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]

        holder.nameTextView.text = menu.name
        holder.priceTextView.text = "Price: ${menu.price}"
        holder.soldTextView.text = "Sold: ${menu.sold}"
    }

    override fun getItemCount() = menuList.size

}