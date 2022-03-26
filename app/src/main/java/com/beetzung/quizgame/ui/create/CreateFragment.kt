package com.beetzung.quizgame.ui.create

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.beetzung.quizgame.R
import com.beetzung.quizgame.databinding.FragmentCreateBinding
import com.beetzung.quizgame.ui.game.GameFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private val viewModel: CreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        binding.buttonCreate.setOnClickListener {
            when {
                binding.inputName.editText!!.text!!.isEmpty() -> {
                    binding.inputName.error = getString(R.string.error_name)
                }
                binding.inputNumber.editText!!.text!!.isEmpty() -> {
                    binding.inputNumber.error = getString(R.string.error_number)
                }
                else -> {
                    viewModel.createGame(
                        binding.inputName.editText!!.text.toString(),
                        binding.inputNumber.editText!!.text.toString().toInt()
                    )
                }
            }
        }
        lifecycleScope.launch {
            viewModel.stateFlow.collect { state ->
                if (state.password != null)
                    showDialog(state.password)
                if (state.error != null)
                    Snackbar.make(requireView(), state.error, Snackbar.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun showDialog(password: String) {
        val layout = LinearLayout(context)
        layout.addView(EditText(context).apply {
            setText(password)
            isEnabled = false
        })
        layout.addView(Button(context).apply {
            setText(R.string.button_copy)
            setOnClickListener {
                val clipboard: ClipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("password", password)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), R.string.text_copied, Toast.LENGTH_SHORT).show()
            }
        })
        layout.orientation = LinearLayout.VERTICAL
        AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)
            .setTitle(R.string.text_copy_title)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                Navigation.findNavController(requireView())
                    .navigate(GameFragmentDirections.openGame())
            }
            .create()
            .show()
    }
}