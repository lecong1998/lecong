package ie.app.uetstudents.ui.profile.change

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.ui.Entity.Account.Put.request.password_put
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.userProfile.get.userprofile
import ie.app.uetstudents.ui.Entity.userProfile.post.email.request.email_request
import ie.app.uetstudents.ui.Entity.userProfile.post.khoa.request.khoa_request
import ie.app.uetstudents.ui.Entity.userProfile.post.mssv.request.mssv_request
import ie.app.uetstudents.ui.profile.ProfileContract
import ie.app.uetstudents.ui.profile.ProfilePresenter
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.change.view.*
import kotlinx.android.synthetic.main.change_password.*
import kotlinx.android.synthetic.main.change_password.view.*
import kotlinx.android.synthetic.main.fragment_change.*

class changeFragment : Fragment(), ProfileContract.View {

    var id_user: Int? = null

    private lateinit var presenter: ProfileContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id_user = PreferenceUtils.getUser().id


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ProfilePresenter(this, Repository(requireContext()))
        presenter.getUserInformation(id_user!!)

        /*-------------------update data---------------------*/


    }

    fun dialogpassword() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        var view_pasword =
            LayoutInflater.from(requireContext()).inflate(R.layout.change_password, null)
        alertDialogBuilder.setView(view_pasword)
        val dialog_password = alertDialogBuilder.create()
        dialog_password.show()
        view_pasword.capnhat_password.setOnClickListener {
            if (view_pasword.dialog_old_password.text.isEmpty() ||
                view_pasword.dialog_reold_password.text.isEmpty() ||
                view_pasword.dialog_new_password.text.isEmpty()
            ) {
                Toast.makeText(context, "Bạn chưa nhập thông tin đầy đủ!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (view_pasword.dialog_old_password.text.toString()
                        .equals(view_pasword.dialog_reold_password.text.toString()) == false
                ) {
                    Toast.makeText(context, "Mật khẩu cũ không khớp!", Toast.LENGTH_LONG).show()
                } else {

                    val passwordPut = password_put(
                        id_user!!,
                        view_pasword.dialog_old_password.text.toString(),
                        view_pasword.dialog_new_password.text.toString()
                    )
                    Repository(requireContext()).change_password(passwordPut)
                    dialog_password.dismiss()

                }
            }
        }

    }

    fun dialog(changcontent: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        var view: View? = LayoutInflater.from(context).inflate(R.layout.change, null)

        alertDialogBuilder.setView(view!!)
        val dialog_change = alertDialogBuilder.create()
        dialog_change.show()
        view.dialog_title.text = "Cập Nhật $changcontent"
        view.dialog_content_change.hint = changcontent
        view.capnhat.setOnClickListener {
            if (view.dialog_content_change.text.isEmpty()) {
                Toast.makeText(context, "Bạn chưa nhập thông tin đầy đủ", Toast.LENGTH_SHORT).show()
            } else {
                if (changcontent.equals("Email")) {
                    val emailRequest: email_request =
                        email_request(view.dialog_content_change.text.toString(), id_user!!)
                    Repository(requireContext()).change_email_user(emailRequest)
                    presenter.getUserInformation(id_user!!)
                }
                if (changcontent.equals("Mssv")) {
                    val mssvRequest: mssv_request =
                        mssv_request(view.dialog_content_change.text.toString(), id_user!!)
                    Repository(requireContext()).change_mssv_user(mssvRequest)
                    presenter.getUserInformation(id_user!!)
                }
                if (changcontent.equals("Khoa")) {
                    val khoaRequest: khoa_request =
                        khoa_request(view.dialog_content_change.text.toString(), id_user!!)
                    Repository(requireContext()).change_khoa_user(khoaRequest)
                    presenter.getUserInformation(id_user!!)
                }
                dialog_change.dismiss()
            }
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val item2: MenuItem = menu.findItem(R.id.action_profile)
        val item3: MenuItem = menu.findItem(R.id.action_notification)
        item.isVisible = false
        item2.isVisible = false
        item3.isVisible = false
    }

    override fun UpdateViewDataQuestionProfile(question: question, type_content_id: Int) {
        TODO("Not yet implemented")
    }

    override fun UpdateViewDataUser(user: userprofile) {
        if (user?.mssv != null) {
            id_mssv.text = user?.mssv.toString()
        } else {
            id_mssv.text = "Chưa cập nhật"
        }

        if (user?.email != null) {
            id_email.text = user?.email.toString()
        } else {
            id_email.text = "Chưa cập nhật"
        }
        if (user?.department != null) {
            id_khoa.text = user?.department.toString()
        } else {
            id_khoa.text = "Chưa cập nhật"
        }

        /*-------------------click-----------------------*/
        change_user.setOnClickListener {
            dialog("Mssv")
        }
        change_email.setOnClickListener {
            dialog("Email")
        }
        change_khoa.setOnClickListener {
            dialog("Khoa")
        }
        change_password.setOnClickListener {
            dialogpassword()
        }
    }

}