package com.example.scientificcalculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import net.objecthunter.exp4j.ExpressionBuilder


data class CalculatorState(
    val currentInputState: MutableState<String>,
    val expressionState: MutableState<String>,
    val resultState: MutableState<String>,
    val showScientificState: MutableState<Boolean>
) {
    fun clear() {
        currentInputState.value = "0"
        expressionState.value = ""
        resultState.value = ""
    }

    fun backspace() {
        if (currentInputState.value.isNotEmpty() && currentInputState.value != "0") {
            currentInputState.value = currentInputState.value.dropLast(1)
            if (currentInputState.value.isEmpty()) {
                currentInputState.value = "0"
            }
        }
    }

    fun toggleScientific() {
        showScientificState.value = !showScientificState.value
    }

    fun evaluate() {
        if (currentInputState.value.isNotEmpty()) {
            try {
                expressionState.value = currentInputState.value
                val sanitized = currentInputState.value
                    .replace("×", "*").replace("÷", "/").replace("%", "/100")
                    .replace("√", "sqrt").replace("log", "log10").replace("ln", "log")
                    .replace("sin⁻¹", "asin").replace("cos⁻¹", "acos").replace("tan⁻¹", "atan")
                    .replace("x^y", "^")
                val exp = ExpressionBuilder(sanitized).build()
                val evalResult = exp.evaluate()
                resultState.value = if (evalResult % 1.0 == 0.0) evalResult.toLong().toString() else evalResult.toString()
                currentInputState.value = resultState.value
            } catch (e: Exception) {
                resultState.value = "Error"
            }
        }
    }

    fun append(text: String) {
        val isFunction = text in listOf("sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹", "log", "ln", "√")
        if (resultState.value.isNotEmpty() && text !in listOf("×", "÷", "+", "-", "%", "x^y")) {
            currentInputState.value = if (isFunction) "$text(" else text
            expressionState.value = ""
            resultState.value = ""
            return
        }
        if (currentInputState.value == "0" && text !in listOf("×", "÷", "+", "-", "%", "x^y", ".")) {
            currentInputState.value = if (isFunction) "$text(" else text
        } else {
            currentInputState.value += if (isFunction) "$text(" else text
        }
        resultState.value = ""
    }
}

@Composable
fun rememberCalculatorState(
    currentInput: String = "0",
    expression: String = "",
    result: String = "",
    showScientific: Boolean = false
): CalculatorState = remember {
    CalculatorState(
        mutableStateOf(currentInput),
        mutableStateOf(expression),
        mutableStateOf(result),
        mutableStateOf(showScientific)
    )
}

fun handleButtonClick(buttonText: String, state: CalculatorState) {
    when (buttonText) {
        "AC" -> state.clear()
        "⌫" -> state.backspace()
        "=" -> state.evaluate()
        "all" -> state.toggleScientific()
        else -> state.append(buttonText)
    }
}