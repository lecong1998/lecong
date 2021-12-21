package ie.app.uetstudents.ui.diendan.detailForum

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ie.app.uetstudents.R
import ie.app.uetstudents.RealPathUtil.RealPathUtil
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.ClickItemCommentLike
import ie.app.uetstudents.adapter.CommentAdapter
import ie.app.uetstudents.adapter.adapter_hienthianh
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Comment.get.CommentDto
import ie.app.uetstudents.ui.Entity.Comment.post.Question
import ie.app.uetstudents.ui.Entity.Comment.post.comment_post
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.ui.Entity.like.Post.Account
import ie.app.uetstudents.ui.Entity.like.Post.Comment
import ie.app.uetstudents.ui.Entity.like.Post.like_comment
import ie.app.uetstudents.ui.Entity.like_question.post.like_question
import ie.app.uetstudents.ui.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.ui.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_detail_forum.*
import kotlinx.android.synthetic.main.fragment_detail_forum.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class DetailForumFragment : Fragment(), DetailForumContract.View, ClickItemCommentLike {

    private val CAMERA_REQUEST: Int = 9999
    private var id_question: Int? = null
    private var owner_username : String? = null

    private lateinit var adapter_comment: CommentAdapter
    private lateinit var presenterDetailForum: DetailForumContract.Presenter
    private var page_comment = 1

    private var uri: Uri? = null

    var id_user : Int?= null
     var soluotthich : Int? = null
    var soluotbinhluan : Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            id_question = it?.getInt("id_question")
            owner_username = it?.getString("owner_username")
        }
        setHasOptionsMenu(true)
        id_user = PreferenceUtils.getUser().id

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_forum, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenterDetailForum = DetailForumPresenter(this, Repository(requireContext()))

        id_question?.let { presenterDetailForum.getDetailForum(it,PreferenceUtils.getUser().id!!) }

        /*-------------------Thích Question---------------------------*/



        /*---------------------------------------------------------------*/

        /*---------------------update Comment vào layout-----------------------------------*/
        id_question?.let { presenterDetailForum.getDetailComment(it, page_comment,id_user!!) }

        adapter_comment = CommentAdapter(this)

        view.detail_comment_forum_recyclerview.layoutManager = LinearLayoutManager(context)
        view.detail_comment_forum_recyclerview.adapter = adapter_comment
        view.detail_comment_forum_recyclerview.isNestedScrollingEnabled = false
        view.detail_scrollview.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v.measuredHeight)) {
                    page_comment++
                    view.detailforum_progressbar.visibility = View.VISIBLE
                    id_question?.let { presenterDetailForum.getDetailComment(it, page_comment,id_user!!) }

                }
                if (scrollY == oldScrollY.plus(v?.getChildAt(0)?.measuredHeight!!)) {
                    view.detailforum_progressbar.visibility = View.VISIBLE
                    id_question?.let { presenterDetailForum.getDetailComment(it, 1,id_user!!) }
                }
            }
        })
        view.detail_comment_forum_recyclerview.scrollToPosition(-1)
        /*--------------------Lấy ảnh bình luận-----------------------------------------*/

        view.camera_comment.setOnClickListener {

            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 261)
            }
        }

        /*----------------------Đăng -Bình luận-----------------------*/


        btndang_detail_forum.setOnClickListener {
            detailforum_progressbar.visibility = View.VISIBLE
            if (edt_detail_forum.text.isEmpty()) {
                Toast.makeText(context, "Bạn chưa nhập bình luận!", Toast.LENGTH_LONG).show()
                detailforum_progressbar.visibility = View.GONE
            } else {
                CallApiComment(edt_detail_forum.text.toString(), PreferenceUtils.getUser().id, id_question!!, uri)
                edt_detail_forum.text.clear()
                chuacocomment.text = ""
                soluotbinhluan = soluotbinhluan!! +1
                if (soluotbinhluan!!>0)
                {
                    soluotbinhluan_detail.text = "$soluotbinhluan Bình luận"
                }
              //  adapter_comment.dataList.add(CommentDto(id_user!!,edt_detail_forum.text.toString(),null,"",id_question!!,java.time.LocalDateTime.now().toString(),0,0,false))
                //presenterDetailForum.getDetailComment(id_question!!, page_comment,id_user!!)
                val call : Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment> = ApiClient.getClient.getCommentQuestion(
                    id_question!!,page_comment,id_user!!
                )
                call.enqueue(object : Callback<ie.app.uetstudents.ui.Entity.Comment.get.Comment>{
                    override fun onResponse(
                        call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                        response: Response<ie.app.uetstudents.ui.Entity.Comment.get.Comment>
                    ) {
                        if (response.isSuccessful)
                        {
                            adapter_comment.setData(response.body()!!.commentDtoList as ArrayList<CommentDto>)
                            view.detail_comment_forum_recyclerview.adapter?.notifyDataSetChanged()
                            view.detail_comment_forum_recyclerview.adapter = adapter_comment
                            detailforum_progressbar.visibility = View.GONE
                            Log.e("Test_call_lai_comment","Thành công")
                        }
                    }

                    override fun onFailure(
                        call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                        t: Throwable
                    ) {
                        Log.e("Test_call_lai_comment","Thất bại")
                    }
                })

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data!!
            Toast.makeText(context, "Đã thêm ảnh vào bình luận!", Toast.LENGTH_LONG).show()
        }
    }

    /*-------------------------Click Nút thích comment----------------------------------*/
    override fun clickOnItem(m: ie.app.uetstudents.ui.Entity.Comment.get.CommentDto) {

        // Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show()
        val idcomment = Comment(m.id!!.toInt())
        val account = Account(PreferenceUtils.getUser().id)
        val likeComment = like_comment(account, idcomment)
        val call: Call<like_comment> = ApiClient.getClient.setLikeComment(likeComment)
        call.enqueue(object : Callback<like_comment> {
            override fun onResponse(call: Call<like_comment>, response: Response<like_comment>) {

                if (response.isSuccessful) {
                    Log.e("Test_API_Like_Comment", "Thành công")
                    update_notification_comment(
                        "LIKE",
                        m.id!!,
                        PreferenceUtils.getUser().username.toString()
                    )
                }
            }

            override fun onFailure(call: Call<like_comment>, t: Throwable) {
                Log.e("Test_API_Like_Comment", "Thất bại")
            }
        })
    }

    /*-----------------------Lấy thông tin nội dung Question-----------------------*/
    @SuppressLint("ResourceAsColor")
    override fun getDataView(data: QuestionDtoX) {
        txtcontent_forum.text = data.content.toString()
        //Log.e("data", data.content.toString())
        val urlAvatar = "${ApiClient.BASE_URL}image${data.accountDto?.avatar}"
        Glide.with(requireActivity())
            .load(urlAvatar)
            .placeholder(R.drawable.img_default_user)
            .error(R.drawable.img_default_user)
            .into(image_detail_forum)

        txt_name_detail_forum.text = data.accountDto?.username
        val thoigian: String = data.time?.substring(11, 16).toString()
        val ngay: String = data.time?.substring(0, 10).toString()
        time_detail_forum.setText(thoigian)
        date_detail_forum.setText(ngay)
        soluotbinhluan = data.comment_quantity
        soluotthich = data.like_quantity
        var liked : Boolean = data.liked
        if(data.comment_quantity != 0)
        {
            soluotbinhluan_detail.text = "${data.comment_quantity} bình luận"
        }
        if (data.like_quantity > 0)
        {
            soluotlike_detail.text = "${data.like_quantity} Người thích"
        }

        if (liked == false)
        {
            imagelike_detail.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            textlike_detail.text= "Thích"
        }
        else
        {
            imagelike_detail.setImageResource(R.drawable.ic_baseline_favorite_24)
            textlike_detail.text = "Đã Thích"
        }
        var listlink: ArrayList<String> = ArrayList()
        if (data.imageDtoList.isNotEmpty()) {
            data.imageDtoList.forEach {
                listlink.add(it.image)
            }
        }
        val adapterhienthi = adapter_hienthianh(listlink)
        detail_listanh.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        detail_listanh.adapter = adapterhienthi

        btn_like_detail.setOnClickListener {
            if (liked == false) {
                textlike_detail.text = "Đã thích"
                textlike_detail.setTextColor(R.color.purple_500)
                imagelike_detail.setImageResource(R.drawable.ic_baseline_favorite_24)
                PostApiLike(id_question!!, id_user!!)
                presenterDetailForum.getDetailForum(id_question!!,id_user!!)
                soluotthich = soluotthich!! + 1
                soluotlike_detail.text = "$soluotthich Người thích"
                liked = true
            }
            else if (liked== true ) {
                textlike_detail.text = "Thích"
                textlike_detail.setTextColor(R.color.black)
                imagelike_detail.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                soluotthich = soluotthich!! -1
                liked = false
                if (soluotthich!! >0)
                {
                    soluotlike_detail.text = "$soluotthich Người thích"
                }

                val call: Call<like_question> =
                    ApiClient.getClient.deletelikeQueston(id_user!!, id_question!!)
                call.enqueue(object : Callback<like_question> {
                    override fun onResponse(
                        call: Call<like_question>,
                        response: Response<like_question>
                    ) {
                        if (response.isSuccessful) {
                            Log.e("Test_bỏ_like", "Thành CÔng")
                        }
                    }

                    override fun onFailure(call: Call<like_question>, t: Throwable) {
                        Log.e("Test_bỏ_like", "Thất bại")
                    }
                })
              //  presenterDetailForum.getDetailForum(id_question!!,id_user!!)
            }


        }
    }

    /*----------------Lấy Thông tin commnet---------------------------*/
    override fun getDataViewComment(datacomment: ie.app.uetstudents.ui.Entity.Comment.get.Comment) {

        adapter_comment.setData(datacomment.commentDtoList as ArrayList<CommentDto>)
        adapter_comment.notifyDataSetChanged()
        if (datacomment.commentDtoList.isEmpty()) {
            chuacocomment.text = "Chưa có bình luận nào"
        } else {
            chuacocomment.text = ""
        }
        detailforum_progressbar.visibility = View.GONE
    }


    /*-----------------------Post comment lên  database---------------------------*/
    fun CallApiComment(comment: String, userid: Int, question_id: Int, uri: Uri?) {
        val account = ie.app.uetstudents.ui.Entity.Comment.post.Account(userid)
        val question = Question(question_id)
        val comment_post = comment_post(account, comment, question)
        val gson = Gson()
        val comment_to_json = gson.toJson(comment_post).toString()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("Comment", comment_to_json)

        uri?.let {
            val strRealPath = RealPathUtil.getRealPath(requireContext(), uri)
            val file = File(strRealPath)
            builder.addFormDataPart("image_file", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
            this.uri = null
        }

        val call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment> = ApiClient.getClient.setCommentQuestion(builder.build())
        call.enqueue(object : Callback<ie.app.uetstudents.ui.Entity.Comment.get.Comment> {
            override fun onResponse(
                call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                response: Response<ie.app.uetstudents.ui.Entity.Comment.get.Comment>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(context, "Bình luận thành công", Toast.LENGTH_SHORT).show()
                    update_notification(
                        "COMMENT",
                        id_question!!,
                        PreferenceUtils.getUser().username.toString(),
                        owner_username ?: ""
                    )

                }
            }

            override fun onFailure(
                call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                t: Throwable
            ) {
                Log.e("Đăng comment", "Thất bại")
            }
        })



    }

    /*------------------ẩn menu-----------------------------*/
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem = menu.findItem(R.id.action_profile)
        val item1: MenuItem = menu.findItem(R.id.action_search)
        val item2: MenuItem = menu.findItem(R.id.action_notification)
        item.isVisible = false
        item1.isVisible = false
        item2.setVisible(false)
    }

    /*------------------------PostLike bài viết---------------------------------*/
    fun PostApiLike(id_question: Int, id_account: Int) {
        val account = ie.app.uetstudents.ui.Entity.like_question.post.Account(id_account)
        val question = ie.app.uetstudents.ui.Entity.like_question.post.Question(id_question)
        val likeQuestion = like_question(account, question)
        val call: Call<like_question> = ApiClient.getClient.postlikequestion(likeQuestion)
        call.enqueue(object : Callback<like_question> {
            override fun onResponse(call: Call<like_question>, response: Response<like_question>) {
                if (response.isSuccessful) {
                    update_notification(
                        "LIKE",
                        id_question,
                        PreferenceUtils.getUser().username.toString(),
                        owner_username ?: ""
                    )
                    Log.e("Test_PostLike", "thành công")
                }
            }

            override fun onFailure(call: Call<like_question>, t: Throwable) {
                Log.e("Test_postlike", "thất Bại")
            }
        })
    }

    /*-------------------------Lấy số người thích bài viết-----------------------------*/


    /*---------------------------update notification question-------------------------------------------*/
    fun update_notification(type_action: String, id_question: Int, username: String, owner_username : String) {
        if (PreferenceUtils.getUser().avatar != null)
        {
            val notifi_item = notification_question_post(
                type_action,
                PreferenceUtils.getUser().avatar.toString(),
                ie.app.uetstudents.ui.Entity.notifications_question.post.Question(id_question),
                username,
                owner_username
            )
            presenterDetailForum.setNotificationQuestion(notifi_item)
        }
        else
        {
            val notifi_item = notification_question_post(
                type_action,
                null,
                ie.app.uetstudents.ui.Entity.notifications_question.post.Question(id_question),
                username,
                owner_username
            )
            presenterDetailForum.setNotificationQuestion(notifi_item)
        }

    }

    /*-----------------------------update notification comment-----------------------------------------------*/
    fun update_notification_comment(type: String, id_comment: Int, username: String) {

        if (PreferenceUtils.getUser().avatar != null)
        {
            val notifiItem = post_notifi_comment(
                type,
                PreferenceUtils.getUser().avatar.toString(),
                ie.app.uetstudents.ui.Entity.notifications_comment.post.Comment(id_comment),
                username
            )
            presenterDetailForum.setNotificationComment(notifiItem)
        }else
        {
            val notifiItem = post_notifi_comment(
                type,
                null,
                ie.app.uetstudents.ui.Entity.notifications_comment.post.Comment(id_comment),
                username
            )
            presenterDetailForum.setNotificationComment(notifiItem)
        }

    }

    private fun openGallery() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"))
        startActivityForResult(Intent.createChooser(intent, "select picture"), CAMERA_REQUEST)
    }
}
