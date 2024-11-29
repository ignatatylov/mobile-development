package com.example.myapplication
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityCalcBinding
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import net.objecthunter.exp4j.ExpressionBuilder

data class CalculatorState(
    val expressionText: String = "",
    val result: String = ""
)

sealed class CalculatorEvent {
    data class AppendCharacter(val value: String) : CalculatorEvent()
    object ClearLastCharacter : CalculatorEvent()
    object ClearAll : CalculatorEvent()
    object CalculateResult : CalculatorEvent()
}

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.AppendCharacter -> appendCharacter(event.value)
            CalculatorEvent.ClearLastCharacter -> removeLastCharacter()
            CalculatorEvent.ClearAll -> clearAll()
            CalculatorEvent.CalculateResult -> calculateResult()
            else -> {}
        }
    }

    private fun appendCharacter(value: String) {
        _state.update { currentState ->
            val correctedValue = when (value) {
                "×" -> "*"
                "÷" -> "/"
                else -> value
            }
            currentState.copy(
                expressionText = currentState.expressionText + value,
                result = ""
            )
        }
    }

    private fun removeLastCharacter() {
        _state.update { currentState ->
            val expression = currentState.expressionText
            if (expression.isNotEmpty()) {
                currentState.copy(
                    expressionText = expression.dropLast(1),
                    result = ""
                )
            } else currentState
        }
    }

    private fun clearAll() {
        _state.value = CalculatorState()
    }

    private fun calculateResult() {
        val expression = _state.value.expressionText
        try {
            val correctedExpression = expression.replace("×", "*").replace("÷", "/")
            val result = ExpressionBuilder(correctedExpression).build().evaluate()
            _state.update {
                it.copy(result = result.toString())
            }
        } catch (e: ArithmeticException) {
            _state.update {
                it.copy(result = "Нельзя делить на 0")
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(result = "Ошибка")
            }
        }
    }
}

@Suppress("DEPRECATION")
class CalcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                binding.expression.text = state.expressionText
                binding.result.text = state.result
                if (state.result == "Ошибка" || state.result == "Нельзя делить на 0") {
                    binding.result.setTextColor(getColor(R.color.red))
                } else {
                    binding.result.setTextColor(getColor(R.color.black))
                }
            }
        }

        val buttons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9,
            binding.buttonAdd, binding.buttonSub, binding.buttonMultiply, binding.buttonDivide,
            binding.buttonComma
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                val value = (it as Button).text.toString()
                viewModel.onEvent(CalculatorEvent.AppendCharacter(value))
                viewModel.onEvent(CalculatorEvent.CalculateResult)
            }
        }

        binding.buttonClear.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.ClearLastCharacter)
        }

    }
}
