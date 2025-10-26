package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.data.listOptions
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.components.MoreItem
import vn.tutorial.cinemate.presentation.more.viewModels.SignOutViewModel

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    viewModel: SignOutViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    val navController = LocalNavController.current
    val showDialog = remember { mutableStateOf(false) }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            LazyRow {
                items(5) {
                    Image(
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 16.dp)
                            .clip(Styles.ShapeStyles.mediumCorner),
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                    )
                }
            }
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.information),
                style = MaterialTheme.typography.titleSmall
            )

            LazyColumn(
                modifier = Modifier.padding(bottom = 120.dp)
            ) {
                items(listOptions) { option ->
                    MoreItem(
                        title = stringResource(option.titleRes),
                        icon = option.icon,
                        onClick = {
                            navController.navigate(option.route)
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
                ShowSignOutDialog(
                    onDismiss = {
                        showDialog.value = false
                    },
                    onLogOut = {
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

@Composable
private fun ShowSignOutDialog(
    onDismiss: () -> Unit = {},
    onLogOut: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.sign_out),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.sign_out_confirm),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Text(
                "OK",
                Modifier
                    .clickable(
                        onClick = onLogOut
                    )
                    .padding(horizontal = 16.dp)
            )

        },
        dismissButton = {
            Text(
                stringResource(R.string.cancel),
                Modifier.clickable(
                    onClick = onDismiss
                )
            )
        }
    )
}
