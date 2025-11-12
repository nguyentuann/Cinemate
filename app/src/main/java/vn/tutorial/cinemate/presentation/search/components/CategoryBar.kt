package vn.tutorial.cinemate.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.presentation.search.viewModels.CategoryViewModel

@Composable
fun CategoryBar(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onCategorySelected: (CategoryModel) -> Unit
) {
    val state = categoryViewModel.state.collectAsState()

    if (state.value.categories.isNotEmpty()) {
        var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
        LazyRow(
            modifier = modifier.padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.value.categories) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        onCategorySelected(category)
                    }
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 32.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        shape = Styles.ShapeStyles.mediumCorner,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
