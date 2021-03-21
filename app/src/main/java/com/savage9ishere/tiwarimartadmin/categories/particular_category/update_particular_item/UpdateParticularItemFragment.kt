package com.savage9ishere.tiwarimartadmin.categories.particular_category.update_particular_item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimartadmin.R

class UpdateParticularItemFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateParticularItemFragment()
    }

    private lateinit var viewModel: UpdateParticularItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_particular_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateParticularItemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}