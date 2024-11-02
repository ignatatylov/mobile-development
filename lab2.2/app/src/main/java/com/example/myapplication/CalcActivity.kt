package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import net.objecthunter.exp4j.ExpressionBuilder
import com.example.myapplication.databinding.ActivityCalcBinding

class CalcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcBinding
    private var expression = ""
    private var expressionText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9,
            binding.buttonAdd, binding.buttonSub, binding.buttonMultiply, binding.buttonDivide,
            binding.buttonComma
        )

        buttons.forEach { button ->
            button.setOnClickListener {
//                избежать каста
                appendToExpression((it as Button).text.toString())
            }
        }

        binding.buttonClear.setOnClickListener {
            removeLastCharacter()
        }
    }

    private fun appendToExpression(value: String) {
        val correctedValue = when (value) {
            "×" -> "*"
            "÷" -> "/"
            else -> value
        }
        expression += correctedValue
        expressionText += value
        updateExpression()
    }

    private fun removeLastCharacter() {
        if (expression.isNotEmpty()) {
            expression = expression.substring(0, expression.length - 1)
            expressionText = expressionText.substring(0, expressionText.length - 1)
        }
        updateExpression()
    }

    private fun updateExpression() {
        binding.expression.text = expressionText
        calculateResult()
    }

    private fun calculateResult() {
        try {
            val expr = ExpressionBuilder(expression).build()
            val result = expr.evaluate()

            binding.expression.setTextColor(getColor(R.color.black))
            binding.result.setTextColor(getColor(R.color.black))
            binding.result.text = result.toString()
        } catch (e: ArithmeticException) {
            binding.expression.setTextColor(getColor(R.color.red))
            binding.result.setTextColor(getColor(R.color.red))
            binding.result.text = "Нельзя делить на 0"
        } catch (e: Exception) {
            binding.expression.setTextColor(getColor(R.color.red))
            binding.result.setTextColor(getColor(R.color.red))
            binding.result.text = "Ошибка"
        }
    }
}
