package com.capstone.healme.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieCompositionFactory
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentOnboardingItemBinding
import com.capstone.healme.extension.gone
import com.capstone.healme.extension.visible

class OnboardingItemFragment : Fragment() {
    private var _binding: FragmentOnboardingItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var onboardingViewModel: OnboardingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(TAB_STATE)

        setContent(position!!)
        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: OnboardingViewModel by viewModels {
            factory
        }

        onboardingViewModel = viewModel
    }

    private fun setupAction() {
        binding.btnStart.setOnClickListener {
            onboardingViewModel.setFirstOpen()
        }
    }

    private fun setContent(position: Int) {
        binding.apply {
            when (position) {
                0 -> {
                    val inputStream = resources.openRawResource(R.raw.onboarding_1)
                    val jsonString = inputStream.bufferedReader().use { it.readText() }

                    LottieCompositionFactory.fromJsonString(jsonString, null)
                        .addListener { composition ->
                            lottieAnimation.setComposition(composition)
                            lottieAnimation.playAnimation()
                        }
                    tvTitle.text = resources.getString(R.string.onboarding_title_1)
                    tvDesc.text = resources.getString(R.string.onboarding_desc_1)
                    btnStart.gone()
                }
                1 -> {
                    val inputStream = resources.openRawResource(R.raw.onboarding_2)
                    val jsonString = inputStream.bufferedReader().use { it.readText() }

                    LottieCompositionFactory.fromJsonString(jsonString, null)
                        .addListener { composition ->
                            lottieAnimation.setComposition(composition)
                            lottieAnimation.playAnimation()
                        }
                    tvTitle.text = resources.getString(R.string.onboarding_title_2)
                    tvDesc.text = resources.getString(R.string.onboarding_desc_2)
                    btnStart.gone()
                }
                else -> {
                    val inputStream = resources.openRawResource(R.raw.onboarding_3)
                    val jsonString = inputStream.bufferedReader().use { it.readText() }

                    LottieCompositionFactory.fromJsonString(jsonString, null)
                        .addListener { composition ->
                            lottieAnimation.setComposition(composition)
                            lottieAnimation.playAnimation()
                        }
                    tvTitle.text = resources.getString(R.string.onboarding_title_3)
                    tvDesc.text = resources.getString(R.string.onboarding_desc_3)
                    btnStart.visible()
                }
            }
        }
    }

    companion object {
        const val TAB_STATE = "tab_index"
    }
}