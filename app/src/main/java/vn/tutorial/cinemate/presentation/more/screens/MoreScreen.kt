package vn.tutorial.cinemate.presentation.more.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.ConfirmationDialog
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.data.listOptions
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.components.MoreItem
import vn.tutorial.cinemate.presentation.more.viewModels.SignOutViewModel
import vn.tutorial.cinemate.presentation.more.viewModels.SubscriptionPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    viewModel: SignOutViewModel = hiltViewModel(),
    subscriptionPlanViewModel: SubscriptionPlanViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    val navController = LocalNavController.current
    val showDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.information),
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                items(listOptions) { option ->

                    if (option.route == Route.Subscription.route) {
                        MoreItem(
                            title = stringResource(option.titleRes),
                            icon = option.icon,
                            onClick = {
                                subscriptionPlanViewModel.getCurrentSubscription {
                                    if (it == null) {
                                        navController.navigate(option.route)
                                    } else {
                                        navController.navigate(
                                            Route.CurrentPlan.createRoute(
                                                planId = it.id
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        MoreItem(
                            title = stringResource(option.titleRes),
                            icon = option.icon,
                            onClick = {
                                navController.navigate(option.route)
                            }
                        )
                    }
                }

                item {
                    MoreItem(
                        title = stringResource(R.string.help_reply),
                        icon = Icons.Default.Email,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:giabao7112004@gmail.com".toUri()
                                putExtra(Intent.EXTRA_SUBJECT, "Contribute for app")
                            }

                            try {
                                context.startActivity(intent)

                            } catch (e: Exception) {
                                LogUtil(e.toString())
                            }

                        }
                    )
                }

                item {
                    MoreItem(
                        title = stringResource(R.string.sign_out),
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        onClick = {
                            showDialog.value = true
                        }
                    )
                }
            }
            if (showDialog.value) {
                ConfirmationDialog(
                    title = stringResource(R.string.sign_out),
                    message = stringResource(R.string.sign_out_confirm),
                    onDismiss = {
                        showDialog.value = false
                    },
                    onConfirm = {
                        showDialog.value = false
                        viewModel.signOut(
                            onSuccess = {
                                navController.navigate(Route.Started.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                )
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