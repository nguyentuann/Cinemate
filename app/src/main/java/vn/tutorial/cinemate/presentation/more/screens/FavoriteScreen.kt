import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.domain.model.filmMock
import vn.tutorial.cinemate.presentation.more.components.CardFilmItem
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    title: String = "Favorite",
) {
    val films = remember { mutableStateListOf(filmMock, filmMock, filmMock) }

    Scaffold(
        topBar = {
            TopAppBarWithBack(title)
        }
    ) {
        Column(
            modifier
                .padding(it)
                .padding(
                    horizontal = 16.dp
                )
        ) {
            LazyColumn {
                items(films) { film ->
                    CardFilmItem(film)
                }
            }
        }
    }
}