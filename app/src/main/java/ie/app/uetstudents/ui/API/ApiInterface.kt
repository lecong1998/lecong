package ie.app.uetstudents.ui.API

import ie.app.uetstudents.ui.Entity.Account.AccountDto
import ie.app.uetstudents.ui.Entity.Account.account
import ie.app.uetstudents.ui.Entity.Category.category
import ie.app.uetstudents.ui.Entity.Comment.CommentDto
import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.Comment.postcomment
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.QuestionPost
import ie.app.uetstudents.ui.Entity.Question.QuestionX
import ie.app.uetstudents.ui.Entity.Search.search_person
import ie.app.uetstudents.ui.Entity.Search.search_question
import ie.app.uetstudents.ui.Entity.like.like_comment
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
   @GET("question/type-content/{id_type_content}")
    fun getQuestions(@Path("id_type_content")id_type_content : Int, @Query("index") index : Int): Call<QuestionX>

    @GET("category")
    fun getCategory() :Call<category>

    @GET("question/category/{id_category}")
    fun getQuestion_of_Category(@Path("id_category") id_category : Int, @Query("index") index: Int) : Call<QuestionX>

    @POST("question/create")
    fun setQuestion(@Body Questionpost: QuestionPost) : Call<QuestionPost>

    @GET("question/id/{id_question}")
    fun getDetailQuestion(@Path("id_question") id_question : Int) : Call<QuestionX>

    /*                     Comment                               */
    @GET("comment/question/{id_question}")
    fun getCommentQuestion(@Path("id_question") id_question: Int, @Query("index") index: Int) : Call<comment>

    @POST("comment/create")
    fun setCommentQuestion(@Body postcomment: postcomment) : Call<comment>

    /*----------------------Thích--------------------------*/

    @POST("react-icon/create")
    fun setLikeComment(@Body likeComment: like_comment) : Call<like_comment>

    @GET("react-icon/comment/{id_comment}")
    fun getPersonsLikeComment(@Path("id_comment") id_comment: Int, @Query("index") index: Int)

    /*-------------------Tài Khoản------------------------------*/
    @GET("getAccount")
    fun  CallAccount() : Call<account>

    @POST("register")
    fun CallsetAccount(@Body AccountDto : AccountDto) : Call<account>

    /*-------------------------Search------------------------------*/

    @GET("question/search")
    fun getQuestionSearch(@Query("index") index: Int,@Query("text") text : String,@Query("type_content_id") type_content_id : Int) : Call<search_question>


    @GET("account/search")
    fun getPerSonSearch(@Query("index") index: Int, @Query("text") text: String) : Call<search_person>

    /*-------------------------lấy danh sách Question theo id account-------------------------------*/
    @GET("question/account/{account_id}")
    fun getQuestion_of_account(@Path("account_id") account_id : Int, @Query("index") index : Int) : Call<QuestionX>

}