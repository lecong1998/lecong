package ie.app.uetstudents.Entity.Search

data class search_person (
    val message: String,
    val personDtoList: List<PersonDto>,
    val result_quantity: Int,
    val success: Boolean
    )