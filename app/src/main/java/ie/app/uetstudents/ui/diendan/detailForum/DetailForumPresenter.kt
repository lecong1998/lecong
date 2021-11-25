package ie.app.uetstudents.ui.diendan.detailForum

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.QuestionX

class DetailForumPresenter(
    private val View : DetailForumContract.View,
    private val repository: Repository
): DetailForumContract.Presenter {
    override fun getDetailForum(id: Int) {
        repository.CallQuestionDetail(this,id)
    }

    override fun getDataUI(data: QuestionDto) {
        View.getDataView(data)
    }

    override fun getDataUIComment(datacomment: comment) {
        View.getDataViewComment(datacomment)
    }

    override fun getDetailComment(id: Int,index : Int) {
        repository.CallCommentQuestion(this,id,index)
    }
}