package com.savage9ishere.tiwarimartadmin.categories.particular_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details.NewItemDetailsViewModel
import com.savage9ishere.tiwarimartadmin.databinding.ItemListItemBinding

private var onLongClicked = false
private var selectedItems = arrayListOf<NewItemDetailsViewModel.Item>()

class ItemsAdapter(
    private val onClick: (NewItemDetailsViewModel.Item) -> Unit,
    private val onLongClick: () -> Unit
) :
    ListAdapter<NewItemDetailsViewModel.Item, ItemsAdapter.ViewHolder>(ItemsDiffCallback()) {

    class ViewHolder private constructor(
        val binding: ItemListItemBinding,
        val onClick: (NewItemDetailsViewModel.Item) -> Unit,
        val onLongClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val itemImage: ImageView = binding.productImageView
        private val itemName: TextView = binding.productNameTextView
        private val ratingCountText: TextView = binding.ratingCountTextView
        private val itemPrice: TextView = binding.priceTextView
        private val itemOriginalPrice: TextView = binding.originalPriceTextView
        private val saveAmountText: TextView = binding.saveAmountTextView
        private val deliveryTimeApproxText: TextView = binding.deliveryTimeApproximateTextView
        private val checkBox : CheckBox = binding.checkBox

        private var currentItem: NewItemDetailsViewModel.Item? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    if(onLongClicked){
                        if(!checkBox.isChecked) {
                            checkBox.isChecked = true
                            checkBox.visibility = View.VISIBLE
                            selectedItems.add(currentItem!!)
                        }
                        else {
                            checkBox.isChecked = false
                            checkBox.visibility = View.GONE
                            selectedItems.remove(currentItem!!)
                        }
                    }
                    else {
                        onClick(it)
                    }
                }
            }

            binding.root.setOnLongClickListener {
                if(!onLongClicked){
                    onLongClick()
                    checkBox.isChecked = true
                    checkBox.visibility = View.VISIBLE
                    onLongClicked = true
                    selectedItems.add(currentItem!!)
                    true
                }
                else {
                    false
                }
            }
        }

        fun bind(item: NewItemDetailsViewModel.Item) {
            currentItem = item
            val itemNameStr = item.name + " " + item.qty
            itemName.text = itemNameStr

            binding.checkBox.visibility = View.GONE
            binding.executePendingBindings()

            val originalPriceStr = "₹" + item.price
            itemOriginalPrice.text = originalPriceStr

            ratingCountText.text = item.peopleRatingCount.toString()

            val originalPrice = item.price.toInt()
            val discount = item.discount.toInt()
            val price = originalPrice - (originalPrice * discount / 100)
            itemPrice.text = "$price"

            val saveAmount =
                "Save ₹" + (originalPrice * discount / 100).toString() + " (${discount.toString()}%)"
            saveAmountText.text = saveAmount

            val str = "Delivery in " + item.deliveryDuration
            deliveryTimeApproxText.text = str

            Glide.with(itemImage.context)
                .load(item.photosUrl[0].toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (NewItemDetailsViewModel.Item) -> Unit,
                onLongClick: () -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClick, onLongClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun callDatasetChanged() {
        notifyDataSetChanged()
        onLongClicked = false
        selectedItems.clear()
    }

    fun resetParamsForActionMode() {
        onLongClicked = false
        selectedItems.clear()
    }

    fun getSelectedItemsList() : ArrayList<NewItemDetailsViewModel.Item> {
        return selectedItems
    }
}

class ItemsDiffCallback : DiffUtil.ItemCallback<NewItemDetailsViewModel.Item>() {
    override fun areItemsTheSame(
        oldItem: NewItemDetailsViewModel.Item,
        newItem: NewItemDetailsViewModel.Item
    ): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(
        oldItem: NewItemDetailsViewModel.Item,
        newItem: NewItemDetailsViewModel.Item
    ): Boolean {
        return oldItem == newItem
    }

}