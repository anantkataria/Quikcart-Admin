package com.savage9ishere.tiwarimartadmin.categories

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
import com.savage9ishere.tiwarimartadmin.databinding.CategoryListItemBinding

private var onLongClicked = false
private val selectedCategories = arrayListOf<Category>()

//class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
class CategoriesAdapter(
    private val onClick: (Category) -> Unit,
    private val onLongClick: () -> Unit
) :
    ListAdapter<Category, CategoriesAdapter.ViewHolder>(CategoriesDiffCallback()) {

//    var data = listOf<Category?>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    class ViewHolder private constructor(
        val binding: CategoryListItemBinding,
        val onClick: (Category) -> Unit,
        val onLongClick: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val categoryImage: ImageView = binding.categoryImage
        private val categoryName: TextView = binding.categoryNameText
        private val checkBox: CheckBox = binding.checkBox
        private var currentCategory: Category? = null

        init {
            binding.root.setOnClickListener {
                currentCategory?.let{
                    if (onLongClicked) {
                        if (!checkBox.isChecked) {
                            checkBox.isChecked = true
                            checkBox.visibility = View.VISIBLE
                            selectedCategories.add(currentCategory!!)
                        } else {
                            checkBox.isChecked = false
                            checkBox.visibility = View.GONE
                            selectedCategories.remove(currentCategory!!)
                        }
                    } else {
                        onClick(it)
                    }
                }
            }
            binding.root.setOnLongClickListener {
                if (!onLongClicked) {
                    onLongClick()
                    checkBox.isChecked = true
                    checkBox.visibility = View.VISIBLE
                    onLongClicked = true
                    selectedCategories.add(currentCategory!!)
                    true
                } else {
                    false
                }
            }

        }

        fun bind(item: Category) {
            currentCategory = item
//            categoryName.text = item.name
            Glide.with(categoryImage.context)
                .load(item.uri.toUri().buildUpon().scheme("https").build())
                .into(categoryImage)

            binding.checkBox.visibility = View.GONE

            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (Category) -> Unit,
                onLongClick: () -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //val view = layoutInflater.inflate(R.layout.category_list_item, parent, false)
                val binding = CategoryListItemBinding.inflate(layoutInflater, parent, false)

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
        selectedCategories.clear()
    }

    fun resetParamsForActionMode(){
        onLongClicked = false
        selectedCategories.clear()
    }

    fun getSelectedItemsList() : ArrayList<Category> {
        return selectedCategories
    }

//    override fun getItemCount(): Int {
//        return data.size
//    }

}

class CategoriesDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}