package de.andreaseckner.android.homestock.ui.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.andreaseckner.android.homestock.data.StockItem
import de.andreaseckner.android.homestock.data.getFormattedPrice
import de.andreaseckner.android.homestock.databinding.StockItemListItemBinding

class StockItemListAdapter(private val onItemClicked: (StockItem) -> Unit) :
    ListAdapter<StockItem, StockItemListAdapter.StockItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StockItemListAdapter.StockItemViewHolder {
        return StockItemViewHolder(
            StockItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: StockItemListAdapter.StockItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class StockItemViewHolder(private var binding: StockItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockItem) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemQuantity.text = item.quantityInStock.toString()
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<StockItem>() {
            override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }
}