package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.MainApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FitnessViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val fitnessViewModel: FitnessViewModel = viewModel()
            val currentThemeMode by fitnessViewModel.themeMode.collectAsState()

            MyApplicationTheme(themeMode = currentThemeMode) {
                MainApp(viewModel = fitnessViewModel)
            }
        }
    }
}


