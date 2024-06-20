package com.capstone.healme.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
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
import com.capstone.healme.extension.gone
import com.capstone.healme.extension.setCustomLoading
import com.capstone.healme.extension.visible

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            if (scanEntity.isNotEmpty()) {
                binding.alertNoRecentScan.gone()
                binding.itemRecentScan.visible()
                if (scanEntity.size == 1) {
                    binding.cardview2.gone()
                    binding.apply {
                        scanEntity[0].let {
                            Glide.with(ivWound1).load(it.imageUrl).into(ivWound1)
                            tvWoundType1.text = it.result
                            tvConfidenceScore1.text = it.confidenceScore.toString()
                        }
                        cardview1.setOnClickListener {
                            navigateDetailHistory(scanEntity[0].resultId)
                        }
                    }
                } else {
                    binding.cardview2.visible()
                    val lastIndex = scanEntity.lastIndex
                    binding.apply {
                        scanEntity[lastIndex].let {
                            Glide.with(ivWound1).load(it.imageUrl).into(ivWound1)
                            tvWoundType1.text = it.result
                            tvConfidenceScore1.text = it.confidenceScore.toString()

                            cardview1.setOnClickListener { _ ->
                                navigateDetailHistory(it.resultId)
                            }
                        }
                        scanEntity[lastIndex - 1].let {
                            Glide.with(ivWound2).load(it.imageUrl).into(ivWound2)
                            tvWoundType2.text = it.result
                            tvConfidenceScore2.text = it.confidenceScore.toString()

                            cardview2.setOnClickListener { _ ->
                                navigateDetailHistory(it.resultId)
                            }
                        }
                    }
                }
            } else {
                binding.apply {
                    itemRecentScan.gone()
                    alertNoRecentScan.visible()
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

    private fun navigateDetailHistory(resultId: String) {
        val bundle = Bundle()
        bundle.putString("resultId", resultId)
        findNavController().navigate(R.id.action_HomeFragment_to_resultFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllHistories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}