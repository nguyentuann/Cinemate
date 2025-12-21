package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.components.InviteMemberBottomSheet
import vn.tutorial.cinemate.presentation.more.components.SubscriptionCard
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.CurrentPlanViewModel


@Composable
fun CurrentScreen(
    title: String = "Current Plan",
    planId: String,
    viewModel: CurrentPlanViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState().value
    var showSheet by remember { mutableStateOf(false) }
    val navController = LocalNavController.current


    viewModel.loadData(planId)

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = title
            )
        }
    ) {

        if (showSheet) {
            InviteMemberBottomSheet(
                onDismiss = { showSheet = false },
                onSend = { email, type ->
                    showSheet = false
                    viewModel.inviteMember(email, type.name)
                }
            )
        }


        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            state.currentPlan?.let {
                SubscriptionCard(
                    plan = it,
                    isSelected = true
                )
            }

            Text(
                text = "Current Members",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            LazyRow {
                items(state.currentMembers) {
                    if (it.isOwner == false) {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 16.dp).clickable {
                                    if (it.isKid) {
                                        navController.navigate(Route.ChildrenMode.createRoute(it.id))
                                    }
                                }
                                .size(80.dp)
                                .clip(Styles.ShapeStyles.mediumCorner),
                            painter = painterResource(
                                id = if (it.isKid) R.drawable.kid else R.drawable.adult
                            ),
                            contentDescription = null,
                        )
                    }
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 16.dp)
                            .size(80.dp)
                            .clip(Styles.ShapeStyles.mediumCorner)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = Styles.ShapeStyles.mediumCorner
                            )
                            .padding(8.dp)
                            .clickable {
                                showSheet = true
                            },
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}