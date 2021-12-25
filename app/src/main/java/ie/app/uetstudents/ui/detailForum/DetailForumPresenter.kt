package ie.app.uetstudents.ui.detailForum

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.Entity.Comment.get.Comment
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.Entity.notifications_question.post.notification_question_post

class DetailForumPresenter(
    private val View : DetailForumContract.View,
    private val repository: Repository
): DetailForumContract.Presenter {
    override fun getDetailForum(id: Int,account_id : Int) {
        repository.CallQuestionDetail(this,id,account_id)
    }

    override fun getDataUI(data: QuestionDtoX) {
        View.getDataView(data)
    }

    /*----------------------getcomment------------------------------*/
    override fun getDataUIComment(datacomment: Comment) {
        View.getDataViewComment(datacomment)
    }

    override fun getDetailComment(id: Int,index : Int,account_id: Int) {
        repository.CallCommentQuestion(this,id,index,account_id)
    }

    /*-------------------------Post notification Question into database---------------------------*/
    override fun setNotificationQuestion(notifi_item: notification_question_post) {
        repository.updateNotifi_Question(notifi_item)
    }
    /*------------------------Post notification Comment into database----------------------------------------------------------*/
    override fun setNotificationComment(notifi_comment: post_notifi_comment) {
        repository.updateNotifi_Comment(notifi_comment)
    }

}