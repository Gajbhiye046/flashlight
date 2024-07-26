package com.example.flashlight


import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import com.example.flashlight.ui.theme.FlashlightTheme


class MainActivity : ComponentActivity() {
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FlashlightTheme {
                MyApp()
            }
        }
    }
}


@Composable
fun MyApp() {
    val context = LocalContext.current
    val cameraManager = remember { ContextCompat.getSystemService(context, CameraManager::class.java) }
    val isFlashOn = remember { mutableStateOf(false) }
    val appBackgroundColor = if (isFlashOn.value) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = appBackgroundColor
    ) {
        FlashLightScreen(cameraManager, isFlashOn.value, onToggle = { isFlashOn.value = it })
    }
}
@Composable
fun FlashLightScreen(cameraManager: CameraManager?, isFlashOn: Boolean, onToggle: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashlightToggleButton(isFlashOn) { newState ->
            onToggle(newState)
            toggleFlashlight(cameraManager, newState)
        }
    }
}

@Composable
fun FlashlightToggleButton(isOn: Boolean, onClick: (Boolean) -> Unit) {
    val backgroundColor = if (isOn) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val textColor = if (isOn) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
    val buttonText = if (isOn) "OFF" else "ON"

    Button(
        onClick = { onClick(!isOn) },
        modifier = Modifier
            .size(60.dp)
            .background(backgroundColor),
        colors = ButtonDefaults.buttonColors(),
        contentPadding = PaddingValues(8.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        Text(
            text = buttonText,
            color = textColor,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}

private fun toggleFlashlight(cameraManager: CameraManager?, isOn: Boolean) {
    cameraManager ?: return
    try {
        val cameraId = cameraManager.cameraIdList[0]
        cameraManager.setTorchMode(cameraId, isOn)
    } catch (e: CameraAccessException) {
        // Handle camera access exception
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}

