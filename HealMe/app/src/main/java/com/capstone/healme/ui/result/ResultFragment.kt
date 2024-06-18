package com.capstone.healme.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentResultBinding
import com.capstone.healme.ui.register.RegisterViewModel
import com.google.ai.client.generativeai.GenerativeModel

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultViewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: ResultViewModel by viewModels {
            factory
        }

        resultViewModel = viewModel

//        val prompt = resources.getString(R.string.prompt_gemini, "kanker kulit", 25, 70, "laki-laki")
//        resultViewModel.getResponse(prompt)

        resultViewModel.geminiResponse.observe(viewLifecycleOwner) { geminiResponse ->
            bindData(geminiResponse.explanation, geminiResponse.firstAidRecommendation)
        }

        resultViewModel.getDetailHistory("666ef6c245b059f2ba91c44c").observe(viewLifecycleOwner) {
            val prompt = resources.getString(R.string.prompt_gemini,"jerawat", 25, 70, "laki-laki")
            val prompt1 = "tuliskan fakta menarik max 50 kata tentang kesehatan kulit yang interaktif"
            val prompt2 = "Bagikan 25 kata tentang tips menjaga kesehatan kulit. Ajak peserta untuk bertanya dan berbagi pengalaman mereka."
            resultViewModel.getResponse(prompt1)
            binding.apply {
                Glide.with(ivWound)
                    .load(it.imageUrl)
                    .into(ivWound)
                tvWoundType.text = it.result
                tvConfidenceScore.text = it.confidenceScore.toString()
            }
        }
    }

    private fun bindData(explanation: String, firstAidRecommendation: String) {
        binding.apply {
            tvExplanation.text = explanation
            tvFirstAid.text = firstAidRecommendation
//            Glide.with(ivWound)
//                .load("https://storage.googleapis.com/skin-image-upload-1/20240614T134623370Z.jpg")
//                .into(ivWound)
        }
    }


    private fun setupAction() {
        binding.apply {
            setupToggle(tvExplanation, toggleTextViewExplanation)
            setupToggle(tvFirstAid, toggleTextViewFirstAid)

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnHealthcare.setOnClickListener {
                findNavController().navigate(R.id.action_resultFragment_to_navigation_healthcare)
            }
        }
    }

    private fun setupToggle(textView: TextView, toggleText: TextView) {
        toggleText.setOnClickListener {
            if (textView.maxLines == 4) {
                textView.maxLines = Int.MAX_VALUE
                toggleText.text = resources.getString(R.string.read_less)
            } else {
                textView.maxLines = 4
                toggleText.text = resources.getString(R.string.read_more)
            }
        }
    }
}