package com.capstone.healme.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentResultBinding
import com.capstone.healme.extension.setCustomLoading
import com.capstone.healme.helper.calculateAge

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultViewModel: ResultViewModel

    private lateinit var prompt: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("resultId")
        setupViewModel(id!!)
        setupAction()
    }

    private fun setupViewModel(resultId: String) {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: ResultViewModel by viewModels {
            factory
        }

        resultViewModel = viewModel

        resultViewModel.getDetailHistory(resultId).observe(viewLifecycleOwner) {
            binding.apply {
                Glide.with(ivWound)
                    .load(it.imageUrl)
                    .into(ivWound)
                tvWoundType.text = it.result
                tvConfidenceScore.text = it.confidenceScore.toString()
            }

            val result = it.result

            resultViewModel.profileResponse.observe(viewLifecycleOwner) { profile ->
                profile.user?.let { user ->
                    val age = calculateAge(user.dateOfBirth!!)
                    prompt = resources.getString(
                        R.string.prompt_gemini_result,
                        result,
                        age,
                        user.weight,
                        user.gender
                    )
                    resultViewModel.getResponse(prompt)
                }
            }
        }

        resultViewModel.geminiResponse.observe(viewLifecycleOwner) { geminiResponse ->
            if (geminiResponse.error == true) {
                resultViewModel.getResponse(prompt)
            } else {
                bindData(geminiResponse.explanation!!, geminiResponse.firstAidRecommendation!!)
            }
        }

        resultViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            setCustomLoading(binding.progressBar, binding.containerExplanation, isLoading)
        }
    }

    private fun bindData(explanation: String, firstAidRecommendation: String) {
        binding.apply {
            tvExplanation.text = explanation
            tvFirstAid.text = firstAidRecommendation
        }
    }


    private fun setupAction() {
        binding.apply {
            setupToggle(tvExplanation, toggleTextViewExplanation)
            setupToggle(tvFirstAid, toggleTextViewFirstAid)
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