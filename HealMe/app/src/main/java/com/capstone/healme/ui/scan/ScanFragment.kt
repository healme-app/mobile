package com.capstone.healme.ui.scan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.databinding.FragmentScanBinding
import com.capstone.healme.extension.gone
import com.capstone.healme.extension.setLoading
import com.capstone.healme.extension.showToast
import com.capstone.healme.extension.visible
import com.capstone.healme.helper.convertDate
import com.capstone.healme.helper.getTempFileUri
import com.capstone.healme.helper.reduceFileImage
import com.capstone.healme.helper.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var scanViewModel: ScanViewModel
    private var currentImageUri: Uri? = null

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

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            changeContainer()
        } else {
            deleteTempFile(requireContext())
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

        scanViewModel.scanResponse.observe(viewLifecycleOwner) { scanResponse ->
            if (!scanResponse.error) {
                scanResponse.let {
                    it.resultDb?.let { resultDb ->
                        val createdAt = convertDate(resultDb.createdAt!!)
                        val entity = ScanEntity(
                            resultDb.id!!,
                            resultDb.result,
                            resultDb.confidenceScore,
                            resultDb.imageUrl,
                            createdAt
                        )
                        scanViewModel.addHistory(entity)
                    }
                }
                deleteTempFile(requireContext())
                findNavController().navigate(R.id.action_scanFragment_to_resultFragment)
            } else {
                showToast(scanResponse.message?: "Error", false)
            }
        }

        scanViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            setLoading(binding.progressBar, isLoading)
        }
    }

    private fun setupAction() {
        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCamera.setOnClickListener {
                startCamera()
            }

            btnChangeImage.setOnClickListener {
                deleteTempFile(requireContext())
                changeContainer()
            }

            btnScan.setOnClickListener {
                scanImage()
//                findNavController().navigate(R.id.action_scanFragment_to_resultFragment)
            }
        }
    }

    private fun scanImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image", imageFile.name, requestImageFile
            )
            scanViewModel.scanImage(multipartBody)
        }
    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun startCamera() {
        currentImageUri = getTempFileUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun deleteTempFile(context: Context) {
        val cacheDir = context.cacheDir
        if (cacheDir.isDirectory) {
            val children = cacheDir.list()
            children?.forEach {
                val file = File(cacheDir, it)
                if (file.exists()) {
                    file.delete()
                }
            }
        }
        currentImageUri = null
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

    override fun onDestroy() {
        super.onDestroy()
        deleteTempFile(requireContext())
    }
}