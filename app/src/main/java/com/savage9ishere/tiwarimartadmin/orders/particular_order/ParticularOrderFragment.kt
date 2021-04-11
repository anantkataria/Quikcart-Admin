package com.savage9ishere.tiwarimartadmin.orders.particular_order

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
import com.savage9ishere.tiwarimartadmin.databinding.ParticularOrderFragmentBinding
import com.savage9ishere.tiwarimartadmin.orders.AddressItem
import com.savage9ishere.tiwarimartadmin.orders.CartItems
import com.savage9ishere.tiwarimartadmin.orders.OrderItem

class ParticularOrderFragment : Fragment() {

    private lateinit var viewModel: ParticularOrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ParticularOrderFragmentBinding.inflate(inflater)

        val order : OrderItem? = requireArguments().getParcelable("orderItem")

        val listItems : ArrayList<CartItems> = order!!.listItems
        val address : AddressItem = order.address
        val paymentMethod : String = order.paymentMethod
        val authPhone : String = order.authPhone
        val orderPlacedTime : Long = order.orderPlacedTime
        val status : String = order.status


        val viewModelFactory = ParticularOrderViewModelFactory(
            listItems,
            address,
            paymentMethod,
            authPhone,
            orderPlacedTime,
            status
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularOrderViewModel::class.java)

        val adapter = ParticularOrderAdapter()
        binding.itemsRecyclerView.adapter = adapter
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.cartItems.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.orderOnWayButton.setOnClickListener {
            viewModel.makeOrderOnWay(order)
        }

        binding.orderDeliveredButton.setOnClickListener {
            viewModel.makeOrderDelivered(order)
        }

        binding.orderCancelledButton.setOnClickListener {
            viewModel.makeOrderCancelled()
        }

        viewModel.orderOnWay.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Order on way", Toast.LENGTH_SHORT).show()
                    val s = "STATUS : ORDER ON WAY"
                    binding.statusTextView.text = s
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                viewModel.doneMakingOrderOnWay()
            }
        })

        viewModel.orderDelivered.observe(viewLifecycleOwner, {
            it?.let{
                if(it){
                    Toast.makeText(context, "Order delivered", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneMakingOrderDelivered()

            }
        })

        viewModel.orderCancelled.observe(viewLifecycleOwner, {
            it?.let {
                val b = Bundle()
                b.putParcelable("orderItem", order)
                findNavController().navigate(R.id.action_particularOrderFragment_to_cancellationReasonFragment, b)
                viewModel.doneMakingOrderCancelled()
            }
        })

        return binding.root
    }

}