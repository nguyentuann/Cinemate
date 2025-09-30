package vn.tutorial.cinemate.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.InteractionButton
import vn.tutorial.cinemate.common.icons.AppIcons

@Composable
fun InteractionBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        InteractionButton(
            icon =  AppIcons.add(),
            title = stringResource(R.string.my_list),
            onClick = {}
        )
        InteractionButton(
            icon = AppIcons.rate(),
            title = stringResource(R.string.rate),
            onClick = {}
        )
        InteractionButton(
            icon = AppIcons.share(),
            title = stringResource(R.string.share),
            onClick = {}
        )
    }
}