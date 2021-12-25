package ie.app.uetstudents.ui.diendan.category

import ie.app.uetstudents.Entity.Category.category

interface CategoryContract {
    interface View{
        fun updateViewData(data : category)
    }

    interface Presenter{
        fun getCategorys()
        fun updateUI(data: category)
    }
}