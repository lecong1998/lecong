package ie.app.uetstudents.ui.profile

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.ui.Entity.Comment.get.Comment
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.userProfile.get.userprofile

class ProfilePresenter (
    private val View : ProfileContract.View,
    private val Repository : Repository
        ) : ProfileContract.Presenter{
    override fun getQuestionProfile(index: Int, account_id: Int,type_content_id : Int) {
        Repository.GetQuestion_accountid(this,index,account_id,type_content_id)
    }

    override fun UpdateUIQuestionProfile(question: question,type_content_id: Int) {
        View.UpdateViewDataQuestionProfile(question,type_content_id)
    }

    override fun getUserInformation(account_id: Int) {
        Repository.getInformationAccount(this,account_id)
    }

    override fun UpdateUIUserinformation(userprofile: userprofile) {
        View.UpdateViewDataUser(userprofile)
    }


}