package vn.tutorial.cinemate.presentation.more.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.components.AsyncImageWithReplace
import vn.tutorial.cinemate.core.helper.uriToFile
import java.io.File
import vn.tutorial.cinemate.R

@Composable
fun AvatarPicker(
    avatarUrl: String?,
    onImageSelected: (File) -> Unit
) {
    val context = LocalContext.current

    // Launcher để chọn ảnh từ gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Chuyển Uri -> File
            val file = uriToFile(it, context)
            file?.let { f -> onImageSelected(f) }
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable {
                launcher.launch("image/*") // mở gallery chọn ảnh
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImageWithReplace(
            model = avatarUrl ?: "",
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            imgReplace = R.drawable.adult
        )
    }
}
