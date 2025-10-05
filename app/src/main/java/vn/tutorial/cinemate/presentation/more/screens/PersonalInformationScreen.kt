package vn.tutorial.cinemate.presentation.more.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack

data class UserUiModel(
    val email: String,
    val fullName: String,
    val dateOfBirth: String,
    val role: String,
    val status: String,
    val avatarUrl: String? = null
)

@Composable
fun PersonalInformationScreen(
    user: UserUiModel = UserUiModel(
        email = "example@gmail.com",
        fullName = "Vincent Nhat Tuan",
        dateOfBirth = "2004-02-24",
        role = "User",
        status = "Active",
        avatarUrl = null
    ),
    onSave: (String, String) -> Unit = { _, _ -> }
) {
    var fullName by remember { mutableStateOf(user.fullName) }
    var birthday by remember { mutableStateOf(user.dateOfBirth) }

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = stringResource(R.string.information)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable {
                    },
                contentAlignment = Alignment.Center
            ) {
                if (user.avatarUrl != null) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Email (chỉ đọc)
            OutlinedTextField(
                textStyle = MaterialTheme.typography.bodyMedium,
                value = user.email,
                onValueChange = {},
                readOnly = true,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(12.dp))

            // Full name (chỉnh sửa được)
            OutlinedTextField(
                textStyle = MaterialTheme.typography.bodyMedium,
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Họ và tên") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = birthday,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ngày sinh") },
                leadingIcon = { Icon(Icons.Default.DateRange, null) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            // Nút lưu thay đổi
            CommonButton(
                title = "Save",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSave(fullName, birthday)
                }
            )
        }
    }
}


