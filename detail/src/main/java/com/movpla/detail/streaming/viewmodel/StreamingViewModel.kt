package com.movpla.detail.streaming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movpla.detail.streaming.data.model.StreamingData
import com.movpla.detail.streaming.data.model.VideoSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class StreamingUiState {
    object Loading : StreamingUiState()
    data class Success(val streamingData: StreamingData) : StreamingUiState()
    data class Error(val message: String) : StreamingUiState()
}

class StreamingViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow<StreamingUiState>(StreamingUiState.Loading)
    val uiState: StateFlow<StreamingUiState> = _uiState.asStateFlow()
    
    fun loadStreamingData(movieId: String, episodeId: String? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = StreamingUiState.Loading
                
                // TODO: Implement actual API call
                // val response = streamingRepository.getStreamingData(movieId, episodeId)
                
                // Mock data for now
                val mockData = StreamingData(
                    sources = listOf(
                        VideoSource(
                            url = "https://example.com/stream.m3u8",
                            type = "application/x-mpegURL",
                            quality = "Auto"
                        )
                    ),
                    subtitles = emptyList()
                )
                
                _uiState.value = StreamingUiState.Success(mockData)
            } catch (e: Exception) {
                _uiState.value = StreamingUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
