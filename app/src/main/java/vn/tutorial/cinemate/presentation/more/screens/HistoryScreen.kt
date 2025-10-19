package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.presentation.more.components.CardFilmItem
import vn.tutorial.cinemate.common.components.SearchBar
import vn.tutorial.cinemate.mockdata.filmMock1
import vn.tutorial.cinemate.mockdata.filmMock2
import vn.tutorial.cinemate.mockdata.filmMock3
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    title: String = "History",
) {

    val todayFilms = remember { mutableStateListOf(filmMock1, filmMock2) }
    val yesterdayFilms = remember { mutableStateListOf(filmMock1) }
    val lastWeekFilms = remember { mutableStateListOf(filmMock1, filmMock2, filmMock3) }

    val mapFavoriteFilm = mapOf(
        "Today" to todayFilms,
        "Yesterday" to yesterdayFilms,
        "Last Week" to lastWeekFilms,
    )

    var query = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = title
            )
        }
    ) {
        // todo thanh tìm kiếm
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(it)
                .fillMaxSize()
        ) {
            SearchBar(
                value = query.value,
                onChange = { newValue ->
                    query.value = newValue
                },
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn {
                mapFavoriteFilm.forEach { (title, filmsMutable) ->
                    item {
                        Text(title, style = MaterialTheme.typography.titleSmall)
                    }
                    items(filmsMutable) { film ->
                        CardFilmItem(
                            film = film,
                            isHistory = true
                        )
                    }
                }
            }
        }
    }
}


