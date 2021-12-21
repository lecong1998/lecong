package ie.app.uetstudents.ui.uettalk

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ie.app.uetstudents.R
import ie.app.uetstudents.adapter.OnclickItem_deleteanh
import ie.app.uetstudents.adapter.adapter_anhwrite
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.Question.post.Account
import ie.app.uetstudents.ui.Entity.Question.post.Category
import ie.app.uetstudents.ui.Entity.Question.post.QuestionPost
import ie.app.uetstudents.ui.Entity.Question.post.TypeContent
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_writing_status.*
import kotlinx.android.synthetic.main.fragment_writing_status.view.*
import kotlinx.android.synthetic.main.layout_bottomsheet_anh.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class WritingStatusFragment : Fragment(), OnclickItem_deleteanh {


    private val MY_REQUEST: Int = 1111
    private val IMG_REQUEST: Int = 1000
    private val DOCUMENT_REQUEST: Int = 1005
    private val CAMERA_REQUEST: Int = 100

    private var listanh: ArrayList<Uri> = ArrayList()
    private lateinit var adapteranh: adapter_anhwrite


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_writing_status, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity())
            .load(ApiClient.BASE_URL+"image"+PreferenceUtils.getUser().avatar)
            .placeholder(R.drawable.img_default_user)
            .error(R.drawable.img_default_user)
            .into(image_status)
        name_user_status.text = PreferenceUtils.getUser().username


        /*-------------------Chọn ảnh--------------------------------*/

        view.btn_addimage_status.setOnClickListener {

            val alertDialogbuild: AlertDialog.Builder = AlertDialog.Builder(context)
            val dialogview =
                LayoutInflater.from(context).inflate(R.layout.layout_bottomsheet_anh, null)
            alertDialogbuild.setView(dialogview)
            val dialog = alertDialogbuild.create()
            dialog.show()

            dialogview.anh_camera.setOnClickListener {
//
//                val cameraIntent =
//                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(cameraIntent, CAMERA_REQUEST)
                onclickRequestPermission("image")
                dialog.dismiss()
            }
            dialogview.anh_thumuc.setOnClickListener {
                onclickRequestPermission("pdf")
                dialog.dismiss()
            }

        }
        adapteranh = adapter_anhwrite(listanh, this)
        if (listanh == null) {
            write_themanh.visibility = View.GONE
        } else {
            write_themanh.visibility = View.VISIBLE
        }
        write_themanh.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        write_themanh.adapter = adapteranh

        /*------------------Đăng bài viết-----------------------------*/
        update_status.setOnClickListener {
            if (edt_status.text.isEmpty() && listanh.isEmpty()) {
                Toast.makeText(context, "Bạn chưa nhập nội dung đăng", Toast.LENGTH_SHORT).show()
            } else {
                callApi(edt_status.text.toString(), listanh, PreferenceUtils.getUser().id)
            }

        }
    }

    fun onclickRequestPermission(theloai: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (theloai.equals("image")) {
                openGallery()
            }
            if (theloai.equals("pdf")) {
                openDocument()
            }

            return
        }
        if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (theloai.equals("image")) {
                openGallery()
            }
            if (theloai.equals("pdf")) {
                openDocument()
            }
        } else {
            if (theloai.equals("image")) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST)
            }
            if (theloai.equals("pdf")) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    DOCUMENT_REQUEST
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_REQUEST) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }

    }
    fun openGallery() {
        val intent: Intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        //mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"))
        startActivityForResult(Intent.createChooser(intent, "select picture"), IMG_REQUEST)
    }

    private fun openDocument() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), DOCUMENT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            listanh.add(uri!!)
            write_themanh.adapter?.notifyDataSetChanged()


        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            listanh.add(uri!!)
            write_themanh.adapter?.notifyDataSetChanged()
        } else if (requestCode == DOCUMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val uripdf = data?.data
            uripdf?.let {
                listanh.add(it)
                write_themanh.adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun callApi(writeContent: String, listUri: List<Uri>, user: Int) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        val category = Category(1)
        val typeContent = TypeContent(2)
        val account = Account(user)
        val question = QuestionPost(account, category, writeContent, writeContent, typeContent)
        val gson = Gson()
        val questionString = gson.toJson(question).toString()

        builder.addFormDataPart("Question", questionString)

        listUri.forEach {
            val fileName = getSelectedFileName(it)
            val byteDataFile = getByteDataFile(it)
            builder.addFormDataPart(
                "image_files",
                fileName,
                RequestBody.create(MediaType.parse("multipart/form-data"), byteDataFile)
            )
        }

        val call: Call<question> = ApiClient.getClient.setQuestion(builder.build())
        call.enqueue(object : Callback<question> {
            override fun onResponse(call: Call<question>, response: Response<question>) {
                if (response.isSuccessful) {
                    Log.e("Đăng thành công", "Đăng thành công")
                    findNavController().navigate(R.id.action_writingStatusFragment_to_nav_uettalk)
                }
            }

            override fun onFailure(call: Call<question>, t: Throwable) {
                Log.e("đăng thành công", "Thất bại")
            }
        })
    }

    private fun getSelectedFileName(selectFileUri: Uri): String? {
        val uriString: String = selectFileUri.toString()
        val myFile = File(uriString)
        var displayName: String? = null
        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor =
                    requireContext().contentResolver.query(selectFileUri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }

    private fun getByteDataFile(uri: Uri?): ByteArray? {
        try {
            val input: InputStream? = requireContext().contentResolver.openInputStream(uri!!)
            return getBytes(input!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val item2: MenuItem = menu.findItem(R.id.action_profile)
        val item3: MenuItem = menu.findItem(R.id.action_notification)
        item.isVisible = false
        item2.setVisible(false)
        item3.isVisible = false
    }

    override fun CLickDelete(anh: Uri) {
        listanh.remove(anh)
        write_themanh.adapter?.notifyDataSetChanged()
    }
}
