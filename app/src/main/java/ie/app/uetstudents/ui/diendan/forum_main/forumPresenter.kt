package ie.app.uetstudents.ui.diendan.forum_main

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.ui.Entity.Question.get.QuestionX
import ie.app.uetstudents.ui.Entity.Question.get.question

class forumPresenter(
    private val view : forumContract.View,
    private val repository: Repository
): forumContract.Presenter {

    override fun getQuestions(id_type_content: Int,index : Int, account_id : Int) {
        repository.CallItemQuestion(this,id_type_content,index,account_id)
    }

    override fun updateUI(data : question) {
        view.updateViewData(data)
    }

    override fun getQuestions_of_Category(id_category: Int, index: Int, account_id: Int) {
        repository.CallQuestions_Category(this,id_category, index,account_id)
    }

}