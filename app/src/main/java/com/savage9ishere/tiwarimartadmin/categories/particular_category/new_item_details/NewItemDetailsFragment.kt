package com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimartadmin.databinding.NewItemDetailsFragmentBinding

const val REQUEST_IMAGE_GET = 1

class NewItemDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = NewItemDetailsFragment()
    }

    private lateinit var viewModel: NewItemDetailsViewModel
    private var inStock = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewItemDetailsFragmentBinding.inflate(inflater)

        val categoryName : String? = requireArguments().getString("name")

        viewModel = ViewModelProvider(this).get(NewItemDetailsViewModel::class.java)

        val adapter = ItemPhotosAdapter {
            viewModel.removeItem(it)
        }

        binding.recyclerViewItemPhotos.adapter = adapter
        val horizontalLayoutManger = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewItemPhotos.layoutManager = horizontalLayoutManger

        val adapter2 = OtherSizesAdapter{
            viewModel.removeOtherSize(it)
        }

        val newSizesRecyclerView = binding.newSizesRecyclerView
        newSizesRecyclerView.adapter = adapter2
        newSizesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.lifecycleOwner = this


        binding.addPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
        }

        binding.stockAvailabilityButton.setOnClickListener {
            if (inStock){
                inStock = false
                binding.stockAvailabilityButton.text = "Change to Stock Available"
            }
            else {
                inStock = true
                binding.stockAvailabilityButton.text = "Change to Not In Stock"
            }
        }

        binding.proceedToAddItemButton.setOnClickListener {
            val array = Array(6){
                ""
            }
            array[0] = binding.textInputLayout1.editText?.text.toString()
            array[1] = binding.textInputLayout5.editText?.text.toString()
            array[2] = binding.textInputLayout2.editText?.text.toString()
            array[3] = binding.textInputLayout3.editText?.text.toString()
            array[4] = binding.textInputLayout4.editText?.text.toString()
            array[5] = binding.textInputLayout6.editText?.text.toString()

            var somethingIsEmpty = false

            for(str in array){
                if(str.isEmpty()){
                    Toast.makeText(context, "Something is empty bro", Toast.LENGTH_SHORT).show()
                    somethingIsEmpty = true
                    break;
                }
            }

            if(viewModel.getItemPhotosArrayList().isEmpty()){
                Toast.makeText(context, "Add some images bro", Toast.LENGTH_SHORT).show()
                somethingIsEmpty = true
            }

            if(!somethingIsEmpty){
                //now we can send items to database
                viewModel.sendToDatabase(array, categoryName, inStock)
            }
        }

        val textInputLayout7 = binding.textInputLayout7
        val textInputLayout8 = binding.textInputLayout8
        val addNewSizeButton = binding.addNewSizeButton

        binding.addNewSizeImage.setOnClickListener {
            textInputLayout7.visibility = View.VISIBLE
            textInputLayout8.visibility = View.VISIBLE
            addNewSizeButton.visibility = View.VISIBLE
        }

        binding.addNewSizeButton.setOnClickListener {
            val size = textInputLayout7.editText?.text.toString()
            val price = textInputLayout8.editText?.text.toString()
            Log.e("111", "size = $size, price = $price")
            viewModel.addToOtherSizes(size, price)
            textInputLayout7.visibility = View.GONE
            textInputLayout8.visibility = View.GONE
            addNewSizeButton.visibility = View.GONE
        }

        viewModel.otherSizes.observe(viewLifecycleOwner, {
            it?.let {
                val list = it.toList()
                if(list.isNotEmpty()){
                   binding.newSizesRecyclerView.visibility = View.VISIBLE
                }
                else{
                    binding.newSizesRecyclerView.visibility = View.GONE
                }
                adapter2.submitList(it.toList())
            }
        })

        viewModel.itemImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        viewModel.uploadToStorageCompleted.observe(viewLifecycleOwner,  {
            it?.let {
                Toast.makeText(context, "Upload to storage completed", Toast.LENGTH_SHORT).show()
                viewModel.uploadToStorageCompletedFinished()
            }
        })
        viewModel.uploadToStorageFailed.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Upload to storage failed, Try Again", Toast.LENGTH_SHORT).show()
                viewModel.uploadToStorageFailedFinished()
            }
        })
        viewModel.uploadToDatabaseCompleted.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Upload to database completed", Toast.LENGTH_SHORT).show()
                viewModel.uploadToDatabaseCompletedFinished()
                this.findNavController().popBackStack()
            }
        })
        viewModel.uploadToDatabaseFailed.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Upload to database failed", Toast.LENGTH_SHORT).show()
                viewModel.uploadToDatabaseFailedFinished()
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){
            //put image uri to arraylist stored in the newItemDetailsViewModel
            if(data != null){
                val imageUri = data.data
                viewModel.addPhotoToArrayList(imageUri.toString())
            }
        }
    }
}