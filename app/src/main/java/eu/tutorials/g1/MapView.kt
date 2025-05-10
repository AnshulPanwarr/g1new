package eu.tutorials.g1

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    initialLocation: LatLng = LatLng(0.0, 0.0),
    onLocationSelected: (LatLng) -> Unit
) {
    LocationPermission {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
        }

        val markerState = remember { mutableStateOf(initialLocation) }

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerState.value = latLng
                onLocationSelected(latLng)
            },
            properties = MapProperties(
                isMyLocationEnabled = true
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {
            Marker(
                state = MarkerState(position = markerState.value),
                title = "Selected Location",
                snippet = "Lat: ${markerState.value.latitude}, Lng: ${markerState.value.longitude}"
            )
        }
    }
} 