package com.beetzung.quizgame.ui.game

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.beetzung.quizgame.R
import com.beetzung.quizgame.data.prefs.Preferences
import com.beetzung.quizgame.databinding.FragmentGameBinding
import com.beetzung.quizgame.ui.MainActivity.Companion.TAG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.recycler.layoutManager = LinearLayoutManager(context)
        lifecycleScope.launch {
            viewModel.gameFlow.collect { state ->
                updateUi(state)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun updateUi(state: GameState) {
        Log.d(TAG, "updateUi() called with: state = $state")
        binding.radioGroup.removeAllViews()
        state.refreshing?.let {
            binding.refreshLayout.isRefreshing = it 
        }
        state.error?.let { error ->
            Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
        }
        var playerList = listOf<Pair<String, String>>()
        state.score?.let {
            playerList = it
        } ?: state.players?.let {
            val tempPlayerList = mutableListOf<Pair<String, String>>()
            for (item in state.players) {
                tempPlayerList.add(Pair(item, ""))
            }
            playerList = tempPlayerList
        }
        binding.recycler.adapter = Adapter(playerList)
        when (state.status) {
            is GameState.Status.Created -> {
                binding.buttonAnswer.setText(R.string.button_start)
                binding.buttonAnswer.setOnClickListener { viewModel.start() }
                binding.buttonAnswer.isEnabled = state.status.isAdmin
                binding.textQuestion.setText(R.string.text_waiting)
            }
            is GameState.Status.Started -> {
                if (state.question != null) {
                    binding.buttonAnswer.isEnabled = true
                    binding.buttonAnswer.setText(R.string.button_answer)
                    binding.textQuestion.text = state.question.text
                    state.question.answers.values.forEachIndexed { index, answer ->
                        val button = RadioButton(context)
                        button.text = answer
                        binding.radioGroup.addView(button)
                        if (index == 0)
                            button.isChecked = true
                    }
                    binding.buttonAnswer.setOnClickListener {
                        val radioButtonID: Int = binding.radioGroup.checkedRadioButtonId
                        val radioButton: View = binding.radioGroup.findViewById(radioButtonID)
                        val index: Int = binding.radioGroup.indexOfChild(radioButton)
                        viewModel.answer(index)
                    }
                } else {
                    binding.textQuestion.text = getString(R.string.text_finished_waiting)
                    binding.buttonAnswer.isEnabled = false
                    binding.buttonAnswer.setText(R.string.button_answer)
                }
            }
            is GameState.Status.Finished -> {
                binding.textQuestion.text = getString(R.string.text_finished, state.status.winner)
                binding.buttonAnswer.isEnabled = true
                binding.buttonAnswer.setText(R.string.button_finish)
                binding.buttonAnswer.setOnClickListener {
                    Preferences.reset()
                    Navigation.findNavController(requireView()).popBackStack()
                }
            }
            else -> Unit
        }
        state.answerIsCorrect?.let { answerIsCorrect ->
            AlertDialog.Builder(context)
                .setTitle(if (answerIsCorrect) R.string.text_correct else R.string.text_wrong)
                .setView(ImageView(context).apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            if (answerIsCorrect) R.drawable.ic_correct else R.drawable.ic_wrong
                        )
                    )
                })
                .setPositiveButton(R.string.button_ok, null)
                .create()
                .show()
        }
    }
}