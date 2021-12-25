package ie.app.uetstudents.ui.thongbao.detail

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX

class DetailPresenter(
    private val View : DetailContract.View,
    private val Repository : Repository
) : DetailContract.Presenter{
    override fun getQuestionDetail(id_question: Int, account_id: Int) {
        Repository.getDetaiNotifiUet(this,id_question, account_id)
    }

    override fun UpdateUiDetail(Question: QuestionDtoX) {
        View.UpdateViewDetail(Question)
    }
}