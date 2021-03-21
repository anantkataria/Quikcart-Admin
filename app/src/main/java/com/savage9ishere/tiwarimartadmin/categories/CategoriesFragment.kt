package com.savage9ishere.tiwarimartadmin.categories

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.databinding.CategoriesFragmentBinding
import com.savage9ishere.tiwarimartadmin.util.OnActionItemClickListener
import com.savage9ishere.tiwarimartadmin.util.OnDestroyActionModeClickListener
import com.savage9ishere.tiwarimartadmin.util.PrimaryActionModeCallback

class CategoriesFragment : Fragment() {


    private lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.categories_fragment, container, false)
        val binding = CategoriesFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        val primaryActionModeCallback = PrimaryActionModeCallback()
        val adapter = CategoriesAdapter ({this.gridViewItemOnClick(it) }, {
            primaryActionModeCallback.startActionMode(requireView(), R.menu.action_mode_menu, "Delete Items")
        })
        binding.recyclerViewCategories.adapter = adapter
        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewCategories.layoutManager = manager

        primaryActionModeCallback.onActionItemClickListener = object : OnActionItemClickListener {
            override fun onActionItemClick(item: MenuItem) {
                when(item.itemId){
                    R.id.delete_item -> {
                        // and delete entry from realtime database
//                        Log.e("aaaa", "delete clicked")
                        viewModel.removeSelectedItems(adapter.getSelectedItemsList())

                        // delete category image from firebase storage


                        // then reset selectedCategories list in adapter and
                        // reset onLongClicked to false
                        adapter.resetParamsForActionMode()
                    }
                }
            }

        }

        primaryActionModeCallback.onDestroyActionModeClickListener = object : OnDestroyActionModeClickListener {
            override fun onDestroyActionModeClicked() {
                adapter.callDatasetChanged()
            }

        }


        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {
//                viewModel.printCategoriesList()
                //adapter.data = it
                adapter.submitList(it.toList())
            }
        })


        binding.addCategoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_newCategoryDetailsFragment)
        }

        return binding.root
    }

    private fun gridViewItemOnClick(category: Category){
        val b= Bundle()
//        b.putParcelable("category", category)
        b.putString("name", category.name)
        b.putString("uri", category.uri)
        b.putString("key", category.key)
        findNavController().navigate(R.id.action_categoriesFragment_to_particularCategoryFragment, b)
    }

}