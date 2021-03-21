package com.savage9ishere.tiwarimartadmin.categories.new_category_details

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimartadmin.databinding.NewCategoryDetailsFragmentBinding

const val REQUEST_IMAGE_GET = 1

class NewCategoryDetailsFragment : Fragment() {

    private lateinit var viewModel: NewCategoryDetailsViewModel
    private var imageUri: Uri? = null
    private var imageView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewCategoryDetailsFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(NewCategoryDetailsViewModel::class.java)

        imageView = binding.categoryImageView

        binding.categoryImageView.setOnClickListener {
            //select image from local storage using intent
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
        }

        binding.proceedButton.setOnClickListener {
            val text = binding.categoryNameEditText.text.toString()
            if (!text.isEmpty() && imageUri != null) {

                viewModel.addToDatabase(text, imageUri!!)

            } else {
                Toast.makeText(context, "Name or Image not added!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.uploadToStorageCompleted.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Upload to storage completed", Toast.LENGTH_SHORT).show()
                viewModel.uploadToStorageCompletedFinished()
            }
        })
        viewModel.uploadToStorageFailed.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Upload to storage failed, Try Again", Toast.LENGTH_SHORT)
                    .show()
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
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            //val fullPhotoUri : Uri? = data?.data
            if (data != null) {
                imageUri = data.data!!
                imageView!!.setImageURI(imageUri)
            }
        }
    }
}