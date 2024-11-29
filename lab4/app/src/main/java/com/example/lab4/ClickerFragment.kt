package com.example.lab4
import ClickerViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ClickerFragment : Fragment() {
    private val viewModel: ClickerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_clicker, container, false)

        val cookieCount: TextView = view.findViewById(R.id.cookieCount)
        val timeElapsed: TextView = view.findViewById(R.id.timeElapsed)
        val cookiePerSecondTextView: TextView = view.findViewById(R.id.cookiePerSecond)
        val cookiePerMinTextView: TextView = view.findViewById(R.id.cookiePerMin)
        val cookieButton: ImageView = view.findViewById(R.id.cookieButton)

        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_back)

        cookieButton.setOnClickListener {
            cookieButton.startAnimation(scaleDown)

            scaleDown.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    cookieButton.startAnimation(scaleUp)
                    viewModel.onCookieClicked()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                cookieCount.text = "Печенья: ${state.cookieCount}"
                timeElapsed.text = "Время: ${state.totalTime} сек."
                cookiePerSecondTextView.text = "Печенек в секунду: ${state.cookiesPerSecond}"
                cookiePerMinTextView.text = "Средняя скорость: ${state.cookiesPerMin} п/мин"
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.toastFlow.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }
}
