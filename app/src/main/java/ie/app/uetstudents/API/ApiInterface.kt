package ie.app.uetstudents.API


import ie.app.uetstudents.data.request.LoginRequest
import ie.app.uetstudents.data.request.RegisterFirebaseTokenRequest
import ie.app.uetstudents.data.response.Response
import ie.app.uetstudents.data.response.login.LoginResponse
import ie.app.uetstudents.Entity.Account.Get.dangky.dangky_account
import ie.app.uetstudents.Entity.Account.Post.account
import ie.app.uetstudents.Entity.Account.Put.request.password_put
import ie.app.uetstudents.Entity.Account.Put.response.password_response
import ie.app.uetstudents.Entity.Category.category
import ie.app.uetstudents.Entity.Comment.get.Comment
import ie.app.uetstudents.Entity.Question.get.QuestionX
import ie.app.uetstudents.Entity.Question.get.question
import ie.app.uetstudents.Entity.Search.Question.search_question
import ie.app.uetstudents.Entity.Search.person.person
import ie.app.uetstudents.Entity.like.Get.like_comment_get
import ie.app.uetstudents.Entity.like.Post.like_comment
import ie.app.uetstudents.Entity.like.disLike.dislike_comment
import ie.app.uetstudents.Entity.like_question.post.like_question
import ie.app.uetstudents.Entity.notifications_comment.get.get_notifi_comment
import ie.app.uetstudents.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.Entity.notifications_comment.put.request.comment_id_put
import ie.app.uetstudents.Entity.notifications_comment.put.response.comment_notifi_put
import ie.app.uetstudents.Entity.notifications_question.get.notification_question
import ie.app.uetstudents.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.Entity.notifications_question.put.request.question_id_put
import ie.app.uetstudents.Entity.notifications_question.put.respont.question_notifi_put
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.Entity.subject.DataSubject.data_subject
import ie.app.uetstudents.Entity.subject.subject
import ie.app.uetstudents.Entity.userProfile.get.userprofile
import ie.app.uetstudents.Entity.userProfile.post.avatar.avatar
import ie.app.uetstudents.Entity.userProfile.post.email.request.email_request
import ie.app.uetstudents.Entity.userProfile.post.khoa.request.khoa_request
import ie.app.uetstudents.Entity.userProfile.post.mssv.request.mssv_request
import ie.app.uetstudents.Entity.userProfile.post.response.update_user_response
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


   /*--------------------------Lây danh sách question theo type content---------------------------*/
   @GET("question/type-content/{id_type_content}")
    fun getQuestions(@Path("id_type_content")id_type_content : Int, @Query("index") index : Int,@Query("account_id") account_id : Int): Call<question>
   /*---------------------Lấy danh sách category-----------------------*/
    @GET("category")
    fun getCategory() :Call<category>
/*---------------------Lấy danh sách question theo category--------------------------*/
    @GET("question/category/{id_category}")
    fun getQuestion_of_Category(@Path("id_category") id_category : Int, @Query("index") index: Int,@Query("account_id") account_id : Int) : Call<question>


/*-----------------------Thêm question-----------------*/
//    @Multipart
//    @POST("question/create")
//    fun setQuestion(@Part image_files : MultipartBody.Part?, @Part("Question") Question : RequestBody ) : Call<question>

    @POST("question/create")
    fun setQuestion(@Body body : RequestBody ) : Call<question>

     /*----------------lấy dư liệu question = id question-------------------*/
    @GET("question/id/{id_question}")
    fun getDetailQuestion(@Path("id_question") id_question : Int,@Query("account_id") account_id: Int) : Call<question>

    /*                     Comment                               */
    @GET("comment/question/{id_question}")
    fun getCommentQuestion(@Path("id_question") id_question: Int, @Query("index") index: Int,@Query("account_id")account_id: Int) : Call<Comment>


    @POST("comment/create")
    fun setCommentQuestion(@Body body : RequestBody ) : Call<Comment>

    /*----------------------Thích--------------------------*/

    @POST("react-icon-comment/create")
    fun setLikeComment(@Body likeComment: like_comment) : Call<like_comment>

    @GET("react-icon/comment/{id_comment}")
    fun getPersonsLikeComment(@Path("id_comment") id_comment: Int, @Query("index") index: Int) : Call<like_comment_get>

    @POST("react-icon-question/create")
    fun postlikequestion(@Body likeQuestion: like_question) : Call<like_question>

    /*--------------------Delete account Like Question,comment ---------------------------*/
    @DELETE("react-icon-question/account/{id_account}/question/{id_question}")
    fun deletelikeQueston(@Path("id_account") id_account : Int,@Path("id_question") id_question: Int) : Call<like_question>

    @DELETE("react-icon-comment/account/{id_account}/comment/{id_comment}")
    fun deletelikeComment(@Path("id_account") id_account: Int,@Path("id_comment") id_comment: Int) : Call<dislike_comment>

    /*-------------------Tài Khoản------------------------------*/
    // đăng ký
    @POST("register")
    fun CallsetAccount(@Body Account : account) : Call<dangky_account>

    //đăng nhập
    @POST("login")
    fun callSigninAccount(@Body request : LoginRequest) : Call<LoginResponse>

//    //đăng nhập
//    @FormUrlEncoded
//    @POST("security")
//    fun callSigninAccount2(@Field("username") username : String, @Field("password") password : String, @Field("remember-me") rememberMe : String) : Call<LoginResponse>

//    @GET("login-success")
//    fun callloginsuccess() :Call<dangnhap_account>

    /*-------------------------Search------------------------------*/

    @GET("question/search")
    fun getQuestionSearch(@Query("index") index: Int,@Query("text") text : String,@Query("type_content_id") type_content_id : Int,@Query("account_id")account_id: Int) : Call<search_question>


    @GET("account/search")
    fun getPerSonSearch(@Query("index") index: Int, @Query("text") text: String) : Call<person>

    /*-------------------------lấy danh sách Question theo id account-------------------------------*/ // chua call
    @GET("question/account/{account_id}")
    fun getQuestion_of_account(@Path("account_id") account_id : Int, @Query("index") index : Int,@Query("account_id") accountid: Int) : Call<question>


    @GET("question/account/{account_id}")
    fun getQuestion_of_account_type_content(@Path("account_id") account_id : Int, @Query("index") index : Int,@Query("account_id") accountid: Int,@Query("type_content_id") type_content_id: Int) : Call<question>
    /*---------------------------Thông Báo--------------------------------------------*/



    @GET("notification-comment/author-account/{account_id}")
    fun getNotification_comment_account(@Path("account_id") account_id: Int,@Query("index") index: Int) : Call<get_notifi_comment>

    @GET("notification-question/author-account/{account_id}")
    fun getNotification_question_account(@Path("account_id") account_id: Int,@Query("index") index: Int) : Call<notification_question>

    @POST("notification-question/create")
    fun setNotification_question(@Body notificationQuestionPost: notification_question_post) : Call<notification_question_post>

    @POST("notification-comment/create")
    fun setNotification_comment(@Body postnotifi_comment : post_notifi_comment) : Call<post_notifi_comment>

    /*----------------------put đã xem thông báo------------------------*/
    //question
    @PUT("notification-question/seen")
    fun putseenNotifi(@Body question_id : question_id_put) : Call<question_notifi_put>

    // comment
    @PUT("notification-comment/seen")
    fun putseenNotifi_comment(@Body comment_id_put : comment_id_put) : Call<comment_notifi_put>



    /*--------------------------------Lây danh sách những ngươi đã like comment-----------------------------------------------*/
    @GET("react-icon-comment/comment/{id_comment}")
    fun getPersonLikeComment(@Path("id_comment") id_comment: Int,@Query("index") index: Int) : Call<like_comment_get>
    /*--------------------Lây ra danh sách những người đã like bài viết---------------------------------*/
    @GET("react-icon-question/question/{id_question}")
    fun getPersonLikeQuestion(@Path("id_question") id_question: Int, @Query("index") index: Int) : Call<ie.app.uetstudents.Entity.like_question.get.like_question>


    /*-------------------------------Lấy danh sách môn học theo ID Category-------------------------------------------------*/

    @GET("subject/category/{id_category}")
    fun getSubject_category(@Path("id_category") id_category: Int, @Query("index") index: Int) : Call<subject>

    /*--------------------------------------------Lấy danh sách tài liệu môn học ----------------------------------------------------------------*/

    @GET("exam-document/subject/{id_subject}")
    fun getDataSubjectforid(@Path("id_subject") id_subject : Int, @Query("type") type : String,@Query("index") index : Int) : Call<data_subject>

    /*---------------------------------------------------------------------*/

    @GET("question/comment/{id_comment}")
    fun getQuestion_of_comment(@Path("id_comment") id_comment: Int,@Query("account_id") account_id: Int ) : Call<QuestionX>

    @POST("register_firebase_token")
    fun registerFirebaseToken(@Body request : RegisterFirebaseTokenRequest) : Call<Response>

    @GET("notification-question/author-account/{userId}/unseen?index=1")
    fun getUnreadQuestion(@Path("userId") id : Int) : Call<Response>

    @GET("notification-comment/author-account/{userId}/unseen?index=1")
    fun getUnreadComment(@Path("userId") id : Int) : Call<Response>


    /*-------------------------get user------------------------*/
    @GET("user-profile/get/{id_user}")
    fun getUserProfile(@Path("id_user") id_user : Int) : Call<userprofile>

    /*------------------------Thay đổi mật khẩu------------------------*/
    @POST("change-password")
    fun change_password(@Body put_password : password_put) : Call<password_response>

    /*--------------------------Thay đổi thông tin-----------------------------*/
    @POST("user-profile/update")
    fun update_user_email(@Body emailRequest: email_request) :Call<update_user_response>

    @POST("user-profile/update")
    fun update_user_khoa(@Body khoaRequest: khoa_request) : Call<update_user_response>

    @POST("user-profile/update")
    fun update_user_mssv(@Body mssvRequest: mssv_request) : Call<update_user_response>

    @POST("user-profile/update-avatar")
    fun update_image_profile(@Body body : RequestBody) : Call<avatar>


    /*-----------------------sub comment------------------------------------*/

    @GET("sub-comment/comment/{id_comment}")
    fun getSubComment(@Path("id_comment") id_comment: Int,@Query("index") index: Int) : Call<getsubcomment>
    @POST("sub-comment/create")
    fun postSubcomment(@Body body : RequestBody) : Call<getsubcomment>

    @GET("user-profile/get/username/{username}")
    fun getUserProfile(@Path("username") username : String) : Call<userprofile>
}
