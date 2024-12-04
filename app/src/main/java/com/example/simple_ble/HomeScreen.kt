import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simple_ble.BleViewModel

@Composable
fun HomeScreen(bleViewModel: BleViewModel, navController: NavController) {
    var showConnectingDialog by remember { mutableStateOf(false) }
    val isConnected = bleViewModel.isConnected.value

    // Start scanning for BLE devices on first composition
    LaunchedEffect(Unit) {
        bleViewModel.startScan()
    }

    // Show connecting dialog
    if (showConnectingDialog && !isConnected) {
        AlertDialog(
            onDismissRequest = { showConnectingDialog = false },
            title = { Text("Connecting") },
            text = { Text("Connecting to the selected BLE device...") },
            confirmButton = {
                Button(onClick = { showConnectingDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text(text = "Available Devices", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(bleViewModel.scanResults) { scannedDevice ->
                DeviceItem(device = scannedDevice.device, rssi = scannedDevice.rssi) {
                    showConnectingDialog = true
                    bleViewModel.connectToDevice(scannedDevice.device)
                }
                Divider()
            }
        }

        if (isConnected) {
            Button(onClick = {
                showConnectingDialog = false
                navController.navigate("graph")
            }) {
                Text("Go to Graph")
            }
        }
    }
}

@Composable
fun DeviceItem(device: BluetoothDevice, rssi: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(text = device.name ?: "Unnamed Device", style = MaterialTheme.typography.bodyLarge)
        Text(text = device.address, style = MaterialTheme.typography.bodyMedium)
        Text(text = "RSSI: $rssi dBm", style = MaterialTheme.typography.bodySmall)
    }
}
