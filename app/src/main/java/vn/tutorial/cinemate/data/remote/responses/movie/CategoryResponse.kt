package vn.tutorial.cinemate.data.remote.responses.movie

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.CategoryModel

data class CategoryResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)


fun CategoryResponse.toCategoryModel() = CategoryModel(
    id = this.id,
    name = this.name,
    description = this.description
)