package vn.tutorial.cinemate.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.SearchBar
import vn.tutorial.cinemate.domain.model.filmMock
import vn.tutorial.cinemate.presentation.more.components.CardFilmItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var query by remember { mutableStateOf("") }
    val films = remember { mutableStateListOf(filmMock, filmMock, filmMock) }


    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                value = query,
                onChange = {
                    query = it
                },
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
            ) {
                item {
                    Text(
                        stringResource(R.string.result),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(films) { film ->
                    CardFilmItem(film, isSearch = true)
                }
            }
        }
    }
}
