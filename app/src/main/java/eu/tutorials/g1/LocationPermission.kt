package eu.tutorials.g1

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    content: @Composable () -> Unit
) {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissions.allPermissionsGranted) {
        content()
    } else {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Location Permission Required") },
            text = { Text("This app needs location permission to show your current location on the map.") },
            confirmButton = {
                Button(onClick = { locationPermissions.launchMultiplePermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        )
    }
} 