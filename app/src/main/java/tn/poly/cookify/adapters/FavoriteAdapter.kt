package tn.poly.cookify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.poly.cookify.R
import tn.poly.cookify.models.FavoriteRecipe

class FavoriteAdapter(
    private var items: List<FavoriteRecipe>,
    private val onDeleteClick: (FavoriteRecipe) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.favTitle)
        val image = view.findViewById<ImageView>(R.id.favImage)
        val removeButton = view.findViewById<Button>(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.image)

        holder.itemView.setOnLongClickListener {
            onDeleteClick(item)
            true
        }

        holder.removeButton.setOnClickListener {
            onDeleteClick(item)
        }
    }
}