package vn.tutorial.cinemate.presentation.more.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.CommonTextField
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.core.helper.getFullAvatarUrl
import vn.tutorial.cinemate.presentation.more.components.AvatarPicker
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.ProfileViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val profile = state.profile
    var showDialog by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    if (profile.dateOfBirth != null) {
        val parsedDate = LocalDate.parse(profile.dateOfBirth, formatter).plusDays(1)
        selectedDate = parsedDate
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBarWithBack(title = stringResource(R.string.information))
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            AvatarPicker(getFullAvatarUrl(profile.avatarUrl ?: "")) { file ->
                viewModel.updateAvatar(file)
            }

            Spacer(Modifier.height(24.dp))

            // First name
            CommonTextField(
                value = profile.firstName ?: "",
                onValueChange = {
                    viewModel.updateFirstName(it)
                },
                placeholder = "First Name",
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = profile.firstName.isNullOrBlank(),
                errorMessage = "First name cannot be empty",
                label = "First Name",
                testTag = "first_name_text_field",
                errorTestTag = "first_name_error_message"
            )

            Spacer(Modifier.height(12.dp))

            // Last name
            CommonTextField(
                value = profile.lastName ?: "",
                onValueChange = {
                    viewModel.updateLastName(it)
                },
                placeholder = "Last Name",
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = profile.lastName.isNullOrBlank(),
                errorMessage = "Last name cannot be empty",
                label = "Last Name",
                testTag = "last_name_text_field",
                errorTestTag = "last_name_error_message"
            )

            Spacer(Modifier.height(12.dp))

            // Birthday picker
            CommonTextField(
                value = profile.dateOfBirth ?: "",
                onValueChange = {},
                placeholder = "Date of birth",
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "date_of_birth_icon_button"
                        },
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                },
                label = "Date of birth",
                testTag = "date_of_birth_text_field",
                errorTestTag = "date_of_birth_error_message"
            )

            Spacer(Modifier.height(12.dp))

            // Gender radio buttons
            Text(
                text = "Gender",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            val genderOptions = listOf("MALE", "FEMALE", "OTHER")

            genderOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateGender(option)
                        }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (profile.gender ?: "OTHER") == option,
                        onClick = {
                            viewModel.updateGender(option)
                        }
                    )
                    Text(
                        text = option.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Save button
            CommonButton(
                title = "Save",
                testTag = "save_profile_button",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.saveProfile()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        }
        LoadingAndError(
            testTag = "profile_api_message",
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }

    if (showDialog) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    modifier = Modifier.semantics {
                        contentDescription = "confirm_date_button"
                    },
                    onClick = {
                        pickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            selectedDate = date
                            viewModel.updateDateOfBirth(
                                "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
                            )
                        }
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier.semantics {
                        contentDescription = "cancel_date_button"
                    },
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                )
            ) {
                DatePicker(
                    state = pickerState,
                )
            }
        }
    }
}


