package com.vehicle.owner.core.location

import android.Manifest
import android.content.Intent
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.vehicle.owner.core.location.PermissionRationaleDialog
import com.vehicle.owner.core.location.PermissionRequestButton
import com.vehicle.owner.core.location.RationaleState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(fineLocationPermissionState: MultiplePermissionsState) {
    val context = LocalContext.current

    // Approximate location access is sufficient for most of use cases
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    // When precision is important request both permissions but make sure to handle the case where
    // the user only grants ACCESS_COARSE_LOCATION
    /*val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )*/



    // In really rare use cases, accessing background location might be needed.
    /*val bgLocationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )
    } else {
        rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }*/

    // Keeps track of the rationale dialog state, needed when the user requires further rationale
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Show rationale dialog when needed
            rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

            /*PermissionRequestButton(
                isGranted = locationPermissionState.status.isGranted,
                title = "Approximate location access",
            ) {
                if (locationPermissionState.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Request approximate location access",
                        "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            locationPermissionState.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            }*/

            PermissionRequestButton(
                isGranted = fineLocationPermissionState.allPermissionsGranted,
                title = "Precise location access",
            ) {
                fineLocationPermissionState.launchMultiplePermissionRequest()
                /*if (fineLocationPermissionState.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Request Precise Location",
                        "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            fineLocationPermissionState.launchMultiplePermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    fineLocationPermissionState.launchMultiplePermissionRequest()
                }*/
            }

            // Background location permission needed from Android Q,
            // before Android Q, granting Fine or Coarse location access automatically grants Background
            // location access
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PermissionRequestButton(
                    isGranted = bgLocationPermissionState.status.isGranted,
                    title = "Background location access",
                ) {
                    if (locationPermissionState.status.isGranted || fineLocationPermissionState.allPermissionsGranted) {
                        if (bgLocationPermissionState.status.shouldShowRationale) {
                            rationaleState = RationaleState(
                                "Request background location",
                                "In order to use this feature please grant access by accepting " + "the background location permission dialog." + "\n\nWould you like to continue?",
                            ) { proceed ->
                                if (proceed) {
                                    bgLocationPermissionState.launchPermissionRequest()
                                }
                                rationaleState = null
                            }
                        } else {
                            bgLocationPermissionState.launchPermissionRequest()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please grant either Approximate location access permission or Fine" + "location access permission",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }*/
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS)) },
        ) {
            Icon(Icons.Outlined.Settings, "Location Settings")
        }
    }
}