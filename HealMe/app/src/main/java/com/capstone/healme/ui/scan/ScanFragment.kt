package com.capstone.healme.ui.scan

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentScanBinding
import com.capstone.healme.extension.gone
import com.capstone.healme.extension.showToast
import com.capstone.healme.extension.visible

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private lateinit var scanViewModel: ScanViewModel

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { inputUri ->
        if (inputUri != null) {
            currentImageUri = inputUri
            changeContainer()
        } else {
            showToast(getString(R.string.no_image_selected), false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: ScanViewModel by viewModels {
            factory
        }

        scanViewModel = viewModel
    }

    private fun setupAction() {
        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }

            btnChangeImage.setOnClickListener {
                currentImageUri = null
                changeContainer()
            }
        }
    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun changeContainer() {
        if (currentImageUri != null) {
            binding.apply {
                containerImageChooser.gone()
                containerSelectedImage.visible()
                ivPreviewSelectedImage.setImageURI(currentImageUri)
            }
        } else {
            binding.apply {
                containerImageChooser.visible()
                containerSelectedImage.gone()
            }
        }
    }
}