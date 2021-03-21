package com.savage9ishere.tiwarimartadmin.categories.particular_category.update_particular_category

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.savage9ishere.tiwarimartadmin.databinding.UpdateParticularCategoryFragmentBinding

const val REQUEST_IMAGE_GET = 1

class UpdateParticularCategoryFragment : Fragment() {

    private lateinit var viewModel: UpdateParticularCategoryViewModel
    private var imageUri : Uri? = null
    private var imageView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UpdateParticularCategoryFragmentBinding.inflate(inflater)

        val categoryName = requireArguments().getString("name")
        val categoryImageURL = requireArguments().getString("uri")
        val categoryKey = requireArguments().getString("key")

        viewModel = ViewModelProvider(this).get(UpdateParticularCategoryViewModel::class.java)

        binding.categoryNameEditText.setText(categoryName)

        imageUri = categoryImageURL!!.toUri()

        imageView = binding.categoryImageView
        Glide.with(imageView!!.context)
            .load(categoryImageURL.toUri().buildUpon().scheme("https").build())
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(imageView!!)

        binding.categoryImageView.setOnClickListener {
            //select image from local storage using intent
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent,
                    REQUEST_IMAGE_GET
                )
            }
        }

        binding.proceedButton.setOnClickListener {
            val text = binding.categoryNameEditText.text.toString()
            if (text.isNotEmpty() && imageUri != null) {
                if(imageUri == categoryImageURL.toUri()){
                    viewModel.updateToRealtimeDatabaseOnly(text, imageUri!!, categoryKey)
                }
                else {
                    viewModel.addToStorageAndUpdateToDatabase(text, imageUri!!, categoryKey)
                }
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