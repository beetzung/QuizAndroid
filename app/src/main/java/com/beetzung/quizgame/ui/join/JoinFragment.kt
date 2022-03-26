package com.beetzung.quizgame.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.beetzung.quizgame.R
import com.beetzung.quizgame.databinding.FragmentJoinBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class JoinFragment : Fragment() {
    private lateinit var binding: FragmentJoinBinding
    private val viewModel: JoinViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinBinding.inflate(inflater, container, false)
        binding.buttonJoin.setOnClickListener {
            when {
                binding.inputName.editText!!.text!!.isEmpty() -> {
                    binding.inputName.error = getString(R.string.error_name)
                }
                binding.inputPassword.editText!!.text!!.isEmpty() -> {
                    binding.inputPassword.error = getString(R.string.error_number)
                }
                else -> {
                    viewModel.joinGame(
                        binding.inputPassword.editText!!.text.toString(),
                        binding.inputName.editText!!.text.toString()
                    )
                }
            }
        }
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                if (state.error != null) {
                    Snackbar.make(requireView(), state.error, Snackbar.LENGTH_SHORT).show()
                }
                if (state.success == true)
                    Navigation.findNavController(requireView())
                        .navigate(JoinFragmentDirections.openGame())
            }
        }
        return binding.root
    }
}