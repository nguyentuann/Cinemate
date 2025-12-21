package vn.tutorial.cinemate.presentation.more.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.presentation.more.components.SubscriptionCard
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.SubscriptionPlanViewModel
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    title: String = "Subscription",
    viewModel: SubscriptionPlanViewModel = hiltViewModel()
) {
     val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getSubscriptionPlans()
    }
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.package_management),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
//            Text(
//                text = stringResource(R.string.choose_your_plan),
//                color = MaterialTheme.colorScheme.onBackground,
//                style = MaterialTheme.typography.titleSmall,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .padding(bottom = 16.dp)
//                    .fillMaxWidth()
//            )

            LazyColumn(
                modifier = Modifier.weight(1f).padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.plans) { plan ->
                    SubscriptionCard(
                        plan,
                        isSelected = (plan.id == state.selectedPlanId),
                        onSelect = viewModel::selectPlan
                    )
                }
                item {
                    CommonButton(
                        title = stringResource(R.string.continue_text),
                        modifier = Modifier
                            .fillMaxWidth().padding(vertical = 16.dp),
                        onClick = {
                            viewModel.paySubscription{url ->
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            }
                        }
                    )
                }
            }
        }

        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error,
        ) {
            viewModel.clearError()
        }
    }
}

