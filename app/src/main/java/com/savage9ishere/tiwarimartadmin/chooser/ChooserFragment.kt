package com.savage9ishere.tiwarimartadmin.chooser

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimartadmin.R
import com.savage9ishere.tiwarimartadmin.databinding.ChooserFragmentBinding

class ChooserFragment : Fragment() {

    companion object {
        fun newInstance() = ChooserFragment()
    }

    private lateinit var viewModel: ChooserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ChooserFragmentBinding.inflate(inflater)

        binding.categoriesButton.setOnClickListener {
            findNavController().navigate(R.id.action_chooserFragment_to_categoriesFragment)
        }

        binding.ordersButton.setOnClickListener {
            findNavController().navigate(R.id.action_chooserFragment_to_ordersFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}