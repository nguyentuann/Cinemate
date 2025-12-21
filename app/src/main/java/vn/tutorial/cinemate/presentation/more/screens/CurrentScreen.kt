package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.ConfirmationDialog
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.domain.model.MemberModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.components.InviteMemberBottomSheet
import vn.tutorial.cinemate.presentation.more.components.SubscriptionCard
import vn.tutorial.cinemate.presentation.more.viewModels.CurrentPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentScreen(
    title: String = "Current Plan",
    subscriptionId: String,
    planId: String,
    viewModel: CurrentPlanViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState().value
    var showSheet by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val navController = LocalNavController.current


    viewModel.loadData(planId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
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

        if (showSheet) {
            InviteMemberBottomSheet(
                onDismiss = { showSheet = false },
                onSend = { email, type ->
                    showSheet = false
                    viewModel.inviteMember(email, type.name)
                },
                viewModel = viewModel
            )
        }

        if (showCancelDialog) {
            ConfirmationDialog(
                title = "Cancel Subscription",
                message = "Are you sure you want to cancel your subscription?",
                onConfirm = {
                    showCancelDialog = false
                    viewModel.cancelPlan(
                        subscriptionId = subscriptionId,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                },
                onDismiss = {
                    showCancelDialog = false
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

                CommonButton(
                    title = "Cancel Plan",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        showCancelDialog = true
                    }
                )

            }

            if (state.currentPlan?.name == "Family") {
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
                            MemberInPlan(
                                member = it,
                                navController = navController,
                                onRemove = { memberUserId ->
                                    viewModel.removeMember(memberUserId)
                                }
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
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
}

@Composable
fun MemberInPlan(
    member: MemberModel, 
    navController: NavHostController,
    onRemove: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        ConfirmationDialog(
            title = "Delete Member",
            message = "Are you sure you want to delete this member?",
            onConfirm = {
                showDeleteDialog = false
                onRemove(member.userId)
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }


    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(Styles.ShapeStyles.mediumCorner)
    ) {

        Image(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    if (member.isKid) {
                        navController.navigate(
                            Route.ChildrenMode.createRoute(member.userId)
                        )
                    }
                },
            painter = painterResource(
                id = if (member.isKid) R.drawable.kid else R.drawable.adult
            ),
            contentDescription = null
        )

        IconButton(
            onClick = { showDeleteDialog = false },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Remove member",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}