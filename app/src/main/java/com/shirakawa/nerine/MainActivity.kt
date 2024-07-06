package com.shirakawa.nerine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
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
                    ColorPickerApp()
                }
            }
        }
    }
}

@Composable
fun ColorPickerApp() {
    var selectedColor by remember { mutableStateOf(Color.White) }
    var savedColors by remember { mutableStateOf(listOf<Color>()) }
    var showColorDetails by remember { mutableStateOf(false) }
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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    selectedColor = getColorAtTouchPosition(imageBitmap)
                    showColorDetails = true
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ColorPreview(selectedColor, showColorDetails) { showColorDetails = false }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { savedColors = savedColors + selectedColor }) {
                Icon(Icons.Default.Add, contentDescription = "Save color")
                Text("Save Color")
            }

            Button(onClick = { savedColors = emptyList() }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear colors")
                Text("Clear All")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Saved Colors:", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(8.dp))

        SavedColorsPalette(savedColors) { selectedColor = it }
    }
}

@Composable
fun ColorPreview(color: Color, showDetails: Boolean, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
    )

    if (showDetails) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Color Details") },
            text = {
                Column {
                    Text("Hex: ${color.toHexString()}")
                    Text("RGB: ${color.red}, ${color.green}, ${color.blue}")
                    Text("HSV: ${color.toHSV()}")
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SavedColorsPalette(colors: List<Color>, onColorClick: (Color) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colors) { color ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    .clickable { onColorClick(color) }
            )
        }
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

fun Color.toHSV(): String {
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt(),
        hsv
    )
    return String.format("%.0f°, %.0f%%, %.0f%%", hsv[0], hsv[1] * 100, hsv[2] * 100)
}
