package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.usecase.history.GetDatesUseCase
import vn.tutorial.cinemate.domain.usecase.history.GetHistoryOfDateUseCase
import javax.inject.Inject
import kotlin.collections.orEmpty

data class DailyHistory(
    val date: String,
    val movies: List<MovieDetailModel> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = true
)

data class HistoryUIState(
    val dailyHistories: List<DailyHistory> = emptyList(),
    val dates: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)



@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getDatesUseCase: GetDatesUseCase,
    private val getHistoryOfDateUseCase: GetHistoryOfDateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUIState())
    val state: StateFlow<HistoryUIState> = _state


    init {
        getHistoryDates()
    }
    // Lấy danh sách ngày
    fun getHistoryDates() {
        LogUtil("call api getHistoryDates")
        executeUseCase(
            state = _state,
            block = {
                getDatesUseCase.invoke(
                    GetDatesUseCase.Params(
                        page = _state.value.dates.size / 5 + 1,
                        size = 5
                    )
                )
            },
            onSuccess = { newDates ->
                val currentDates = _state.value.dates
                val updatedDates = currentDates + newDates.orEmpty()

                val dailyHistories = updatedDates.map { date ->
                    _state.value.dailyHistories.find { it.date == date } ?: DailyHistory(date)
                }
                // Tự động load movies cho các dates mới
                newDates?.forEach { date ->
                    getHistoryMoviesByDate(date)
                }

                _state.value.copy(
                    dates = updatedDates,
                    dailyHistories = dailyHistories,
                    isLoading = false,
                    error = null
                )


            },
            onError = { errorMsg ->
                _state.value.copy(isLoading = false, error = errorMsg)
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

    // Lấy movies theo ngày, phân trang
    fun getHistoryMoviesByDate(date: String, size: Int = 10) {
        LogUtil("getHistoryMoviesByDate: $date")
        val dailyHistory =
            _state.value.dailyHistories.find { it.date == date } ?: DailyHistory(date)
        executeUseCase(
            state = _state,
            block = {
                getHistoryOfDateUseCase.invoke(
                    GetHistoryOfDateUseCase.Params(
                        date = date,
                        page = dailyHistory.page,
                        size = size
                    )
                )
            },
            onSuccess = { newMovies ->
                val updatedDailyHistory = dailyHistory.copy(
                    movies = dailyHistory.movies + newMovies.orEmpty(),
                    page = dailyHistory.page + 1,
                    hasMore = (newMovies?.size ?: 0) >= size
                )

                // Cập nhật lại list dailyHistories
                val updatedDailyHistories = _state.value.dailyHistories.map {
                    if (it.date == date) updatedDailyHistory else it
                }

                _state.value.copy(dailyHistories = updatedDailyHistories, isLoading = false)

            },
            onError = { errorMsg ->
                _state.value.copy(isLoading = false, error = errorMsg)
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

    // Load tiếp movies cho ngày
    fun loadNextPage(date: String, size: Int = 10) {
        val dailyHistory = _state.value.dailyHistories.find { it.date == date } ?: return
        if (dailyHistory.hasMore && !_state.value.isLoading) {
            getHistoryMoviesByDate(date, size)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
