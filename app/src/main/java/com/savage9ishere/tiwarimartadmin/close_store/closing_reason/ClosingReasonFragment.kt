package com.savage9ishere.tiwarimartadmin.close_store.closing_reason

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimartadmin.databinding.ClosingReasonFragmentBinding

class ClosingReasonFragment : Fragment() {

    private lateinit var viewModel: ClosingReasonViewModel
    private var openingStatusStr = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ClosingReasonFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(ClosingReasonViewModel::class.java)

        openingStatusStr = requireArguments().getString("status", "")!!

        binding.statusEditText.setText(openingStatusStr)

        binding.uploadButton.setOnClickListener {
            val statusEditTextStr = binding.statusEditText.text.toString()
            if (statusEditTextStr.isNotEmpty()){
                viewModel.uploadReason(statusEditTextStr)
            }
            else {
                Toast.makeText(context, "Add Description", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updationComplete.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Store Closed", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneUpdationComplete()
            }
        })

        return binding.root
    }

}