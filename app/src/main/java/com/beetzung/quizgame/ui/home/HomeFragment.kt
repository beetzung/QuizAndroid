package com.beetzung.quizgame.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.beetzung.quizgame.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val viewModel: HomeViewModel by viewModels()

        binding.buttonCreate.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(HomeFragmentDirections.createGame())
        }
        binding.buttonConnect.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(HomeFragmentDirections.joinGame())
        }
        binding.buttonContinue.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(HomeFragmentDirections.openGame())
        }
        if (viewModel.checkGame()) {
            binding.buttonContinue.visibility = View.VISIBLE
        }
        return binding.root
    }
}