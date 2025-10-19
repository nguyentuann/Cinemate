package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Refreshable(
    isRefreshing: Boolean,
    onRefresh: suspend () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = state,
        onRefresh = {
            scope.launch { onRefresh() }
        },
        indicator = {
            // Dời xuống 56.dp (chiều cao TopBar)
            Box(
                modifier = Modifier
                    .padding(top = 56.dp).align(Alignment.TopCenter)
            ) {
                PullToRefreshDefaults.Indicator(
                    isRefreshing = isRefreshing,
                    state = state
                )
            }
        },
        modifier = modifier,
    ) {
        content()
    }
}
