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
import android.Manifest
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.cashcluster.collect.ui.SplashScreen
import com.cashcluster.collect.ui.OnboardingScreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.cashcluster.collect.ui.MainScreen
import com.cashcluster.collect.OnboardingPrefs

enum class AppScreen {
    Splash,
    PermissionRequest,
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

                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted: Boolean ->
                        if (isGranted) {
                            currentScreen = AppScreen.Onboarding
                        } else {
                            currentScreen = AppScreen.Onboarding
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(1500)

                    if (onboardingPrefs.isOnboardingComplete()) {
                        currentScreen = AppScreen.Main
                    } else {
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            currentScreen = AppScreen.Onboarding
                        } else {
                            currentScreen = AppScreen.PermissionRequest
                        }
                    }
                }

                when (currentScreen) {
                    AppScreen.Splash -> SplashScreen()
                    AppScreen.PermissionRequest -> {
                        AlertDialog(
                            onDismissRequest = { /* Popup dışında tıklanınca bir şey yapma veya popup'ı kapatıp onboarding'e geç */ },
                            title = { Text("İzin Gerekli") },
                            text = { Text("Uygulamanın çalışması için fotoğraflara erişim izni vermelisiniz.") },
                            confirmButton = {
                                Button(onClick = {
                                    permissionLauncher.launch(permission)
                                }) {
                                    Text("İzin Ver")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { currentScreen = AppScreen.Onboarding }) {
                                    Text("Reddet")
                                }
                            }
                        )
                    }
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