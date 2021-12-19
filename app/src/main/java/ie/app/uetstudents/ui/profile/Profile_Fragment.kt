package ie.app.uetstudents.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import ie.app.uetstudents.R
import ie.app.uetstudents.RealPathUtil.RealPathUtil
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.AdapterUETTalk
import ie.app.uetstudents.adapter.ClickItemCommentLike
import ie.app.uetstudents.adapter.CommentAdapter
import ie.app.uetstudents.adapter.OnClickItem_UetTalk
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Comment.get.Comment
import ie.app.uetstudents.ui.Entity.Comment.get.CommentDto
import ie.app.uetstudents.ui.Entity.Comment.post.Account
import ie.app.uetstudents.ui.Entity.Comment.post.comment_post
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.like.Post.like_comment
import ie.app.uetstudents.ui.Entity.like_question.post.like_question
import ie.app.uetstudents.ui.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.ui.Entity.notifications_question.post.Question
import ie.app.uetstudents.ui.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.ui.Entity.userProfile.get.userprofile
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_uettalk.view.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Profile_Fragment: Fragment(),ProfileContract.View, OnClickItem_UetTalk, ClickItemCommentLike {

    var id_user : Int? = null
    private lateinit var presenter_profile : ProfileContract.Presenter
    private lateinit var adapterUETTalk: AdapterUETTalk
    private val CAMERA_REQUEST: Int = 222
    var uri: Uri? = null

    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var adapter_comment_uettalk: CommentAdapter
    private lateinit var bottomSheetView: View

    private var page_comment: Int = 1
    private var page_uettalk: Int = 1
    var id_user_other : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id_user = PreferenceUtils.getUser().id

            arguments.let {
                if (it?.isEmpty== false)
                {
                    id_user_other = it?.getInt("id_user") as Int
                }

            }


        if(id_user_other != null)
        {
            id_user = id_user_other
            id_user_other = null
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter_profile = ProfilePresenter(this, Repository(requireContext()))
        adapterUETTalk = AdapterUETTalk(this)
        presenter_profile.getQuestionProfile(page_uettalk,id_user!!)
        presenter_profile.getUserInformation(id_user!!)
        profile_list_question.layoutManager = LinearLayoutManager(context)
        profile_list_question.adapter= adapterUETTalk


    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item1 : MenuItem = menu.findItem(R.id.action_search)
        val item2 : MenuItem = menu.findItem(R.id.action_notification)
        val item3 : MenuItem = menu.findItem(R.id.action_profile)
        item1.setVisible(false)
        item2.setVisible(false)
        item3.setVisible(false)
    }

    override fun UpdateViewDataQuestionProfile(question: question) {
        adapterUETTalk.setData(question.questionDtoList)
        profile_list_question.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    override fun UpdateViewDataUser(userprofile: userprofile) {
        Mssv.text = userprofile.mssv
        username.text = userprofile.fullname
        if(userprofile.email!= null)
        {
            email.text = userprofile.email
        }
        else
        {
            email.text = "Chưa cập nhật!"
            email.setTextColor(R.color.teal_700)
        }
        if (userprofile.department != null)
        {
            khoa.text = userprofile.department.toString()
        }else
        {
            khoa.text = "Chưa cập nhật!"
            khoa.setTextColor(R.color.teal_700)
        }

        if (userprofile.avatar != null) {
            Glide.with(requireActivity()).load(ApiClient.BASE_URL+"image"+userprofile.avatar)
                .error(R.drawable.img_default_user).into(profile_image_account)
        }else
        {
            profile_image_account.setImageResource(R.drawable.img_default_user)
        }

        thaydoithongtin.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user",userprofile)
            this.findNavController().navigate(R.id.action_action_profile_to_changeFragment,bundle)
        }
    }

    override fun ClickItem_like(QuestionDto: QuestionDtoX) {
        if(QuestionDto.liked == false)
        {
            QuestionDto.liked = true
            PostApiLike(QuestionDto.id, QuestionDto.accountDto?.username ?: "" ,id_user!!)
            update_notification("LIKE", QuestionDto.id!!, PreferenceUtils.getUser().username!!, QuestionDto.accountDto?.username ?: "")
        }else
        {
            QuestionDto.liked=false
            deleteLikeQuestion(id_user!!,QuestionDto.id)
        }

    }

    override fun ClickItem_comment(QuestionDto: QuestionDtoX) {

        adapter_comment_uettalk = CommentAdapter(this)


        bottomSheetDialog = BottomSheetDialog(
            this.requireContext(), R.style.BottomSheetDialogTheme
        )
        bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.sheetbottom_comment_uettalk, bottomsheet_uettalk)

        bottomSheetView.comment_recyclerview_uettalk.layoutManager =
            LinearLayoutManager(context)

        getComment(QuestionDto.id,page_comment,id_user!!)
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
                   // bottomSheetView.comment_progressbar.visibility = View.VISIBLE
                    getComment(QuestionDto.id,page_comment,id_user!!)
                }

            }
        })



        bottomSheetView.comment_uet_camera.setOnClickListener {
//            val cameraIntent =
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(cameraIntent, CAMERA_REQUEST)
            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 261)
            }
        }

        bottomSheetView.btn_update_comment_uettalk.setOnClickListener {


            Log.e("uri", uri.toString())
            xulybtncommemt(QuestionDto.id, QuestionDto.accountDto?.username ?: "", uri )
        }

        bottomSheetDialog!!.setContentView(bottomSheetView)
        bottomSheetDialog!!.show()
    }

    fun openGallery() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select picture"), CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data
            Toast.makeText(context, "Đã thêm ảnh vào bình luận!", Toast.LENGTH_LONG).show()
            Log.e("uri", uri.toString())
        }
    }
    private fun getComment(id_question: Int,page : Int, id_account: Int) {
        val call : Call<Comment> = ApiClient.getClient.getCommentQuestion(id_question,page,id_account)
        call.enqueue(object : Callback<Comment>{
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful)
                {
                    adapter_comment_uettalk.setData(response.body()!!.commentDtoList as ArrayList<CommentDto>)
                    bottomSheetView.comment_recyclerview_uettalk.isNestedScrollingEnabled = false
                    bottomSheetView.comment_recyclerview_uettalk.adapter = adapter_comment_uettalk
                    bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()


                    Log.e("Test comment" ,"Thành công")
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("Test comment" ,"Thất bại")
            }
        })

    }


    override fun ClickItem_uettalk(QuestionDto: QuestionDtoX) {
        val bundle = Bundle()
        bundle.putInt("id_question", QuestionDto.id!!)
        bundle.putString("owner_username",QuestionDto.accountDto?.username ?: "")
        Toast.makeText(context, QuestionDto.id.toString(), Toast.LENGTH_SHORT).show()
        this.findNavController().navigate(R.id.action_action_profile_to_detailForumFragment, bundle)
    }


    /*-----------------Post like lên server-------------------*/
    fun PostApiLike(id_question: Int, owner_username: String , id_account: Int) {
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
    fun deleteLikeQuestion(account_id: Int,id_question: Int)
    {
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
    fun update_notification(type_action: String, id_question: Int, username: String, owner_username : String) {
        val notifi_item = notification_question_post(
            type_action,
            "",
            Question(id_question),
            username,
            owner_username
        )
        Repository(requireContext()).updateNotifi_Question(notifi_item)
    }

    override fun clickOnItem(m: ie.app.uetstudents.ui.Entity.Comment.get.CommentDto) {
        val idcomment = ie.app.uetstudents.ui.Entity.like.Post.Comment(m.id!!.toInt())
        val account = ie.app.uetstudents.ui.Entity.like.Post.Account(null)
        val likeComment = like_comment(account, idcomment)
        val call: Call<like_comment> = ApiClient.getClient.setLikeComment(likeComment)
        call.enqueue(object : Callback<like_comment> {
            override fun onResponse(call: Call<like_comment>, response: Response<like_comment>) {
                if (response.isSuccessful) {
                    val notifi_item = post_notifi_comment(
                        "LIKE",
                        "",
                        ie.app.uetstudents.ui.Entity.notifications_comment.post.Comment(m.id),
                        PreferenceUtils.getUser().username!!
                    )
                    Repository(requireContext()).updateNotifi_Comment(notifi_item)
                }
                Log.e("Test_API_Like_Comment", "Thành công")
            }

            override fun onFailure(call: Call<like_comment>, t: Throwable) {
                Log.e("Test_API_Like_Comment", "Thất bại")
            }
        })
    }

    /*-------------------------------------------------*/
    fun xulybtncommemt(id_question: Int, owner_username: String, uri: Uri?) {
        if (bottomSheetView.edt_comment_uettalk.text.isEmpty()) {
            Toast.makeText(context, "Bạn Chưa nhập bình luận!", Toast.LENGTH_LONG).show()
        } else {
            val commentpost = comment_post(
                Account(PreferenceUtils.getUser().id),
                bottomSheetView.edt_comment_uettalk.text.toString(),
                ie.app.uetstudents.ui.Entity.Comment.post.Question(id_question)
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



            val call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment> =
                ApiClient.getClient.setCommentQuestion(builder.build())
            call.enqueue(object : Callback<ie.app.uetstudents.ui.Entity.Comment.get.Comment> {
                override fun onResponse(
                    call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                    response: Response<ie.app.uetstudents.ui.Entity.Comment.get.Comment>
                ) {
                    Log.e("Test", "thành công")
                    Toast.makeText(context, "bình luận thành công!", Toast.LENGTH_LONG).show()
                    if (response.isSuccessful) {
                        update_notification(
                            "COMMENT",
                            id_question,
                            PreferenceUtils.getUser().username!!,
                            owner_username
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                    t: Throwable
                ) {
                    Log.e("Test", "thất bại")
                }
            })
            bottomSheetView.edt_comment_uettalk.text.clear()


            bottomSheetView.comment_progressbar.visibility = View.VISIBLE

            val call_get: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment> =
                ApiClient.getClient.getCommentQuestion(id_question, page_comment,id_user!!)
            call_get.enqueue(object : Callback<ie.app.uetstudents.ui.Entity.Comment.get.Comment> {
                override fun onResponse(
                    call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                    response: Response<ie.app.uetstudents.ui.Entity.Comment.get.Comment>
                ) {
                    if (response.isSuccessful) {
                        adapter_comment_uettalk.setData(response.body()?.commentDtoList!! as ArrayList<CommentDto>)
                        bottomSheetView.comment_recyclerview_uettalk?.adapter?.notifyDataSetChanged()
                        bottomSheetView.comment_progressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ie.app.uetstudents.ui.Entity.Comment.get.Comment>,
                    t: Throwable
                ) {
                    Log.e("Test", "lỗi rồi")
                }
            })
            bottomSheetView.recyclerview_item_uettalk?.scrollToPosition(0)
        }
    }
}