package com.savage9ishere.tiwarimartadmin.orders.particular_order

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.databinding.CancellationReasonFragmentBinding
import com.savage9ishere.tiwarimartadmin.orders.OrderItem

class CancellationReasonFragment : Fragment() {

    private lateinit var viewModel: CancellationReasonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CancellationReasonFragmentBinding.inflate(inflater)

        val order : OrderItem? = requireArguments().getParcelable("orderItem")

        viewModel = ViewModelProvider(this).get(CancellationReasonViewModel::class.java)

        binding.cancelOrderButton.setOnClickListener {
            val text = binding.reasonTextInputLayout.editText?.text

            if(text!!.isNotEmpty()){
                viewModel.cancelOrder(text, order)
            }
        }

        viewModel.orderCancelled.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneOrderCancelling()
                findNavController().popBackStack(R.id.ordersFragment, false)
            }
        })

        return binding.root
    }

}