package com.savage9ishere.tiwarimartadmin.categories.particular_category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details.NewItemDetailsViewModel
import com.savage9ishere.tiwarimartadmin.databinding.ParticularCategoryFragmentBinding
import com.savage9ishere.tiwarimartadmin.util.OnActionItemClickListener
import com.savage9ishere.tiwarimartadmin.util.OnDestroyActionModeClickListener
import com.savage9ishere.tiwarimartadmin.util.PrimaryActionModeCallback


class ParticularCategoryFragment : Fragment() {

    private lateinit var viewModel: ParticularCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ParticularCategoryFragmentBinding.inflate(inflater)

        val categoryName : String? = requireArguments().getString("name")
        val categoryImageUri : String? = requireArguments().getString("uri")
        val categoryKey : String? = requireArguments().getString("key")

//        setHasOptionsMenu(true)

        val viewModelFactory = ParticularCategoryViewModelFactory(categoryName!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularCategoryViewModel::class.java)

        val primaryActionModeCallback = PrimaryActionModeCallback()
        val adapter = ItemsAdapter({itemOnclick(it)}, {
            primaryActionModeCallback.startActionMode(requireView(), R.menu.action_mode_menu, "Delete Items")
        })
        binding.recyclerViewItems.adapter = adapter
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.lifecycleOwner = this

        primaryActionModeCallback.onActionItemClickListener = object : OnActionItemClickListener {
            override fun onActionItemClick(item: MenuItem) {
                when(item.itemId){
                    R.id.delete_item -> {
                        viewModel.removeSelectedItems(adapter.getSelectedItemsList(), categoryName)
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

        viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        binding.updateCategoryButton.setOnClickListener{
            val b = Bundle()
            b.putString("name", categoryName)
            b.putString("uri", categoryImageUri)
            b.putString("key", categoryKey)
            findNavController().navigate(R.id.action_particularCategoryFragment_to_updateParticularCategoryFragment, b)
        }

        binding.addItemButton.setOnClickListener {
            val b = Bundle()
            b.putString("name", categoryName)
            findNavController().navigate(R.id.action_particularCategoryFragment_to_newItemDetailsFragment, b)
        }

        return binding.root
    }

    private fun itemOnclick(it: NewItemDetailsViewModel.Item) {
        val b = Bundle()
        b.putParcelable("Item", it)
        // todo findNavController().navigate() make a new fragment which will enable updating an item
        Toast.makeText(context, "Hi there, Hello", Toast.LENGTH_SHORT).show()
    }


}