package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.presentation.more.components.AgeLimitSelector
import vn.tutorial.cinemate.presentation.more.components.SettingSwitchItem
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.components.WatchTimeSelector
import vn.tutorial.cinemate.presentation.more.viewModels.ChildrenModeViewModel

@Composable
fun ChildrenModeScreen(
    viewModel: ChildrenModeViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getChildrenMode("")
    }
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBarWithBack("Children Mode")
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Ở Chế độ Trẻ em, bạn có thể giới hạn tối đa thời lượng xem, lựa chọn phim theo chủ đề phù hợp và kiểm soát nội dung theo độ tuổi.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(36.dp))

            SettingSwitchItem(
                title = "Kích hoạt",
                isChecked = state.isEnable,
                onCheckedChange = { viewModel.enable() }
            )

            Spacer(Modifier.height(36.dp))

            Text(
                "Giới hạn độ tuổi",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "Chọn độ tuổi phù hợp để giới hạn nội dung cho con bạn",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(36.dp))

            AgeLimitSelector(
                selected = state.selectedAgeLimit,
                onSelect = { viewModel.selectAgeLimit(it) }
            )

            Spacer(Modifier.height(36.dp))

            Text(
                "Thời gian xem tối đa",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "Đặt thời gian xem tối đa hằng ngày cho con bạn.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            WatchTimeSelector(
                selected = state.selectedWatchTime,
                onSelect = { viewModel.selectWatchTime(it) }
            )
            Spacer(modifier = Modifier.weight(1f))
            CommonButton(
                title = stringResource(R.string.continue_text),
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.setChildrenMode()
                }

            )
        }

        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error,
        ) {
            viewModel.clearError()
        }
    }
}
