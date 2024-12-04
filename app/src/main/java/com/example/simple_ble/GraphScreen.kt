import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simple_ble.BleViewModel

@Composable
fun GraphScreen(bleViewModel: BleViewModel, navController: NavController) {
    val receivedData = bleViewModel.receivedData.value
    val graphData = remember { mutableStateListOf<Pair<Long, Int>>() }

    // Update graph data whenever new data is received
    LaunchedEffect(receivedData) {
        if (receivedData != null) {
            val currentTime = System.currentTimeMillis()
            graphData.add(Pair(currentTime, receivedData.toInt()))
        }
    }

    // Extract color for use in Canvas
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Data Graph", style = MaterialTheme.typography.headlineMedium)

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            val spacing = 20.dp.toPx()
            val points = graphData.takeLast(20) // Only take the last 20 points for simplicity
            val maxValue = points.maxOfOrNull { it.second } ?: 1

            points.forEachIndexed { index, pair ->
                val x = spacing * index
                val y = size.height - (pair.second / maxValue.toFloat()) * size.height
                drawCircle(
                    color = primaryColor,
                    center = Offset(x, y),
                    radius = 5f
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            bleViewModel.disconnect()
            navController.navigate("home")
        }) {
            Text("Disconnect and Back to Devices")
        }
    }
}
