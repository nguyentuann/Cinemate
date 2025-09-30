package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.data.listOptions
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.presentation.more.components.MoreItem

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppBar()
        },
    ) {
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



            LazyColumn {
                items(listOptions) { option ->
                    MoreItem(
                        title = stringResource(option.titleRes),
                        icon = option.icon,
                        onClick = option.action
                    )
                }
            }
        }
    }
}
