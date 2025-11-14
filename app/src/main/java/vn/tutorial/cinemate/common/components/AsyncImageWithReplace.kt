package vn.tutorial.cinemate.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun AsyncImageWithReplace(
    modifier: Modifier = Modifier,
    model: String,
    contentDescription: String?,
    contentScale: ContentScale,
    imgReplace: Int
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(imgReplace),
        error = painterResource(imgReplace),
    )
}