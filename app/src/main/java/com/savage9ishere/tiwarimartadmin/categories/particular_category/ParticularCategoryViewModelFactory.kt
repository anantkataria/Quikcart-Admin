package com.savage9ishere.tiwarimartadmin.categories.particular_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParticularCategoryViewModelFactory(
    private val categoryName : String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticularCategoryViewModel::class.java)) {
            return ParticularCategoryViewModel(categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}