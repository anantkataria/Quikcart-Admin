package com.savage9ishere.tiwarimartadmin.orders.particular_order

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimartadmin.databinding.ParticularOrderItemBinding
import com.savage9ishere.tiwarimartadmin.orders.CartItems

class ParticularOrderAdapter(): ListAdapter<CartItems, ParticularOrderAdapter.ViewHolder>(FinalBillItemDiffCallback()) {
    class ViewHolder private constructor(val binding: ParticularOrderItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val itemImage : ImageView = binding.productImageView
        private val itemName : TextView = binding.productNameTextView
        private val itemPrice : TextView = binding.priceTextView
        private val itemOriginalPrice : TextView = binding.originalPriceTextView
        private val saveAmountText : TextView = binding.saveAmountTextView
        private val deliveryTimeApproxText : TextView = binding.deliveryTimeApproximateTextView

        private var currentItem : CartItems? = null

        fun bind(item: CartItems){
            currentItem = item

            val itemNameStr = item.name
            itemName.text = itemNameStr

            binding.executePendingBindings()

            val price = "₹" + item.price
            itemPrice.text = price

            val originalPrice = "₹" + item.priceOriginal
            itemOriginalPrice.text = originalPrice

            val saved = item.priceOriginal.toInt() - item.price.toInt()

            val discount = saved * 100 / item.priceOriginal.toInt()

            val save = "Save ₹$saved($discount%)"
            saveAmountText.text = save

            val delivery = "Delivery in " + item.deliveryDuration
            deliveryTimeApproxText.text = delivery

            Glide.with(itemImage.context)
                .load(item.photoUrl.toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(
                parent: ViewGroup
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ParticularOrderItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class FinalBillItemDiffCallback : DiffUtil.ItemCallback<CartItems>(){
    override fun areItemsTheSame(oldItem: CartItems, newItem: CartItems): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: CartItems, newItem: CartItems): Boolean {
        return oldItem == newItem
    }

}
