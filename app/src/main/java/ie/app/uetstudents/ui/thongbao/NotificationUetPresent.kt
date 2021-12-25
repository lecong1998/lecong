package ie.app.uetstudents.ui.thongbao

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.Entity.Question.get.question

class NotificationUetPresent(
    private val View : NotificationUetContract.View,
    private val Repository: Repository
) : NotificationUetContract.Presenter{
    override fun getQuestionList(index: Int, type_content_id: Int, account_id: Int) {
        Repository.getNotificationUet(this,index, type_content_id, account_id)

    }

    override fun UpdateUIItem(question: question) {
        View.UpdateDataViewItem(question)
    }
}