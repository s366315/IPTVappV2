@file:OptIn(ExperimentalMaterial3Api::class)

package com.iptv.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposablePreview()
        }
    }
}

@Preview
@Preview(showBackground = true)
@Composable
fun ComposablePreview() {
    MaterialTheme(darkColorScheme()) {
        TvScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TvScreen() {
    Scaffold(
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = "Body content"
                    )
                }
            )
        })
}