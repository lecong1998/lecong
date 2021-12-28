package ie.app.uetstudents.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
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
import ie.app.uetstudents.adapter.*
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Comment.get.Comment
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.Comment.post.Account
import ie.app.uetstudents.Entity.Comment.post.comment_post
import ie.app.uetstudents.Entity.Question.get.ImageDto
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.Entity.Question.get.question
import ie.app.uetstudents.Entity.like.Post.like_comment
import ie.app.uetstudents.Entity.like_question.post.like_question
import ie.app.uetstudents.Entity.notifications_comment.post.post_notifi_comment
import ie.app.uetstudents.Entity.notifications_question.post.Question
import ie.app.uetstudents.Entity.notifications_question.post.notification_question_post
import ie.app.uetstudents.Entity.userProfile.get.userprofile
import ie.app.uetstudents.Entity.userProfile.post.avatar.avatar
import ie.app.uetstudents.ui.tailieu.detailPDF
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_update_anh_profile.view.*
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
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.app.uetstudents.Entity.Search.person.person
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.Entity.subcomment.post.Subcomment_post
import ie.app.uetstudents.data.response.AccountDto
import ie.app.uetstudents.utils.Constants
import kotlinx.android.synthetic.main.fragment_detail_forum.*


class Profile_Fragment : Fragment(), ProfileContract.View, OnClickItem_UetTalk,
    ClickItemCommentLike, ClickItem, BaseAdapter.OnclickPdf<ImageDto> ,Clicktext{

    private val IMAGE_REQUEST: Int = 121
    private val MY_REQUEST: Int = 140
    var id_user: Int? = null
    private lateinit var presenter_profile: ProfileContract.Presenter
    private lateinit var adapterUETTalk: AdapterUETTalk

    private lateinit var adapter: adapter_forum
    private val CAMERA_REQUEST: Int = 222
    var uri: Uri? = null

    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var adapter_comment_uettalk: CommentAdapter
    private lateinit var bottomSheetView: View

    private var page_comment: Int = 1
    private var page_uettalk: Int = 1
    var id_user_other: Int? = null

    private var type_content_id: Int = 2
    private lateinit var view_update_image: View
    private var uri_updateanh: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id_user = PreferenceUtils.getUser().id

        arguments.let {
            if (it?.isEmpty == false) {
                id_user_other = it?.getInt("id_user") as Int
            }

        }


        if (id_user_other != null) {
            id_user = id_user_other
            id_user_other = null
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter_profile = ProfilePresenter(this, Repository(requireContext()))
        /*-----------------------Update ảnh-----------------*/

        view_update_image =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_anh_profile, null)
        if (id_user == PreferenceUtils.getUser().id) {
            profile_image_account.setOnClickListener {
                if (view_update_image.parent != null) {
                    (view_update_image.getParent() as ViewGroup).removeView(view_update_image)
                }

                view_update_image = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_update_anh_profile, null)
                val dialogbuilder: AlertDialog.Builder =
                    AlertDialog.Builder(requireContext(), R.style.DialogFullScreen)

                dialogbuilder.setView(view_update_image)

                var dialog: AlertDialog? = null
                if (dialog == null) {
                    dialog = dialogbuilder.create()
                    dialog.show()

                }





                view_update_image.chonanhmoi.setOnClickListener {
                    onclickRequestPermission()
                }


                view_update_image.capnhatanh.setOnClickListener {
                    val builder = MultipartBody.Builder()
                    builder.setType(MultipartBody.FORM)
                    builder.addFormDataPart("id", id_user!!.toString())
                    if (uri_updateanh != null) {
                        uri_updateanh?.let {
                            val realpathutil = uri_updateanh.let {
                                RealPathUtil.getRealPath(
                                    requireContext(),
                                    it!!
                                )
                            }
                            val file = File(realpathutil)
                            builder.addFormDataPart(
                                "avatar",
                                file.name,
                                RequestBody.create(MediaType.parse("multipart/form-data"), file)
                            )
                        }

                        val call: Call<avatar> =
                            ApiClient.getClient.update_image_profile(builder.build())
                        call.enqueue(object : Callback<avatar> {
                            override fun onResponse(
                                call: Call<avatar>,
                                response: Response<avatar>
                            ) {
                                if (response.isSuccessful) {
                                    Log.e("cập nhật ảnh", response.body()?.message)
                                    Toast.makeText(
                                        context,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    dialog?.dismiss()
                                    presenter_profile.getUserInformation(id_user!!)
                                    PreferenceUtils.getUser().avatar = response.body()!!.userProfileDto.avatar
                                    val account = AccountDto(response.body()!!.userProfileDto.avatar,
                                        PreferenceUtils.getUser().email,
                                    id_user!!,
                                    PreferenceUtils.getUser().password,java.time.LocalDateTime.now().toString(),
                                    PreferenceUtils.getUser().username)
                                    PreferenceUtils.remove(Constants.KEY_PREFERENCE_USER)
                                    PreferenceUtils.saveObject(Constants.KEY_PREFERENCE_USER,account)
                                }
                            }

                            override fun onFailure(call: Call<avatar>, t: Throwable) {
                                Log.e("cập nhật ảnh", t.message)
                            }
                        })

                    } else {
                        Toast.makeText(context, "Bạn chưa chọn ảnh", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }

        /*--------------------------uet--------------------------------*/


        presenter_profile.getQuestionProfile(page_uettalk, id_user!!, type_content_id)
        adapter = adapter_forum(this)
        adapterUETTalk = AdapterUETTalk(requireContext(), this, this)
        presenter_profile.getUserInformation(id_user!!)

        profile_uettalk.setOnClickListener {
            type_content_id = 2
            Toast.makeText(context, "Đã chọn thể loại Bài viết UETTalk", Toast.LENGTH_SHORT).show()
            profile_forum.isChecked = false
            adapterUETTalk.notifyDataSetChanged()
            presenter_profile.getQuestionProfile(page_uettalk, id_user!!, type_content_id)
            profile_list_question.adapter?.notifyDataSetChanged()


        }
        profile_forum.setOnClickListener {
            type_content_id = 1
            Toast.makeText(context, "Đã chọn thể loại Bài viết diễn đàn", Toast.LENGTH_SHORT).show()
            profile_uettalk.isChecked = false
            adapter.notifyDataSetChanged()
            presenter_profile.getQuestionProfile(page_uettalk, id_user!!, type_content_id)
            profile_list_question.adapter?.notifyDataSetChanged()
        }

        profile_scrollview.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v?.measuredHeight)) {
                    page_uettalk++
                    presenter_profile.getQuestionProfile(page_uettalk, id_user!!, type_content_id)
                }
            }
        })


    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item1: MenuItem = menu.findItem(R.id.action_search)
        val item2: MenuItem = menu.findItem(R.id.action_notification)
        val item3: MenuItem = menu.findItem(R.id.action_profile)
        item1.setVisible(false)
        item2.setVisible(false)
        item3.setVisible(false)
    }

    override fun UpdateViewDataQuestionProfile(question: question, type_content_id: Int) {
        if (type_content_id == 2) {
            adapterUETTalk.setData(question.questionDtoList)
            profile_list_question.layoutManager = LinearLayoutManager(context)
            profile_list_question.adapter = adapterUETTalk
            profile_list_question.adapter?.notifyDataSetChanged()
        }
        if (type_content_id == 1) {
            adapter.setData(question.questionDtoList)
            profile_list_question.layoutManager = LinearLayoutManager(context)
            profile_list_question.adapter = adapter
            profile_list_question.adapter?.notifyDataSetChanged()
        }

        profile_list_question.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    override fun UpdateViewDataUser(userprofile: userprofile) {
        if (userprofile.mssv != null) {
            Mssv.text = userprofile.mssv
        } else {
            Mssv.text = "Chưa cập nhật!"
            Mssv.setTextColor(R.color.teal_700)
        }

        username.text = userprofile.fullname
        if (userprofile.email != null) {
            email.text = userprofile.email
        } else {
            email.text = "Chưa cập nhật!"
            email.setTextColor(R.color.teal_700)
        }
        if (userprofile.department != null) {
            khoa.text = userprofile.department.toString()
        } else {
            khoa.text = "Chưa cập nhật!"
            khoa.setTextColor(R.color.teal_700)
        }

        if (userprofile.avatar != null) {
            Glide.with(requireActivity()).load(ApiClient.BASE_URL + "image" + userprofile.avatar)
                .error(R.drawable.img_default_user).into(profile_image_account)
        } else {
            profile_image_account.setImageResource(R.drawable.img_default_user)
        }

        thaydoithongtin.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user", userprofile)
            this.findNavController().navigate(R.id.action_action_profile_to_changeFragment, bundle)
        }
        if (uri_updateanh == null) {
            Glide.with(requireActivity()).load(ApiClient.BASE_URL + "image" + userprofile.avatar)
                .placeholder(R.drawable.img_default_user)
                .error(R.drawable.img_default_user)
                .into(view_update_image.image_update_profile)
        }

    }

    override fun ClickItem_like(QuestionDto: QuestionDtoX,liked: Boolean) {
        if (liked == true) {

            PostApiLike(QuestionDto.id, QuestionDto.accountDto?.username ?: "", id_user!!)
        } else {
            deleteLikeQuestion(id_user!!, QuestionDto.id)
        }

    }

    override fun ClickItem_comment(QuestionDto: QuestionDtoX) {

        adapter_comment_uettalk = CommentAdapter(this,this)


        bottomSheetDialog = BottomSheetDialog(
            this.requireContext(), R.style.BottomSheetDialogTheme
        )
        bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.sheetbottom_comment_uettalk, bottomsheet_uettalk)

        bottomSheetView.comment_recyclerview_uettalk.layoutManager =
            LinearLayoutManager(context)

        getComment(QuestionDto.id, page_comment, id_user!!)

        bottomSheetView.edt_comment_uettalk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.e("call person","Thành công")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().toLowerCase().contains("@user/"))
                {
                    val str: String = s.toString()
                    val begin = str.indexOf("@user/")
                    val startkitu = str.lastIndexOf("/", s.toString().length)
                    val end = str.indexOf(" ", begin)
                    if (end<0)
                    {
                        val text = s?.substring(startkitu+1,s?.length).toString()
                        val call : Call<person> = ApiClient.getClient.getPerSonSearch(1,text!!)
                        call.enqueue(object : Callback<person>{
                            override fun onResponse(
                                call: Call<person>,
                                response: Response<person>
                            ) {
                                if (response.isSuccessful)
                                {
                                    val person : person = response.body()!!
                                    val adapterperson = adapter_person(person.accountDtoList)
                                    bottomSheetView.listperson_uet.adapter = adapterperson
                                    Log.e("call person","Thành công")
                                }
                            }

                            override fun onFailure(call: Call<person>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }

                bottomSheetView.listperson_uet.setOnItemClickListener(object : AdapterView.OnItemClickListener
                {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val startkitu = s.toString().lastIndexOf("/", s.toString().length)
                        val p = bottomSheetView.listperson_uet.getItemAtPosition(position) as AccountDto
                        val str = s.toString()?.substring(0,startkitu)
                        var string = ""
                        if (str.contains("@user/"))
                        {

                            string = str + p.username+" "
                        }else
                        {
                            string = str+"/" + p.username+" "
                        }
                        val begin = string.indexOf("@user/")
                        val end = string.indexOf(" ", begin)
                        bottomSheetView.edt_comment_uettalk.text.clear()

                        val stringspanner = SpannableString(string)
                        val fcolor = ForegroundColorSpan(Color.BLUE)
                        stringspanner.setSpan(RelativeSizeSpan(1.0f),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        stringspanner.setSpan(fcolor,begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        stringspanner.setSpan(StyleSpan(Typeface.BOLD),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                        bottomSheetView.edt_comment_uettalk.setText(stringspanner)
                        bottomSheetView.listperson_uet.visibility = View.GONE
                        bottomSheetView.edt_comment_uettalk.setSelection(bottomSheetView.edt_comment_uettalk.text.length)
                        bottomSheetView.edt_comment_uettalk.requestFocus()
                        bottomSheetView.edt_comment_uettalk.setOnClickListener {
                            bottomSheetView.edt_comment_uettalk.setSelection(begin,end)
                            val bundle = Bundle()
                            bundle.putInt("id_user",p.id)
                            this@Profile_Fragment.findNavController().navigate(R.id.action_action_profile_self,bundle)
                            bottomSheetDialog!!.dismiss()
                        }

                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("call person","Thành công")
            }
        })


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
                                    this@Profile_Fragment.findNavController().navigate(R.id.action_action_profile_self,bundle)
                                    bottomSheetDialog!!.dismiss()
                                }
                            }

                            bottomSheetView.btn_update_comment_uettalk.setOnClickListener {
                                CallApiSubComment(edt_detail_forum.text.toString(), PreferenceUtils.getUser().id, id_comment!!, uri)
                                bottomSheetView.edt_comment_uettalk.text.clear()
                                val call : Call<getsubcomment> = ApiClient.getClient.getSubComment(id_comment,1)
                                call.enqueue(object : Callback<getsubcomment>, Clicktext {
                                    override fun onResponse(
                                        call: Call<getsubcomment>,
                                        response: Response<getsubcomment>
                                    ) {
                                        val adapter = SubCommentAdapter(id_comment,this)
                                        adapter.setData(response.body()!!.subCommentDtoList as ArrayList<SubcommentDto>)
                                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                        recyclerView.adapter= adapter
                                        Log.e("lay subcomment","Thành công")
                                        getComment(QuestionDto.id!!,page_comment,id_user!!)
                                    }

                                    override fun onFailure(
                                        call: Call<getsubcomment>,
                                        t: Throwable
                                    ) {
                                        Log.e("lay subcomment","thất bại")
                                    }

                                    override fun clicktext(name_account: String) {
                                        chuyentrangprofile(name_account)
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
                    // bottomSheetView.comment_progressbar.visibility = View.VISIBLE
                    getComment(QuestionDto.id, page_comment, id_user!!)
                }

            }
        })



        bottomSheetView.comment_uet_camera.setOnClickListener {
            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery_comment()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 261)
            }
        }

        bottomSheetView.btn_update_comment_uettalk.setOnClickListener {


            Log.e("uri", uri.toString())
            xulybtncommemt(QuestionDto.id, QuestionDto.accountDto?.username ?: "", uri)
        }

        bottomSheetDialog!!.setContentView(bottomSheetView)
        bottomSheetDialog!!.show()
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


    fun openGallery() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select picture"), IMAGE_REQUEST)
    }

    fun openGallery_comment() {
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
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uri_updateanh = data.data
            val photo: Bitmap =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri_updateanh)
            view_update_image.image_update_profile.setImageBitmap(photo)
            Toast.makeText(context, "Đã thêm ảnh vào trường update", Toast.LENGTH_LONG).show()
        }
    }

    private fun getComment(id_question: Int, page: Int, id_account: Int) {
        val call: Call<Comment> =
            ApiClient.getClient.getCommentQuestion(id_question, page, id_account)
        call.enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    adapter_comment_uettalk.setData(response.body()!!.commentDtoList as ArrayList<CommentDto>)
                    bottomSheetView.comment_recyclerview_uettalk.isNestedScrollingEnabled = false
                    bottomSheetView.comment_recyclerview_uettalk.adapter = adapter_comment_uettalk
                    bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()

                    bottomSheetView.comment_progressbar.visibility = View.GONE
                    Log.e("Test comment", "Thành công")
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("Test comment", "Thất bại")
            }
        })

    }


    override fun ClickItem_uettalk(QuestionDto: QuestionDtoX) {
        val bundle = Bundle()
        bundle.putInt("id_question", QuestionDto.id!!)
        bundle.putString("owner_username", QuestionDto.accountDto?.username ?: "")
        Toast.makeText(context, QuestionDto.id.toString(), Toast.LENGTH_SHORT).show()
        this.findNavController().navigate(R.id.action_action_profile_to_detailForumFragment, bundle)
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

    fun update_notification(
        type_action: String,
        id_question: Int,
        username: String,
        owner_username: String
    ) {
        if (PreferenceUtils.getUser().avatar != null)
        {
            val notifi_item = notification_question_post(
                type_action,
                PreferenceUtils.getUser().avatar,
                Question(id_question),
                username,
                owner_username
            )
            Repository(requireContext()).updateNotifi_Question(notifi_item)
        }else
        {
            val notifi_item = notification_question_post(
                type_action,
                "",
                Question(id_question),
                username,
                owner_username
            )
            Repository(requireContext()).updateNotifi_Question(notifi_item)
        }

    }

    override fun clickOnItem(m: ie.app.uetstudents.Entity.Comment.get.CommentDto, liked : Boolean) {
        if (liked == true) {
            val idcomment = ie.app.uetstudents.Entity.like.Post.Comment(m.id!!.toInt())
            val account = ie.app.uetstudents.Entity.like.Post.Account(null)
            val likeComment = like_comment(account, idcomment)
            val call: Call<like_comment> = ApiClient.getClient.setLikeComment(likeComment)
            call.enqueue(object : Callback<like_comment> {
                override fun onResponse(
                    call: Call<like_comment>,
                    response: Response<like_comment>
                ) {
                    if (response.isSuccessful) {
                        val notifi_item = post_notifi_comment(
                            "LIKE",
                            "",
                            ie.app.uetstudents.Entity.notifications_comment.post.Comment(m.id),
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
        }else
        {
            Repository(requireContext()).deletelike_comment(PreferenceUtils.getUser().id,m.id!!)
        }
    }

    /*-------------------------------------------------*/
    fun xulybtncommemt(id_question: Int, owner_username: String, uri: Uri?) {
        if (bottomSheetView.edt_comment_uettalk.text.isEmpty()) {
            Toast.makeText(context, "Bạn Chưa nhập bình luận!", Toast.LENGTH_LONG).show()
        } else {
            val commentpost = comment_post(
                Account(PreferenceUtils.getUser().id),
                bottomSheetView.edt_comment_uettalk.text.toString(),
                ie.app.uetstudents.Entity.Comment.post.Question(id_question)
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
                            PreferenceUtils.getUser().username!!,
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

    override fun clickOnItem(m: QuestionDtoX) {
        val bundle = Bundle()
        bundle.putInt("id_question", m.id!!)
        bundle.putString("owner_username", m.accountDto?.username ?: "")
        Toast.makeText(context, m.id.toString(), Toast.LENGTH_SHORT).show()
        this.findNavController().navigate(R.id.action_action_profile_to_detailForumFragment, bundle)
    }

    override fun onItemClick(position: Int, item: ImageDto) {
        val intent = Intent(activity, detailPDF::class.java)
        intent.putExtra("ExamDocument", item.image)
        startActivity(intent)
    }


    private fun onclickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery()
            return
        }
        if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_REQUEST && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    override fun clicktext(name_account: String) {
        chuyentrangprofile(name_account)
    }
    fun chuyentrangprofile(username: String)
    {
        val call : Call<userprofile> = ApiClient.getClient.getUserProfile(username)
        call.enqueue(object : Callback<userprofile>{
            override fun onResponse(call: Call<userprofile>, response: Response<userprofile>) {
                if (response.isSuccessful)
                {
                    bottomSheetDialog?.dismiss()
                    val id_account = response.body()!!.id
                    val bundle = Bundle()
                    bundle.putInt("id_user",id_account)
                    this@Profile_Fragment.findNavController().navigate(R.id.action_action_profile_self,bundle)

                }
            }

            override fun onFailure(call: Call<userprofile>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}