package com.mrntlu.PassVault.ui.views

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.mrntlu.PassVault.R

@Composable
fun PolicyScreen(
    isTerms: Boolean
) {
    val textColor = MaterialTheme.colorScheme.onBackground

    AndroidView(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(6.dp),
        factory = {

            TextView(it).apply {
                text = if (isTerms) {
                    HtmlCompat.fromHtml(it.getString(R.string.terms_conditions), HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else {
                    HtmlCompat.fromHtml(it.getString(R.string.privacy_policy), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }

                movementMethod = LinkMovementMethod.getInstance()
                isVerticalScrollBarEnabled = true
                setTextIsSelectable(true)
                setTextColor(textColor.toArgb())
            }
        }
    )
}

@Preview
@Composable
fun PolicyScreenPreview() {
    PolicyScreen(isTerms = true)
}