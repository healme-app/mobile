package com.capstone.healme.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.healme.R
import com.capstone.healme.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

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
            if (textView.maxLines == 2) {
                textView.maxLines = Int.MAX_VALUE
                toggleText.text = resources.getString(R.string.read_less)
            } else {
                textView.maxLines = 2
                toggleText.text = resources.getString(R.string.read_more)
            }
        }
    }


}