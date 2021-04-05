package com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimartadmin.databinding.NewSizeItemBinding


class OtherSizesAdapter(private val onRemoveClicked: (String) -> Unit) : ListAdapter<Pair<String, String>, OtherSizesAdapter.ViewHolder>(ItemDiffCallback()){

    class ViewHolder private constructor(val binding: NewSizeItemBinding, val onRemoveClicked: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private val sizeText : TextView = binding.sizeTextView
        private val priceText : TextView = binding.priceTextView
        private val removeImage : ImageView = binding.removeImage

        private var itemm : Pair<String, String>? = null

        init {
            removeImage.setOnClickListener {
                itemm?.let {
                    onRemoveClicked(itemm!!.first)
                    Log.e("27", "onRemoveclicked in otherdetailsadapter")
                }

            }
        }

        fun bind(item: Pair<String, String>){
            itemm = item

            sizeText.text = item.first
            priceText.text = item.second
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, onRemoveClicked: (String) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NewSizeItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onRemoveClicked)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, onRemoveClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class ItemDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem == newItem
    }

}
