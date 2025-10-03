package com.example.scientificcalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val BackgroundBlack = Color(0xFF000000)
val OrangeHighlight = Color(0xFFFF9800)
val DarkGrayButton = Color(0xFF333333)
val LightGrayButton = Color(0xFFA5A5A5)
val WhiteText = Color.White

@Composable
fun ScientificCalculatorApp() {
    val calculatorState = rememberCalculatorState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            CalculatorDisplay(
                expression = calculatorState.expressionState.value,
                result = if (calculatorState.resultState.value.isNotEmpty()) calculatorState.resultState.value else calculatorState.currentInputState.value
            )
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorButtonGrid(
                state = calculatorState,
                onButtonClick = { buttonText -> handleButtonClick(buttonText, calculatorState) }
            )
        }
    }
}

@Composable
fun CalculatorDisplay(expression: String, result: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(end = 4.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = expression, fontSize = 28.sp, color = Color.Gray, textAlign = TextAlign.End, maxLines = 1)
        Text(text = result, fontSize = 60.sp, color = WhiteText, textAlign = TextAlign.End, maxLines = 2)
    }
}

@Composable
fun CalculatorButtonGrid(state: CalculatorState, onButtonClick: (String) -> Unit) {
    val scientificRows = listOf(
        listOf("sin", "cos", "tan", "log", "ln"),
        listOf("sin⁻¹", "cos⁻¹", "tan⁻¹", "x^y", "("),
        listOf("√", "x!", "1/x", "%", ")")
    )
    val basicRows = listOf(
        listOf("AC", "⌫", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("all", "0", ".", "=")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (state.showScientificState.value) {
            scientificRows.forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        CalculatorButton(
                            text = item,
                            modifier = Modifier.weight(1f),
                            backgroundColor = DarkGrayButton,
                            fontSize = 18.sp,
                            size = 64.dp,
                            onClick = { onButtonClick(item) }
                        )
                    }
                }
            }
        }
        basicRows.forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                if (rowItems.size == 3) {
                    CalculatorButton(text = rowItems[0], modifier = Modifier.weight(1f), backgroundColor = LightGrayButton, textColor = BackgroundBlack, onClick = { onButtonClick(rowItems[0]) })
                    CalculatorButton(text = rowItems[1], modifier = Modifier.weight(1f), backgroundColor = LightGrayButton, textColor = BackgroundBlack, onClick = { onButtonClick(rowItems[1]) })
                    CalculatorButton(text = rowItems[2], modifier = Modifier.weight(1f), backgroundColor = OrangeHighlight, onClick = { onButtonClick(rowItems[2]) })
                } else {
                    rowItems.forEach { item ->
                        val backgroundColor = when (item) {
                            "AC", "⌫" -> LightGrayButton
                            "%", "÷", "×", "-", "+", "=", "all" -> OrangeHighlight
                            else -> DarkGrayButton
                        }
                        val textColor = if (item in listOf("AC", "⌫")) BackgroundBlack else WhiteText
                        CalculatorButton(
                            text = item,
                            modifier = Modifier.weight(1f),
                            backgroundColor = backgroundColor,
                            textColor = textColor,
                            onClick = { onButtonClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.CalculatorButton(
    text: String,
    modifier: Modifier,
    backgroundColor: Color,
    textColor: Color = WhiteText,
    fontSize: TextUnit = 32.sp,
    size: Dp = 80.dp,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Text(text = text, fontSize = fontSize, color = textColor, fontWeight = FontWeight.Medium)
    }
}