package com.nearby.data.model

import androidx.annotation.DrawableRes
import com.nearby.ui.comonents.category.CategoryFilterChipView
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String
) {
    @get:DrawableRes
    val icon: Int?
        get() = CategoryFilterChipView.Companion.fromDescription(description = name)?.icon
}
