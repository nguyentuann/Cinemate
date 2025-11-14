package vn.tutorial.cinemate.presentation.more.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.core.constant.enums.ThemeType
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ThemeAndLanguageScreen(
    settingsViewModel: SettingsViewModel,
) {
    var showDialogChooseLanguage = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBarWithBack(title = stringResource(R.string.theme_language))
        }) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.dark_theme),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    modifier = Modifier
                        .scale(0.8f),
                    checked = settingsViewModel.theme.collectAsState().value == ThemeType.DARK,
                    onCheckedChange = {
                        settingsViewModel.setTheme(
                            if (it) ThemeType.DARK else ThemeType.LIGHT
                        )
                    }
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            showDialogChooseLanguage.value = true
                        }
                    )
                    .padding(
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.language),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    AppIcons.arrowRight(),
                    contentDescription = null
                )
            }
        }
    }

    if (showDialogChooseLanguage.value) {
        DialogChooseLanguage(
            selectedLanguage = settingsViewModel.locale.collectAsState().value.language,
            onDismiss = {
                showDialogChooseLanguage.value = false
            },
            onLanguageSelected = { languageCode ->
                settingsViewModel.setLocale(languageCode)
            }
        )
    }
}

@Composable
private fun DialogChooseLanguage(
    selectedLanguage: String = "vi",
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.select_language),
                style = MaterialTheme.typography.titleSmall
            )
        },
        text = {
            Column {
                // Lựa chọn Tiếng Anh
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLanguageSelected("en")
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLanguage == "en",
                        onClick = {
                            onLanguageSelected("en")
                            onDismiss()
                        }
                    )
                    Text(stringResource(R.string.english))
                }

                // Lựa chọn Tiếng Việt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLanguageSelected("vi")
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLanguage == "vi",
                        onClick = {
                            onLanguageSelected("vi")
                            onDismiss()
                        }
                    )
                    Text(stringResource(R.string.vietnamese))
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}