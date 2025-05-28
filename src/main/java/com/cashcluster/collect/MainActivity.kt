package com.cashcluster.collect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cashcluster.collect.ui.theme.CashClusterTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.cashcluster.collect.ui.SplashScreen
import com.cashcluster.collect.ui.OnboardingScreen
import com.cashcluster.collect.ui.MainScreen
import com.cashcluster.collect.OnboardingPrefs

enum class AppScreen {
    Splash,
    Onboarding,
    Main
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashClusterTheme {
                var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Splash) }
                val context = LocalContext.current
                val onboardingPrefs = remember { OnboardingPrefs(context) }

                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(1500)

                    if (onboardingPrefs.isOnboardingComplete()) {
                        currentScreen = AppScreen.Main
                    } else {
                        currentScreen = AppScreen.Onboarding
                    }
                }

                when (currentScreen) {
                    AppScreen.Splash -> SplashScreen()
                    AppScreen.Onboarding -> {
                        OnboardingScreen(onFinish = { 
                            onboardingPrefs.setOnboardingComplete(true)
                            currentScreen = AppScreen.Main
                        })
                    }
                    AppScreen.Main -> {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CashClusterTheme {
        Greeting("Android")
    }
}