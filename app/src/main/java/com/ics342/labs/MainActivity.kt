package com.ics342.labs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.ics342.labs.ui.theme.LabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var hasPermission by remember { mutableStateOf(false) }
            var showPermissionRationale by remember { mutableStateOf(false) }

            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(contract = RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permission granted
                    hasPermission = true
                } else {
                    // Permission denied
                    showPermissionRationale = true
                }
            }

            LabsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (hasPermission) {
                            // Permission has been granted, show the LocationView
                            LocationView()
                        } else {
                            // Permission has not been granted, show the button to request permission
                            Button(onClick = {
                                checkOrRequestPermission(
                                    context = context,
                                    permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                                    launcher = launcher,
                                    permissionGranted = { hasPermission = true }
                                )
                            }) {
                                Text("Request Location Permission")
                            }
                        }

                        // Show the permission rationale dialog if the user has denied permission
                        if (showPermissionRationale) {
                            PermissionRationaleDialog(
                                onConfirm = {
                                    showPermissionRationale = false
                                    launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                                },
                                onCancel = { showPermissionRationale = false }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LocationView() {
        Text("Has Location Permission")
    }

    @Composable
    private fun PermissionRationaleDialog(
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = @Composable {
                Button(onClick = { onConfirm() }) {
                    Text("OK")
                }
            },
            dismissButton = @Composable {
                Button(onClick = { onCancel() }) {
                    Text("No")
                }
            },
            title = @Composable {
                Text("Permissions Required")
            },
            text = @Composable {
                Text("This app requires this permission to use this feature.")
            }
        )
    }

    private fun checkOrRequestPermission(
        context: Context,
        permission: String,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        permissionGranted: () -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            permissionGranted()
        } else {
            // Permission is not granted, launch the permission request
            launcher.launch(permission)
        }
    }
}
