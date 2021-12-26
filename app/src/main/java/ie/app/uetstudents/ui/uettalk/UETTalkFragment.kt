package ie.app.uetstudents.ui.uettalk

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import ie.app.uetstudents.R
import ie.app.uetstudents.RealPathUtil.RealPathUtil
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.*
import ie.app.uetstudents.databinding.FragmentUettalkBinding
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.API.ApiClient.BASE_URL
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.Comment.post.Question
import ie.app.uetstudents.Entity.Comment.post.comment_post
import ie.app.uetstudents.Entity.Question.get.ImageDto
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.Entity.Question.get.question
import ie.app.uetstudents.Entity.like.Post.Account
import ie.app.uetstudents.Entity.like.Post.Comment
import ie.app.uetstudents.Entity.like.Post.like_comment
import ie.app.uetstudents.Entity.like_question.post.like_question
import ie.app.uetstudents.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.Entity.subcomment.post.Subcomment_post
import ie.app.uetstudents.Entity.userProfile.get.userprofile
import ie.app.uetstudents.ui.detailForum.DetailForumContract
import ie.app.uetstudents.ui.detailForum.DetailForumPresenter
import ie.app.uetstudents.ui.diendan.forum_main.forumContract
import ie.app.uetstudents.ui.diendan.forum_main.forumPresenter
import ie.app.uetstudents.ui.tailieu.detailPDF
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_detail_forum.*
import kotlinx.android.synthetic.main.fragment_uettalk.*
import kotlinx.android.synthetic.main.fragment_uettalk.view.*
import kotlinx.android.synthetic.main.layout_bottomsheet.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UETTalkFragment : Fragment(), forumContract.View, OnClickItem_UetTalk,
    DetailForumContract.View,
    ClickItemCommentLike, BaseAdapter.OnclickPdf<ImageDto>,Clicktext {

    private val CAMERA_REQUEST: Int = 8888
    private var _binding: FragmentUettalkBinding? = null

    private val binding get() = _binding!!

    private lateinit var presenter: forumContract.Presenter
    private lateinit var adapter_uettalk: AdapterUETTalk

    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var adapter_comment_uettalk: CommentAdapter
    private lateinit var presenter_uettalk_comment: DetailForumContract.Presenter

    private lateinit var bottomSheetView: View

    private var page_comment: Int = 1
    private var page_uettalk: Int = 1
    var id_user: Int? = null

    var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id_user = PreferenceUtils.getUser().id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUettalkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter = forumPresenter(this, Repository(requireContext()))
        presenter_uettalk_comment = DetailForumPresenter(this, Repository(requireContext()))


        presenter.getQuestions(2, page_uettalk, id_user!!)
        adapter_uettalk = AdapterUETTalk(requireContext(), this, this)
        root.recyclerview_item_uettalk.layoutManager = LinearLayoutManager(requireContext())
        root.recyclerview_item_uettalk.isNestedScrollingEnabled = false


        root.recyclerview_item_uettalk.adapter = adapter_uettalk
        root.recyclerview_item_uettalk.scrollToPosition(0)
        root.uettalk_scrollview.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int) {
                    page_uettalk++
                    root.uet_talk_progressbar.visibility = View.VISIBLE
                    presenter.getQuestions(2, page_uettalk, id_user!!)
                }
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferenceUtils.getInstance()
        val user = PreferenceUtils.getUser()
        val urlAvatar = "${BASE_URL}image${user.avatar}"

        Glide.with(requireContext())
            .load(urlAvatar)
            .placeholder(R.drawable.img_default_user)
            .error(R.drawable.img_default_user)
            .into(image_uettalk)

        status_uettalk.setOnClickListener {
            this.findNavController().navigate(R.id.action_nav_uettalk_to_writingStatusFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*-----------------update dữ liệu question vào Layout -----------------------------------*/
    override fun updateViewData(data: question) {
        uet_talk_progressbar?.visibility = View.GONE
        adapter_uettalk.setData(data.questionDtoList)
        recyclerview_item_uettalk?.adapter?.notifyDataSetChanged()
    }

    /*-----------------------Click vào btn thích------------------------------------*/
    override fun ClickItem_like(QuestionDto: QuestionDtoX,liked: Boolean) {
        if (liked == true) {

            PostApiLike(QuestionDto.id, QuestionDto.accountDto?.username ?: "", id_user!!)
            // update_notification("LIKE", QuestionDto.id!!, PreferenceUtils.getUser().username.toString(), QuestionDto.accountDto?.username ?: "")
        } else {
            deleteLikeQuestion(id_user!!, QuestionDto.id)
        }


    }

    /*-----------------------Chuyenr đến bottomsheet comment--------------------------------*/
    override fun ClickItem_comment(QuestionDto: QuestionDtoX) {
        // Toast.makeText(context,"Bình luận",Toast.LENGTH_LONG).show()
        //presenter_uettalk_comment = DetailForumPresenter(this, Repository(requireContext()))


        adapter_comment_uettalk = CommentAdapter(this,this)


        bottomSheetDialog = BottomSheetDialog(
            this@UETTalkFragment.requireContext(), R.style.BottomSheetDialogTheme
        )
        bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.sheetbottom_comment_uettalk, bottomsheet_uettalk)

        bottomSheetView.comment_recyclerview_uettalk.layoutManager =
            LinearLayoutManager(context)

        presenter_uettalk_comment.getDetailComment(
            QuestionDto.id?.toInt()!!,
            page_comment,
            id_user!!
        )
        bottomSheetView.comment_recyclerview_uettalk.isNestedScrollingEnabled = false
        bottomSheetView.comment_recyclerview_uettalk.adapter = adapter_comment_uettalk
        bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()


        adapter_comment_uettalk.listener(object : truyen_name_account{
            override fun truyen_name_account(
                id_account: Int,
                id_comment: Int,
                recyclerView: RecyclerView
            ) {
                val call : Call<userprofile> = ApiClient.getClient.getUserProfile(id_account)
                call.enqueue(object : Callback<userprofile>{
                    override fun onResponse(
                        call: Call<userprofile>,
                        response: Response<userprofile>
                    ) {
                        if (response.isSuccessful)
                        {
                            val username : String = response.body()!!.fullname.toString()
                            val stringspanner = SpannableString("@user/$username ")
                            val fcolor = ForegroundColorSpan(Color.BLUE)
                            stringspanner.setSpan(RelativeSizeSpan(1.0f),0,stringspanner.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            stringspanner.setSpan(fcolor,0,stringspanner.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            stringspanner.setSpan(StyleSpan(Typeface.BOLD),0,stringspanner.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                            bottomSheetView.edt_comment_uettalk.setText(stringspanner, TextView.BufferType.EDITABLE)

                            bottomSheetView.edt_comment_uettalk.setSelection(bottomSheetView.edt_comment_uettalk.text.length)
                            bottomSheetView.edt_comment_uettalk.requestFocus()
                            if (bottomSheetView.edt_comment_uettalk.text.toString().contains("@user/"))
                            {
                                val begin : Int = bottomSheetView.edt_comment_uettalk.text.toString().indexOf("@user/")
                                val end : Int = bottomSheetView.edt_comment_uettalk.text.toString().indexOf(" ",begin)
                                bottomSheetView.edt_comment_uettalk.setOnClickListener {
                                    bottomSheetView.edt_comment_uettalk.setSelection(begin,end)
                                    val bundle = Bundle()
                                    bundle.putInt("id_user",id_account)
                                    this@UETTalkFragment.findNavController().navigate(R.id.action_nav_uettalk_to_action_profile,bundle)
                                    bottomSheetDialog!!.dismiss()
                                }
                            }

                            bottomSheetView.btn_update_comment_uettalk.setOnClickListener {
                                CallApiSubComment(edt_detail_forum.text.toString(), PreferenceUtils.getUser().id, id_comment!!, uri)
                                bottomSheetView.edt_comment_uettalk.text.clear()
                                val call : Call<getsubcomment> = ApiClient.getClient.getSubComment(id_comment,1)
                                call.enqueue(object : Callback<getsubcomment>{
                                    override fun onResponse(
                                        call: Call<getsubcomment>,
                                        response: Response<getsubcomment>
                                    ) {
                                        val adapter = SubCommentAdapter(id_comment)
                                        adapter.setData(response.body()!!.subCommentDtoList as ArrayList<SubcommentDto>)
                                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                        recyclerView.adapter= adapter
                                        Log.e("lay subcomment","Thành công")
                                        presenter_uettalk_comment.getDetailComment(QuestionDto.id!!,page_comment,id_user!!)
                                    }

                                    override fun onFailure(
                                        call: Call<getsubcomment>,
                                        t: Throwable
                                    ) {
                                        Log.e("lay subcomment","thất bại")
                                    }
                                })
                            }


                        }
                    }

                    override fun onFailure(call: Call<userprofile>, t: Throwable) {
                        Log.e("lấy username comment","Thất bại")
                    }
                })
            }
        })

        bottomSheetView.comment_scrollview.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int) {
                    page_comment++
                    bottomSheetView.comment_progressbar.visibility = View.VISIBLE
                    presenter_uettalk_comment.getDetailComment(
                        QuestionDto.id,
                        page_comment,
                        id_user!!
                    )
                }

            }
        })

        bottomSheetView.comment_uet_camera.setOnClickListener {
            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 261)
            }
        }

        bottomSheetView.btn_update_comment_uettalk.setOnClickListener {
            xulybtncommemt(QuestionDto.id, QuestionDto.accountDto?.username ?: "", uri)
        }

        bottomSheetDialog!!.setContentView(bottomSheetView)
        bottomSheetDialog!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data
            Toast.makeText(context, "Đã thêm ảnh vào bình luận!", Toast.LENGTH_LONG).show()
        }
    }
    private fun CallApiSubComment(content: String, id_account: Int, idComment: Int, uri: Uri?) {
        val account = ie.app.uetstudents.Entity.subcomment.post.Account(id_account)
        val comment = ie.app.uetstudents.Entity.subcomment.post.Comment(idComment)
        val subcommentpost = Subcomment_post(account,comment,content)
        val gson = Gson()
        val comment_to_json = gson.toJson(subcommentpost).toString()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("SubComment", comment_to_json)

        uri?.let {
            val strRealPath = RealPathUtil.getRealPath(requireContext(), uri)
            val file = File(strRealPath)
            builder.addFormDataPart("image_file", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
            this.uri = null
        }

        val call: Call<getsubcomment> = ApiClient.getClient.postSubcomment(builder.build())
        call.enqueue(object : Callback<getsubcomment> {
            override fun onResponse(
                call: Call<getsubcomment>,
                response: Response<getsubcomment>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(context, "Bình luận thành công", Toast.LENGTH_SHORT).show()

                    val notifiItem = post_notifi_comment(
                        "COMMENT",
                        PreferenceUtils.getUser().avatar,
                        ie.app.uetstudents.Entity.notifications_comment.post.Comment(idComment),
                        PreferenceUtils.getUser().toString()
                    )
                    Repository(requireContext()).updateNotifi_Comment(notifiItem)
                }
            }

            override fun onFailure(
                call: Call<getsubcomment>,
                t: Throwable
            ) {
                Log.e("Đăng comment", "Thất bại")
            }
        })


    }

    /*------------------------Click vao itemUet chuyển sang DetailFragment-------------------------------*/
    override fun ClickItem_uettalk(QuestionDto: QuestionDtoX) {
        val bundle = Bundle()
        bundle.putInt("id_question", QuestionDto.id!!)
        this.findNavController().navigate(R.id.action_nav_uettalk_to_detailForumFragment, bundle)
    }

    override fun getDataView(data: QuestionDtoX) {

    }

    /*------------------------update data comment----------------------------------------*/
    override fun getDataViewComment(datacomment: ie.app.uetstudents.Entity.Comment.get.Comment) {
        // bottomSheetView.comment_processbar.progress = View.GONE
        bottomSheetView.comment_progressbar.visibility = View.GONE
        if (datacomment.commentDtoList.isEmpty()) {
            bottomSheetView.txt_comment_chuacobinhluan.text = "Chưa có bình luận nào!"
        } else {
            adapter_comment_uettalk.setData(datacomment.commentDtoList as ArrayList<CommentDto>)
            bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()
        }
    }


    /*--------------------Post Like comment------------------------------*/
    override fun clickOnItem(m: ie.app.uetstudents.Entity.Comment.get.CommentDto,liked : Boolean) {
        if (liked == true)
        {
            val idcomment = Comment(m.id!!.toInt())
            val account = Account(null)
            val likeComment = like_comment(account, idcomment)
            val call: Call<like_comment> = ApiClient.getClient.setLikeComment(likeComment)
            call.enqueue(object : Callback<like_comment> {
                override fun onResponse(call: Call<like_comment>, response: Response<like_comment>) {
                    if (response.isSuccessful) {
                        val notifi_item = post_notifi_comment(
                            "LIKE",
                            "",
                            ie.app.uetstudents.Entity.notifications_comment.post.Comment(m.id),
                            PreferenceUtils.getUser().username.toString()
                        )
                        presenter_uettalk_comment.setNotificationComment(notifi_item)
                    }
                    Log.e("Test_API_Like_Comment", "Thành công")
                }

                override fun onFailure(call: Call<like_comment>, t: Throwable) {
                    Log.e("Test_API_Like_Comment", "Thất bại")
                }
            })
        }else
        {
            Repository(requireContext()).deletelike_comment(id_user!!,m.id!!)
        }

    }

    /*--------------------------------Xử lý khi click btn đăng binh luận-----------------------------------------*/
    fun xulybtncommemt(id_question: Int, owner_username: String, uri: Uri?) {
        if (bottomSheetView.edt_comment_uettalk.text.isEmpty()) {
            Toast.makeText(context, "Bạn Chưa nhập bình luận!", Toast.LENGTH_LONG).show()
        } else {
            val commentpost = comment_post(
                ie.app.uetstudents.Entity.Comment.post.Account(PreferenceUtils.getUser().id),
                bottomSheetView.edt_comment_uettalk.text.toString(),
                Question(id_question)
            )
            val gson = Gson()
            val commentstr = gson.toJson(commentpost).toString()

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("Comment", commentstr)

            uri?.let {
                val realpathutil = uri.let { RealPathUtil.getRealPath(requireContext(), it) }
                val file = File(realpathutil)
                builder.addFormDataPart(
                    "image_file",
                    file.name,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }


            val call: Call<ie.app.uetstudents.Entity.Comment.get.Comment> =
                ApiClient.getClient.setCommentQuestion(builder.build())
            call.enqueue(object : Callback<ie.app.uetstudents.Entity.Comment.get.Comment> {
                override fun onResponse(
                    call: Call<ie.app.uetstudents.Entity.Comment.get.Comment>,
                    response: Response<ie.app.uetstudents.Entity.Comment.get.Comment>
                ) {
                    Log.e("Test", "thành công")
                    Toast.makeText(context, "bình luận thành công!", Toast.LENGTH_LONG).show()
                    if (response.isSuccessful) {
                        update_notification(
                            "COMMENT",
                            id_question,
                            PreferenceUtils.getUser().username.toString(),
                            owner_username
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ie.app.uetstudents.Entity.Comment.get.Comment>,
                    t: Throwable
                ) {
                    Log.e("Test", "thất bại")
                }
            })
            bottomSheetView.edt_comment_uettalk.text.clear()


            bottomSheetView.comment_progressbar.visibility = View.VISIBLE

            val call_get: Call<ie.app.uetstudents.Entity.Comment.get.Comment> =
                ApiClient.getClient.getCommentQuestion(id_question, page_comment, id_user!!)
            call_get.enqueue(object : Callback<ie.app.uetstudents.Entity.Comment.get.Comment> {
                override fun onResponse(
                    call: Call<ie.app.uetstudents.Entity.Comment.get.Comment>,
                    response: Response<ie.app.uetstudents.Entity.Comment.get.Comment>
                ) {
                    if (response.isSuccessful) {
                        adapter_comment_uettalk.setData(response.body()?.commentDtoList!! as ArrayList<CommentDto>)
                        bottomSheetView.comment_recyclerview_uettalk?.adapter?.notifyDataSetChanged()
                        bottomSheetView.comment_progressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ie.app.uetstudents.Entity.Comment.get.Comment>,
                    t: Throwable
                ) {
                    Log.e("Test", "lỗi rồi")
                }
            })
            bottomSheetView.recyclerview_item_uettalk?.scrollToPosition(0)
        }
    }

    /*---------------------------Update thông báo Question---------------------------------*/

    fun update_notification(
        type_action: String,
        id_question: Int,
        username: String,
        owner_username: String
    ) {
        if (PreferenceUtils.getUser().avatar != null) {
            val notifi_item = notification_question_post(
                type_action,
                PreferenceUtils.getUser().avatar.toString(),
                ie.app.uetstudents.Entity.notifications_question.post.Question(id_question),
                username,
                owner_username
            )
            presenter_uettalk_comment.setNotificationQuestion(notifi_item)
        } else {
            val notifi_item = notification_question_post(
                type_action,
                null,
                ie.app.uetstudents.Entity.notifications_question.post.Question(id_question),
                username,
                owner_username
            )
            presenter_uettalk_comment.setNotificationQuestion(notifi_item)
        }

    }

    fun openGallery() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"))
        startActivityForResult(Intent.createChooser(intent, "select picture"), CAMERA_REQUEST)
    }

    /*-----------------Post like lên server-------------------*/
    fun PostApiLike(id_question: Int, owner_username: String, id_account: Int) {
        val account = ie.app.uetstudents.Entity.like_question.post.Account(id_account)
        val question = ie.app.uetstudents.Entity.like_question.post.Question(id_question)
        val likeQuestion = like_question(account, question)
        val call: Call<like_question> = ApiClient.getClient.postlikequestion(likeQuestion)
        call.enqueue(object : Callback<like_question> {
            override fun onResponse(call: Call<like_question>, response: Response<like_question>) {
                if (response.isSuccessful) {
                    update_notification(
                        "LIKE",
                        id_question,
                        PreferenceUtils.getUser().username.toString(),
                        owner_username
                    )
                    Log.e("Test_PostLike", "thành công")
                }
            }

            override fun onFailure(call: Call<like_question>, t: Throwable) {
                Log.e("Test_postlike", "thất Bại")
            }
        })
    }

    /*-------------------Delete like----------------------------*/
    fun deleteLikeQuestion(account_id: Int, id_question: Int) {
        val call: Call<like_question> =
            ApiClient.getClient.deletelikeQueston(account_id, id_question)
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
    }

    override fun onItemClick(position: Int, item: ImageDto) {

        val intent = Intent(activity, detailPDF::class.java)
        intent.putExtra("ExamDocument", item.image)
        startActivity(intent)
    }

    override fun clicktext(name_account: String) {
        TODO("Not yet implemented")
    }


}
