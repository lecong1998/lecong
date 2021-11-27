package ie.app.uetstudents.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Account.account
import ie.app.uetstudents.ui.Entity.Category.category
import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.QuestionX
import ie.app.uetstudents.ui.diendan.category.CategoryContract
import ie.app.uetstudents.ui.diendan.detailForum.DetailForumContract
import ie.app.uetstudents.ui.diendan.forum_main.forumContract
import ie.app.uetstudents.ui.login.LogContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
val context: Context) {
    fun CallItemQuestion(presenter : forumContract.Presenter,id_type_content : Int, index: Int)
    {
        var dataList: List<QuestionDto> = ArrayList<QuestionDto>()

        var data : QuestionX
        val call: Call<QuestionX> =
            ApiClient.getClient.getQuestions(id_type_content,index)
        call.enqueue(object : Callback<QuestionX> {
            override fun onResponse(
                call: Call<QuestionX>?,
                response: Response<QuestionX>?
            ) {
                if (response?.isSuccessful!!) {

                    data = response.body()!!

                    presenter.updateUI(data)
                }
            }

            override fun onFailure(call: Call<QuestionX>?, t: Throwable?) {
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

    fun CallQuestions_Category(presenter: forumContract.Presenter,id_category: Int, index: Int)
    {
        var data : QuestionX
        val call : Call<QuestionX> = ApiClient.getClient.getQuestion_of_Category(id_category,index)
        call.enqueue(object : Callback<QuestionX>{
            override fun onResponse(call: Call<QuestionX>, response: Response<QuestionX>) {
                if(response.isSuccessful)
                {
                    data = response.body()!!
                    presenter.updateUI(data)
                }
            }

            override fun onFailure(call: Call<QuestionX>, t: Throwable) {
                Log.e("Test_API","Test Fail")
            }

        })
    }

    fun CallQuestionDetail(presenter: DetailForumContract.Presenter,id:Int)
    {
        var data : QuestionDto
        val call: Call<QuestionX> = ApiClient.getClient.getDetailQuestion(id)
        call.enqueue(object : Callback<QuestionX>{
            override fun onResponse(call: Call<QuestionX>, response: Response<QuestionX>) {
                if (response.isSuccessful)
                {
                    data = response.body()!!.questionDtoList.get(0)
                    presenter.getDataUI(data)
                    Log.e("API_detailquestion",data?.content.toString())
                }

            }

            override fun onFailure(call: Call<QuestionX>, t: Throwable) {
                Log.e("API_Test","thất bại")
            }
        })
    }
    /*-----------------Lấy bình luận của câu hỏi----------------------------------*/
    fun CallCommentQuestion(presenter: DetailForumContract.Presenter,id:Int,index: Int)
    {

        var datacomment : comment
        val call: Call<comment> = ApiClient.getClient.getCommentQuestion(id,index)
        call.enqueue(object : Callback<comment>{
            override fun onResponse(call: Call<comment>, response: Response<comment>) {
                if (response.isSuccessful)
                {
                    datacomment = response.body()!!
                    presenter.getDataUIComment(datacomment)
                }
            }

            override fun onFailure(call: Call<comment>, t: Throwable) {
                Log.e("TestAPI","thất bại")
            }
        })
    }
    /*-------------- lấy tài khoản------------------------------*/
    fun CallAccount(presenter: LogContract.Presenter){
            var data : account
            val call : Call<account> = ApiClient.getClient.CallAccount()
            call.enqueue(object : Callback<account>{

                override fun onResponse(call: Call<account>, response: Response<account>) {
                    if (response.isSuccessful)
                    {
                        data = response.body()!!
                        presenter.UpdateUI(data)
                    }

                }

                override fun onFailure(call: Call<account>, t: Throwable) {
                    Log.e("Test_API","Lỗi")
                }
            })
    }
}