package ie.app.uetstudents.ui.diendan.detailForum

import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.QuestionX

interface DetailForumContract {
    interface View {
        fun getDataView(data: QuestionDto)
        fun getDataViewComment(datacomment : comment)
    }
    interface Presenter{
        fun getDetailForum(id: Int)
        fun getDataUI(data: QuestionDto)
        fun getDataUIComment(datacomment: comment)
        fun getDetailComment(id:Int,index: Int)
    }
}