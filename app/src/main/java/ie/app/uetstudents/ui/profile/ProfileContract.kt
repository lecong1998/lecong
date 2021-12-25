package ie.app.uetstudents.ui.profile

import ie.app.uetstudents.Entity.Question.get.question
import ie.app.uetstudents.Entity.userProfile.get.userprofile

interface ProfileContract {
    interface View{
        fun UpdateViewDataQuestionProfile(question: question,type_content_id: Int)
        fun UpdateViewDataUser(userprofile: userprofile)
    }
    interface Presenter{

        /*---------------lấy question profile-------------------*/
        fun getQuestionProfile(index : Int,account_id : Int,type_content_id : Int)
        fun UpdateUIQuestionProfile(question: question,type_content_id: Int)
        /*-----------------------Lấy thông tin u-------------------------------*/

        fun getUserInformation(account_id: Int)
        fun UpdateUIUserinformation(userprofile: userprofile)
    }
}