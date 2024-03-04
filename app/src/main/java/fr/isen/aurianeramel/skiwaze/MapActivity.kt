package fr.isen.aurianeramel.skiwaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity


class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Text("salut")
            ZoomableImage(drawableId = R.drawable.ski_image)

        }
    }
}

@Composable
fun ZoomableImage(
    drawableId: Int,
    modifier: Modifier = Modifier
) {
    val painter = rememberImagePainter(drawableId)

    val scale = remember { mutableStateOf(1f) }
    val offset = remember { mutableStateOf(Offset.Zero) }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale.value *= zoom
                    offset.value += pan
                }
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
    )
}
