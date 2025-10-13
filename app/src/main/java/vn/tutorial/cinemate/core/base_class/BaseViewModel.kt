package vn.tutorial.cinemate.core.base_class

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.core.util.LogUtil

fun <S, P> ViewModel.executeUseCase(
    state: MutableStateFlow<S>,
    block: suspend () -> Resource<P>,
    onSuccess: (P?) -> S, // cho phép update state tùy vào data trả về
    onError: (String?) -> S, // cho phép update state khi lỗi
    onLoading: (() -> S)? = null // optional
) {
    viewModelScope.launch {
        // loading ban đầu
        onLoading?.let { state.value = it() }

        when (val result = block()) {
            is Resource.Success -> {
                state.value = onSuccess(result.data)
            }

            is Resource.Error -> {
                state.value = onError(result.message)
            }

            is Resource.Loading -> {
                onLoading?.let { state.value = it() }
            }
        }
    }
}

