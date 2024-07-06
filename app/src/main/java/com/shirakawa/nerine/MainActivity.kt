package com.shirakawa.nerine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.shirakawa.nerine.ui.theme.MyComposeApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorPickerScreen()
                }
            }
        }
    }
}

@Composable
fun ColorPickerScreen() {
    var selectedColor by remember { mutableStateOf(Color.White) }
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.sample_image)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Sample Image",
            modifier = Modifier
                .size(300.dp)
                .clickable { selectedColor = getColorAtTouchPosition(imageBitmap) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(selectedColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected Color: ${selectedColor.toHexString()}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun getColorAtTouchPosition(imageBitmap: ImageBitmap): Color {
    val bitmap = imageBitmap.asAndroidBitmap()
    val x = (Math.random() * bitmap.width).toInt()
    val y = (Math.random() * bitmap.height).toInt()
    val pixel = bitmap.getPixel(x, y)
    return Color(pixel)
}

fun Color.toHexString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}