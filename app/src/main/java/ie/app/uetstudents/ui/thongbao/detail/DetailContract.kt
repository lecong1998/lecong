package ie.app.uetstudents.ui.thongbao.detail

import ie.app.uetstudents.Entity.Question.get.QuestionDtoX

interface DetailContract {
    interface View {
        fun UpdateViewDetail(Question: QuestionDtoX)
    }
    interface Presenter{
        fun getQuestionDetail(id_question : Int, account_id : Int)
        fun UpdateUiDetail(Question: QuestionDtoX)
    }
}