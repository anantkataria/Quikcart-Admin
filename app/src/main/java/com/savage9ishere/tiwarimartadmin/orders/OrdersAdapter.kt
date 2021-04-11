package com.savage9ishere.tiwarimartadmin.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimartadmin.databinding.OrderItemBinding

class OrdersAdapter(private val onClick : (OrderItem) -> Unit) : ListAdapter<OrderItem, OrdersAdapter.ViewHolder> (OrdersDiffCallback()) {
    class ViewHolder private constructor(val binding : OrderItemBinding, onClick: (OrderItem) -> Unit): RecyclerView.ViewHolder(binding.root) {
        private val nameText : TextView = binding.nameTextView
        private val itemNamesText : TextView = binding.itemNamesTextView
        private val statusText : TextView = binding.statusTextView
        private val phoneNumberText : TextView = binding.phoneNumberTextView
        private val addressText : TextView = binding.addressTextView

        private var currentItem : OrderItem? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item : OrderItem) {
            currentItem = item

            nameText.text = item.address.fullName

            var s = ""
            for(itm in item.listItems){
                s += itm.name + "\n"
            }
            itemNamesText.text = s

            statusText.text = item.status

            phoneNumberText.text = item.authPhone

            val address = item.address.areaColonyStreet + " " + item.address.townCity + ", " + item.address.state + ", " + item.address.pinCode
            addressText.text = address
        }

        companion object {
            fun from (parent : ViewGroup, onClick: (OrderItem) -> Unit): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrderItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class OrdersDiffCallback : DiffUtil.ItemCallback<OrderItem>(){
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.orderKey == newItem.orderKey
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }

}
