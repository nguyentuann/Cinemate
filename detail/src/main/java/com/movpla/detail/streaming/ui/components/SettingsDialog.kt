package com.movpla.detail.streaming.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.movpla.detail.streaming.quality.QualityOption

enum class SettingsTab {
    QUALITY, SPEED, SUBTITLES
}

@Composable
fun SettingsDialog(
    qualities: List<QualityOption>,
    currentQuality: QualityOption?,
    playbackSpeed: Float,
    subtitles: List<String>,
    currentSubtitle: String?,
    onQualitySelected: (QualityOption?) -> Unit,
    onSpeedSelected: (Float) -> Unit,
    onSubtitleSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(SettingsTab.QUALITY) }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            color = Color(0xFF1C1C1C),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color(0xFF2C2C2C)
                ) {
                    Tab(
                        selected = selectedTab == SettingsTab.QUALITY,
                        onClick = { selectedTab = SettingsTab.QUALITY },
                        text = { Text("Quality") }
                    )
                    Tab(
                        selected = selectedTab == SettingsTab.SPEED,
                        onClick = { selectedTab = SettingsTab.SPEED },
                        text = { Text("Speed") }
                    )
                    Tab(
                        selected = selectedTab == SettingsTab.SUBTITLES,
                        onClick = { selectedTab = SettingsTab.SUBTITLES },
                        text = { Text("Subtitles") }
                    )
                }
                
                // Content
                when (selectedTab) {
                    SettingsTab.QUALITY -> QualityList(
                        qualities = qualities,
                        currentQuality = currentQuality,
                        onQualitySelected = {
                            onQualitySelected(it)
                            onDismiss()
                        }
                    )
                    SettingsTab.SPEED -> SpeedList(
                        currentSpeed = playbackSpeed,
                        onSpeedSelected = {
                            onSpeedSelected(it)
                            onDismiss()
                        }
                    )
                    SettingsTab.SUBTITLES -> SubtitleList(
                        subtitles = subtitles,
                        currentSubtitle = currentSubtitle,
                        onSubtitleSelected = {
                            onSubtitleSelected(it)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QualityList(
    qualities: List<QualityOption>,
    currentQuality: QualityOption?,
    onQualitySelected: (QualityOption?) -> Unit
) {
    LazyColumn {
        item {
            SettingItem(
                label = "Auto",
                isSelected = currentQuality == null,
                onClick = { onQualitySelected(null) }
            )
        }
        
        items(qualities) { quality ->
            SettingItem(
                label = quality.label,
                isSelected = currentQuality?.height == quality.height,
                onClick = { onQualitySelected(quality) }
            )
        }
    }
}

@Composable
private fun SpeedList(
    currentSpeed: Float,
    onSpeedSelected: (Float) -> Unit
) {
    val speeds = listOf(0.25f, 0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f)
    
    LazyColumn {
        items(speeds) { speed ->
            SettingItem(
                label = "${speed}x",
                isSelected = currentSpeed == speed,
                onClick = { onSpeedSelected(speed) }
            )
        }
    }
}

@Composable
private fun SubtitleList(
    subtitles: List<String>,
    currentSubtitle: String?,
    onSubtitleSelected: (String?) -> Unit
) {
    LazyColumn {
        item {
            SettingItem(
                label = "Off",
                isSelected = currentSubtitle == null,
                onClick = { onSubtitleSelected(null) }
            )
        }
        
        items(subtitles) { subtitle ->
            SettingItem(
                label = subtitle,
                isSelected = currentSubtitle == subtitle,
                onClick = { onSubtitleSelected(subtitle) }
            )
        }
    }
}

@Composable
private fun SettingItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.Red
            )
        }
    }
}
