package ie.app.uetstudents.ui.thongbao

import ie.app.uetstudents.Entity.Question.get.question

interface NotificationUetContract  {
    interface View{
        fun UpdateDataViewItem(question: question)
    }
    interface Presenter{
        fun getQuestionList(index: Int,type_content_id : Int, account_id : Int)
        fun UpdateUIItem(question: question)
    }
}