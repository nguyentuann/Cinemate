package vn.tutorial.cinemate.domain.model

data class CategoryModel(
    val id: String,
    val name: String,
    val description: String? = null
)
