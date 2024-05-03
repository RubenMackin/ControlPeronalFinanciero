package com.rubenmackin.controlperonalfinanciero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rubenmackin.controlperonalfinanciero.presentation.HomeScreen
import com.rubenmackin.controlperonalfinanciero.presentation.HomeViewModel
import com.rubenmackin.controlperonalfinanciero.ui.theme.ControlPeronalFinancieroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControlPeronalFinancieroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val homeViewModel by viewModels<HomeViewModel>()
                    HomeScreen(homeViewModel)
                }
            }
        }
    }
}