package vn.tutorial.cinemate.presentation.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.Refreshable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val importantNotifications = state.importantNotifications
    val otherNotifications = state.otherNotifications

    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notification),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        Refreshable(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.refresh()
                delay(1000)
                isRefreshing = false
            },
        ) {
            LazyColumn(
                modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {

                if (importantNotifications.isNotEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.important),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    items(importantNotifications) { item ->
                        NotificationItem(item = item)
                    }
                }

                if (otherNotifications.isNotEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.other),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    items(otherNotifications) { item ->
                        NotificationItem(item = item)
                    }
                }
            }
            LoadingAndError(
                isLoading = state.isLoading,
                error = state.error,
                onErrorDismiss = {
                    viewModel.clearError()
                }
            )
        }
    }
}