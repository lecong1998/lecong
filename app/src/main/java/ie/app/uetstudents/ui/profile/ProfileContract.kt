package ie.app.uetstudents.ui.profile

import ie.app.uetstudents.ui.Entity.Comment.get.Comment
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.userProfile.get.userprofile

interface ProfileContract {
    interface View{
        fun UpdateViewDataQuestionProfile(question: question)
        fun UpdateViewDataUser(userprofile: userprofile)
    }
    interface Presenter{

        /*---------------lấy question profile-------------------*/
        fun getQuestionProfile(index : Int,account_id : Int)
        fun UpdateUIQuestionProfile(question: question)
        /*-----------------------Lấy thông tin u-------------------------------*/

        fun getUserInformation(account_id: Int)
        fun UpdateUIUserinformation(userprofile: userprofile)
    }
}