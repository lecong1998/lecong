package ie.app.uetstudents.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Account.Put.request.password_put
import ie.app.uetstudents.Entity.Account.Put.response.password_response
import ie.app.uetstudents.Entity.Category.category
import ie.app.uetstudents.Entity.Comment.get.Comment
import ie.app.uetstudents.Entity.Question.get.QuestionDto
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.Entity.Question.get.question
import ie.app.uetstudents.Entity.Search.Question.search_question
import ie.app.uetstudents.Entity.Search.person.person
import ie.app.uetstudents.Entity.like.disLike.dislike_comment
import ie.app.uetstudents.Entity.notifications_comment.get.get_notifi_comment
import ie.app.uetstudents.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.Entity.notifications_comment.put.request.comment_id_put
import ie.app.uetstudents.Entity.notifications_comment.put.response.comment_notifi_put
import ie.app.uetstudents.Entity.notifications_question.get.notification_question
import ie.app.uetstudents.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.Entity.notifications_question.put.request.question_id_put
import ie.app.uetstudents.Entity.notifications_question.put.respont.question_notifi_put
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.Entity.userProfile.get.userprofile
import ie.app.uetstudents.Entity.userProfile.post.email.request.email_request
import ie.app.uetstudents.Entity.userProfile.post.khoa.request.khoa_request
import ie.app.uetstudents.Entity.userProfile.post.mssv.request.mssv_request
import ie.app.uetstudents.Entity.userProfile.post.response.update_user_response
import ie.app.uetstudents.ui.diendan.category.CategoryContract
import ie.app.uetstudents.ui.detailForum.DetailForumContract
import ie.app.uetstudents.ui.diendan.forum_main.forumContract
import ie.app.uetstudents.ui.notifications.notification_Contract
import ie.app.uetstudents.ui.profile.ProfileContract
import ie.app.uetstudents.ui.thongbao.NotificationUetContract
import ie.app.uetstudents.ui.thongbao.detail.DetailContract
import ie.app.uetstudents.ui.timkiem.SearchContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
val context: Context) {
    fun CallItemQuestion(presenter : forumContract.Presenter,id_type_content : Int, index: Int, account_id : Int)
    {
        var dataList: List<QuestionDto> = ArrayList<QuestionDto>()

        var data : question
        val call: Call<question> =
            ApiClient.getClient.getQuestions(id_type_content,index,account_id)
        call.enqueue(object : Callback<question> {
            override fun onResponse(
                call: Call<question>?,
                response: Response<question>?
            ) {
                if (response?.isSuccessful!!) {

                    data = response.body()!!

                    presenter.updateUI(data)
                }
            }

            override fun onFailure(call: Call<question>?, t: Throwable?) {
                Log.d("Test_API", "onFailure() called with: call = $call, t = $t")
            }
        })
    }

    fun CallCategorys(presenter: CategoryContract.Presenter)
    {
        var data : category
        val call : Call<category> = ApiClient.getClient.getCategory()
        call.enqueue(object : Callback<category>{
            override fun onResponse(call: Call<category>, response: Response<category>) {
                //Toast.makeText(context,"Call thanh cong",Toast.LENGTH_SHORT).show()
                if(response.isSuccessful!!)
                {
                    data = response.body()!!
                    presenter.updateUI(data)
                }
            }

            override fun onFailure(call: Call<category>, t: Throwable) {
                Toast.makeText(context,"Call that bai",Toast.LENGTH_LONG).show()
                Log.e("Test_API","Test Fail")
            }

        })
    }

    fun CallQuestions_Category(presenter: forumContract.Presenter,id_category: Int, index: Int,account_id: Int)
    {
        var data : question
        val call : Call<question> = ApiClient.getClient.getQuestion_of_Category(id_category,index,account_id)
        call.enqueue(object : Callback<question>{
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if(response.isSuccessful)
                {
                    data = response.body()!!
                    presenter.updateUI(data)
                }
            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("Test_API","Test Fail")
            }

        })
    }

    fun CallQuestionDetail(presenter: DetailForumContract.Presenter,id:Int,account_id: Int)
    {
        var data : QuestionDtoX
        val call: Call<question> = ApiClient.getClient.getDetailQuestion(id,account_id)
        call.enqueue(object : Callback<question>{
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if (response.isSuccessful)
                {
                    data = response.body()!!.questionDtoList.get(0)
                    presenter.getDataUI(data)
                   // Log.e("API_detailquestion",data?.content.toString())
                }

            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("API_Test","thất bại")
            }
        })
    }
    /*-----------------Lấy bình luận của câu hỏi----------------------------------*/
    fun CallCommentQuestion(presenter: DetailForumContract.Presenter,id:Int,index: Int,account_id: Int)
    {

        var datacomment : Comment
        val call: Call<Comment> = ApiClient.getClient.getCommentQuestion(id,index,account_id)
        call.enqueue(object : Callback<Comment>{
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful)
                {
                    datacomment = response.body()!!
                    presenter.getDataUIComment(datacomment)
                    Log.e("TestAPI_laycomment","thành công")
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("TestAPI","thất bại")
            }
        })
    }
    /*-------------- tài khoản------------------------------*/

    // đăng ký


    /*------------------------------Lấy notification_Question---------------------------------------------*/
    fun callNotificationQuestion(presenter: notification_Contract.Presenter, id_account: Int, page: Int)
    {
        var data_notifi_question : notification_question
        val call : Call<notification_question> = ApiClient.getClient.getNotification_question_account(id_account,page)
        call.enqueue(object : Callback<notification_question>{
            override fun onResponse(
                call: Call<notification_question>,
                response: Response<notification_question>
            ) {
                if (response.isSuccessful)
                {
                    data_notifi_question = response.body()!!
                    presenter.getUpdateUINotificationQuestion(data_notifi_question)
                }
            }

            override fun onFailure(call: Call<notification_question>, t: Throwable) {
                Log.e("Test","Thất Bại")
            }
        })
    }


    fun putseenNotifiQuestion (questionIdPut: question_id_put)
    {
        val call : Call<question_notifi_put> = ApiClient.getClient.putseenNotifi(questionIdPut)
        call.enqueue(object : Callback<question_notifi_put>{
            override fun onResponse(
                call: Call<question_notifi_put>,
                response: Response<question_notifi_put>
            ) {
                if (response.isSuccessful)
                {
                    Log.e("Put seen notifi","Thành công")
                }
            }

            override fun onFailure(call: Call<question_notifi_put>, t: Throwable) {
                Log.e("Put seen notifi","Thất bại")
            }
        })
    }

    /*--------------------------------notification comment-------------------------------------*/
    fun callNotificationComment(presenter: notification_Contract.Presenter,id_account: Int,page: Int)
    {
        var data_notifi_comment : get_notifi_comment
        val call : Call<get_notifi_comment> = ApiClient.getClient.getNotification_comment_account(id_account,page)
        call.enqueue(object : Callback<get_notifi_comment>{
            override fun onResponse(
                call: Call<get_notifi_comment>,
                response: Response<get_notifi_comment>
            ) {
                if (response.isSuccessful)
                {
                    data_notifi_comment = response.body()!!
                    presenter.getUpdateUINotificationComment(data_notifi_comment)
                }
            }

            override fun onFailure(call: Call<get_notifi_comment>, t: Throwable) {
                Log.e("Test","Thất Bại")
            }
        })
    }

    fun putSeenNotifi_comment(commentIdPut: comment_id_put)
    {
        val call : Call<comment_notifi_put> = ApiClient.getClient.putseenNotifi_comment(commentIdPut)
        call.enqueue(object : Callback<comment_notifi_put>{
            override fun onResponse(
                call: Call<comment_notifi_put>,
                response: Response<comment_notifi_put>
            ) {
                if (response.isSuccessful)
                {
                    Log.e("seen comment _ notifi","Thành công")
                }
            }

            override fun onFailure(call: Call<comment_notifi_put>, t: Throwable) {
                Log.e("seen comment _ notifi","Thất bại")
            }
        })
    }

    /*---------------------------Search Person--------------------------------*/
    fun callPersonSearch(presenter: SearchContract.Presenter,page: Int,text: String)
    {
        var person : person
        val call : Call<person> = ApiClient.getClient.getPerSonSearch(page,text)
        call.enqueue(object : Callback<person>{
            override fun onResponse(call: Call<person>, response: Response<person>) {
                if (response.isSuccessful)
                {
                    person = response.body()!!
                    presenter.UpdateUIperson(person)
                    Log.e("Test_search_person","Thành Công")
                }
            }

            override fun onFailure(call: Call<person>, t: Throwable) {
                Log.e("Test","Thất Bại")
            }
        })
    }
    /*-------------------------------Search_Question------------------------------------*/

    fun callQuestionSearch(presenter: SearchContract.Presenter,page: Int,text: String,type_content_id : Int, account_id: Int)
    {
        var searchQuestion : search_question
        val call: Call<search_question> = ApiClient.getClient.getQuestionSearch(page,text,type_content_id,account_id)
        call.enqueue(object : Callback<search_question>{
            override fun onResponse(
                call: Call<search_question>,
                response: Response<search_question>
            ) {
                if (response.isSuccessful)
                {
                    searchQuestion = response.body()!!
                    presenter.UpdateUIQuestion(searchQuestion)
                    Log.e("Test_search_question","Thành CÔng")
                }
            }

            override fun onFailure(call: Call<search_question>, t: Throwable) {
                Log.e("Test","Thất Bại")
            }
        })
    }

    /*-------------------------------Post notification Question------------------------------*/
     fun updateNotifi_Question (notifi_item: notification_question_post)
    {
        val callnotifi : Call<notification_question_post> = ApiClient.getClient.setNotification_question(notifi_item)
        callnotifi.enqueue(object : Callback<notification_question_post>{
            override fun onResponse(
                call: Call<notification_question_post>,
                response: Response<notification_question_post>
            ) {
                if (response.isSuccessful)
                {
                    Log.e("Test_post_notifi_question","thành công")
                }
            }

            override fun onFailure(
                call: Call<notification_question_post>,
                t: Throwable
            ) {
                Log.e("Test_post_notifi_question","Thất bại")
            }
        })
     }

    /*-------------------------------Post notification Comment-------------------------------------*/

    fun updateNotifi_Comment(postnotifi_comment : post_notifi_comment)
    {
        val call : Call<post_notifi_comment> = ApiClient.getClient.setNotification_comment(postnotifi_comment)
        call.enqueue(object : Callback<post_notifi_comment>{
            override fun onResponse(
                call: Call<post_notifi_comment>,
                response: Response<post_notifi_comment>
            ) {
                if (response.isSuccessful)
                {
                    Log.e("Test_post_notifi_comment","Thành Công")
                }
            }

            override fun onFailure(call: Call<post_notifi_comment>, t: Throwable) {
                Log.e("Test_post_notifi_comment","Thất bại")
            }
        })
    }

    /*-------------------------------Get persons like Question-----------------------------------------*/

    fun getPersons_LikeQuestion(presenter: DetailForumContract.Presenter,id_question: Int, page: Int)
    {
        var personslike : Int   = 0
        val call : Call<ie.app.uetstudents.Entity.like_question.get.like_question> = ApiClient.getClient.getPersonLikeQuestion(id_question,page)
        call.enqueue(object : Callback<ie.app.uetstudents.Entity.like_question.get.like_question>{
            override fun onResponse(
                call: Call<ie.app.uetstudents.Entity.like_question.get.like_question>,
                response: Response<ie.app.uetstudents.Entity.like_question.get.like_question>
            ) {
                if (response.isSuccessful)
                {
                    if (response.body()!!.result_quantity != 0)
                    {
                       personslike = response.body()!!.result_quantity

                    }
                }
                Log.e("test_get_person_like question","Số lượt thích lấy thành công")
            }

            override fun onFailure(
                call: Call<ie.app.uetstudents.Entity.like_question.get.like_question>,
                t: Throwable
            ) {
                Log.e("test_get_person_like question","thất bại")
            }
        })
    }

    /*------------------------Lấy danh sách bài viết ---------------------------*/

    fun GetQuestion_accountid (presenter: ProfileContract.Presenter,index: Int,account_id: Int,type_content_id: Int)
    {
        var question : question
        val call : Call<question> = ApiClient.getClient.getQuestion_of_account_type_content(account_id,index,account_id,type_content_id)
        call.enqueue(object  : Callback<question>{
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if (response.isSuccessful)
                {
                    question = response.body()!!
                    presenter.UpdateUIQuestionProfile(question,type_content_id)
                    Log.e("Api_callQuestionAccount","Thành công")
                }

            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("Api_callQuestionAccount","Thất bại")
            }
        })
    }
    /*-----------------------Lấy thông tin account------------------------------*/

    fun getInformationAccount(presenter: ProfileContract.Presenter,account_id: Int)
    {

        val call : Call<userprofile> = ApiClient.getClient.getUserProfile(account_id)
        call.enqueue(object : Callback<userprofile> {
            override fun onResponse(call: Call<userprofile>, response: Response<userprofile>) {
                if (response.isSuccessful)
                {
                    presenter.UpdateUIUserinformation(response.body()!!)
                    Log.e("Test_call_userProfile","Thành công")
                }
            }

            override fun onFailure(call: Call<userprofile>, t: Throwable) {
                Log.e("Test_call_userProfile","Thất bại")
            }
        })
    }

    /*------------------------------Get notification UET-------------------------------*/

    fun getNotificationUet(presenter: NotificationUetContract.Presenter,index: Int,type_content_id: Int,account_id: Int)
    {
        var question : question
        val call : Call<question> = ApiClient.getClient.getQuestions(type_content_id,index,account_id)
        call.enqueue(object : Callback<question>{
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if (response.isSuccessful)
                {
                    question = response.body()!!
                    presenter.UpdateUIItem(question)
                    Log.e("test_notificationUet","Thành công")
                }
            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("test_notificationUet","Thất bại")
            }
        })
    }
    /*---------------------------Get NotificationDetail---------------------------------------------------*/
    fun getDetaiNotifiUet(presenter : DetailContract.Presenter,id_question: Int,account_id: Int)
    {
        var questionDtoX : QuestionDtoX
        val call : Call<question> = ApiClient.getClient.getDetailQuestion(id_question, account_id)
        call.enqueue(object : Callback<question>{
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if (response.isSuccessful)
                {
                    Log.e("Test","thanh cong")
                    questionDtoX = response.body()!!.questionDtoList[0]
                    presenter.UpdateUiDetail(questionDtoX)
                }
            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("Test","that bai")
            }
        })

    }

    /*--------------------------------------------------------*/
    fun change_email_user(emailRequest: email_request)
    {
            val call: Call<update_user_response> = ApiClient.getClient.update_user_email(emailRequest)
        call.enqueue(object : Callback<update_user_response>{
            override fun onResponse(
                call: Call<update_user_response>,
                response: Response<update_user_response>
            ) {
                if (response.isSuccessful)
                {
                    Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    Log.e("Update email","Thành công")
                }
            }

            override fun onFailure(call: Call<update_user_response>, t: Throwable) {
                Log.e("Update email","Thất bại")
            }
        })
    }
    /*--------------------------------------------------------*/
    fun change_khoa_user(khoaRequest: khoa_request)
    {
        val call: Call<update_user_response> = ApiClient.getClient.update_user_khoa(khoaRequest)
        call.enqueue(object : Callback<update_user_response>{
            override fun onResponse(
                call: Call<update_user_response>,
                response: Response<update_user_response>
            ) {
                if (response.isSuccessful)
                {
                    Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    Log.e("Update khoa","Thành công")
                }
            }

            override fun onFailure(call: Call<update_user_response>, t: Throwable) {
                Log.e("Update khoa","Thất bại")
            }
        })
    }

    /*--------------------------------------------------------*/

    fun change_mssv_user(mssvRequest: mssv_request)
    {
        val call: Call<update_user_response> = ApiClient.getClient.update_user_mssv(mssvRequest)
        call.enqueue(object : Callback<update_user_response>{
            override fun onResponse(
                call: Call<update_user_response>,
                response: Response<update_user_response>
            ) {
                if (response.isSuccessful)
                {
                    Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    Log.e("Update mssv","Thành công")
                }
            }

            override fun onFailure(call: Call<update_user_response>, t: Throwable) {
                Log.e("Update mssv","Thất bại")
            }
        })
    }

    /*------------------------thay đổi mật khẩu---------------------------------*/
    fun change_password(put_password : password_put)
    {
        val call: Call<password_response> = ApiClient.getClient.change_password(put_password)
        call.enqueue(object : Callback<password_response>{
            override fun onResponse(
                call: Call<password_response>,
                response: Response<password_response>
            ) {
                if (response.isSuccessful)
                {
                    Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    Log.e("Update mật khẩu","Thành công")
                }
            }

            override fun onFailure(call: Call<password_response>, t: Throwable) {
                Log.e("Update mật khẩu","Thất bại")
            }
        })
    }
    /*--------------------------Delete like comment--------------------------------*/

    fun deletelike_comment(id_account: Int,id_comment : Int)
    {
        val call: Call<dislike_comment> = ApiClient.getClient.deletelikeComment(id_account,id_comment)
        call.enqueue(object : Callback<dislike_comment>{
            override fun onResponse(
                call: Call<dislike_comment>,
                response: Response<dislike_comment>
            ) {
                if (response.isSuccessful)
                {
                    Log.e("bỏ like comment","thành công")
                }
            }

            override fun onFailure(call: Call<dislike_comment>, t: Throwable) {
                Log.e("bỏ like comment","Thất bại")
            }
        })
    }

    /*-----------------------------Get sub comment---------------------------------------*/

}