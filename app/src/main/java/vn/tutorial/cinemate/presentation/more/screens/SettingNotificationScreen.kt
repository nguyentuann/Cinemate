package vn.tutorial.cinemate.presentation.more.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingNotificationScreen(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isEnabled by viewModel.isNotificationEnabled.collectAsState()
    var showPermissionDialog by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Launcher xin quyền
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateNotificationEnabled(isGranted)
        if (isGranted) {
            Toast.makeText(
                context, R.string.access_notifications, Toast.LENGTH_SHORT
            ).show()
        } else {
            // Hiện dialog hướng dẫn người dùng bật lại quyền
            showPermissionDialog = true
        }
    }

    // Kiểm tra quyền khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.checkNotificationPermission()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkNotificationPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Scaffold(
        topBar = {
            TopAppBarWithBack(title = stringResource(R.string.notification_management))
        }) { padding ->
        Column(
            modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.receive_notifications),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    modifier = Modifier.scale(0.8f),
                    checked = isEnabled,
                    onCheckedChange = { checked ->
                        if (checked) {
                            viewModel.requestPermission(context, permissionLauncher)
                        } else {
                            // Gợi ý mở cài đặt để tắt thủ công
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                    })
            }
        }
        if (showPermissionDialog) {
            OpenSettingDialog(
                onDismiss = { showPermissionDialog = false },
                context = context
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun OpenSettingDialog(
    onDismiss: () -> Unit,
    context: Context
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.deny_notifications),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
    }, text = {
        Text(
            text = stringResource(R.string.deny_notifications_desc),

            )
    }, confirmButton = {
        TextButton(onClick = {
            onDismiss()
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }) {
            Text(stringResource(R.string.open_settings))
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.cancel))
        }
    })
}


