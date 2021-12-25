package ie.app.uetstudents.Entity.Category

data class category(
    val categoryDtoList: List<CategoryDto>,
    val message: String,
    val success: Boolean
)