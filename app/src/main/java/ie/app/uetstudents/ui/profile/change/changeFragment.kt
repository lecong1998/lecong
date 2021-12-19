package ie.app.uetstudents.ui.profile.change

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.change.view.*
import kotlinx.android.synthetic.main.fragment_change.*

class changeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        change_user.setOnClickListener {
            dialog("Username")
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

    fun dialog(changcontent: String)
    {
        val alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(context)
        var view : View?  = LayoutInflater.from(context).inflate(R.layout.change,null)

        alertDialogBuilder.setView(view!!)
        val dialog_change = alertDialogBuilder.create()
        dialog_change.show()
        view.dialog_title.text = "Cập Nhật $changcontent"
        view.back_dialog.setOnClickListener {
            dialog_change.dismiss()
        }

    }
    fun dialogpassword()
    {
        val alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(context)
        var view : View?  = LayoutInflater.from(context).inflate(R.layout.change_password,null)
        alertDialogBuilder.setView(view)
        val dialog_password = alertDialogBuilder.create()
        dialog_password.show()

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

}