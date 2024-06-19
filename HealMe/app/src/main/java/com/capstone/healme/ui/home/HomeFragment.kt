package com.capstone.healme.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentHomeBinding
import com.capstone.healme.extension.setCustomLoading

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }
        homeViewModel = viewModel

        homeViewModel.getGeminiTips(resources.getString(R.string.prompt_gemini_fun_fact))

        homeViewModel.geminiResponse.observe(viewLifecycleOwner) { response ->
            animateCardView(binding.cardViewHealthTips)
            binding.tvHealthTips.text = response
        }

        homeViewModel.histories.observe(viewLifecycleOwner) { scanEntity ->
            binding.apply {
                scanEntity[0].let {
                    Glide.with(ivWound1)
                        .load(it.imageUrl)
                        .into(ivWound1)
                    tvWoundType1.text = it.result
                    tvConfidenceScore1.text = it.confidenceScore.toString()
                }
                scanEntity[1].let {
                    Glide.with(ivWound2)
                        .load(it.imageUrl)
                        .into(ivWound2)
                    tvWoundType2.text = it.result
                    tvConfidenceScore2.text = it.confidenceScore.toString()
                }
            }
        }

        binding.apply {
            homeViewModel.loadingRecentScan.observe(viewLifecycleOwner) { isLoading ->
                setCustomLoading(progressBarRecentScan, containerRecentScan, isLoading)
            }
            homeViewModel.loadingHealthTips.observe(viewLifecycleOwner) { isLoading ->
                setCustomLoading(progressBarTips, tvHealthTips, isLoading)
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnHistory.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_HistoryFragment)
            }

            btnRefresh.setOnClickListener {
                animateCardView(binding.cardViewHealthTips)
                homeViewModel.getGeminiTips(resources.getString(R.string.prompt_gemini_fun_fact))
            }

            btnFindHealthcare.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_HealthcareFragment)
            }
        }
    }

    private fun animateCardView(view: View) {
        view.post {
            val initialHeight = view.height
            view.measure(
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            val targetHeight = view.measuredHeight

            if (initialHeight != targetHeight) {
                val animator = ValueAnimator.ofInt(initialHeight, targetHeight)
                animator.addUpdateListener { animation ->
                    val layoutParams = view.layoutParams
                    layoutParams.height = animation.animatedValue as Int
                    view.layoutParams = layoutParams
                }
                animator.duration = 300
                animator.start()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}