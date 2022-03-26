package com.beetzung.quizgame.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.beetzung.quizgame.data.prefs.Preferences
import com.beetzung.quizgame.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        if (Preferences.getGame() != null) {
            binding.buttonContinue.visibility = View.VISIBLE
        }
        return binding.root
    }
}