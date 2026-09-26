package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.SpacePanelLight

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = SpacePanelBorder,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 14.dp,
    glowAccent: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            SpacePanelLight.copy(alpha = 0.85f),
            SpacePanelBg.copy(alpha = 0.95f)
        )
    )

    Surface(
        modifier = modifier
            .clip(shape),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            borderWidth,
            glowAccent?.copy(alpha = 0.45f) ?: borderColor
        ),
        onClick = onClick ?: {},
        enabled = onClick != null
    ) {
        Box(
            modifier = Modifier
                .background(backgroundBrush)
                .fillMaxWidth()
                .padding(14.dp),
            content = content
        )
    }
}
