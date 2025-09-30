package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.domain.model.filmMock
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.presentation.more.components.CardFilmItem

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    title: String = "Favorite",
) {

    val todayFilms = remember { mutableStateListOf(filmMock, filmMock) }
    val yesterdayFilms = remember { mutableStateListOf(filmMock) }
    val lastWeekFilms = remember { mutableStateListOf(filmMock, filmMock, filmMock) }

    val mapFavoriteFilm = mapOf(
        "Today" to todayFilms,
        "Yesterday" to yesterdayFilms,
        "Last Week" to lastWeekFilms,
    )

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = title
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            mapFavoriteFilm.forEach { (title, filmsMutable) ->
                item {
                    Text(title, style = MaterialTheme.typography.titleSmall)
                }
                items(filmsMutable) { film ->
                    CardFilmItem(
                        film = film,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithBack(
    title: String,
) {
    val navController = LocalNavController.current
    TopAppBar(
        modifier = Modifier.background(Color.Black),
        title = {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    AppIcons.back(),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

