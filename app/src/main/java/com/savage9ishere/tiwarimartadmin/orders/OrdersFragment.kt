package com.savage9ishere.tiwarimartadmin.orders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.databinding.OrdersFragmentBinding

class OrdersFragment : Fragment() {

    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OrdersFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        val adapter = OrdersAdapter{
            onOrderItemClick(it)
        }

        binding.ordersRecyclerView.adapter = adapter
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)

        binding.lifecycleOwner = this

        viewModel.orders.observe(viewLifecycleOwner, {
            it?.let {
                val list = it.toList()
                adapter.submitList(list)
            }

        })

        return binding.root
    }

    private fun onOrderItemClick(orderItem: OrderItem) {
        val b = Bundle()
        b.putParcelable("orderItem", orderItem)
        findNavController().navigate(R.id.action_ordersFragment_to_particularOrderFragment, b)
    }

}