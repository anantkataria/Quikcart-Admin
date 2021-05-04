package com.savage9ishere.tiwarimartadmin.close_store

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.databinding.CloseStoreFragmentBinding

class CloseStoreFragment : Fragment() {

    private lateinit var viewModel: CloseStoreViewModel
    private var openingStatusStr : String = ""
    private var closeOpenStr : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CloseStoreFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(CloseStoreViewModel::class.java)

        binding.closeOpenButton.setOnClickListener {
            if (closeOpenStr == "open"){
                val b = Bundle()
                b.putString("status", openingStatusStr)
                if(findNavController().currentDestination?.id == R.id.closeStoreFragment)
                    findNavController().navigate(R.id.action_closeStoreFragment_to_closingReasonFragment, b)
            }
            else {
                viewModel.openStore()
            }
        }

        binding.noticeButton.setOnClickListener {
            val b = Bundle()
            b.putString("status", openingStatusStr)
            if(findNavController().currentDestination?.id == R.id.closeStoreFragment)
                findNavController().navigate(R.id.action_closeStoreFragment_to_closingReasonFragment, b)
        }

        viewModel.isOpen.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    closeOpenStr = "open"
                    val closeOpenButtonText = "Close Store"
                    val closeOpenText = "Open"
                    binding.closeOpenButton.text = closeOpenButtonText
                    binding.openCloseText.text = closeOpenText
                    binding.noticeButton.visibility = View.INVISIBLE
                }
                else {
                    closeOpenStr = "close"
                    val closeOpenButtonText = "Open Store"
                    val closeOpenText = "Closed"
                    binding.closeOpenButton.text = closeOpenButtonText
                    binding.openCloseText.text = closeOpenText
                    binding.noticeButton.visibility = View.VISIBLE
                }
                binding.closeOpenButton.visibility = View.VISIBLE
                binding.openCloseText.visibility = View.VISIBLE
//                viewModel.doneIsOpen()
            }
        })

        viewModel.openingAgainStatus.observe(viewLifecycleOwner, {
            it?.let {
                openingStatusStr = it
//                viewModel.doneOpeningAgainStatus()
            }
        })

        viewModel.openingSuccessful.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Store Opened successfully", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneOpeningSuccessful()
            }
        })

        return binding.root
    }

}