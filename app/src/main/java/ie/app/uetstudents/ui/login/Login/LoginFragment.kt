package ie.app.uetstudents.ui.login.Login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import ie.app.uetstudents.MainActivity
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.Entity.Account.account
import ie.app.uetstudents.ui.login.LogContract
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(),LogContract.View {

    private lateinit var presenter: LogContract.Presenter
    private lateinit var account: account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_fragment_btn_login?.setOnClickListener {

            if (
                login_fragment_edt_username.text.toString().isEmpty()||
                login_fragment_edt_password.text.toString().isEmpty()
            )
            {
                Toast.makeText(context,"Thông tin chưa đầy đủ",Toast.LENGTH_SHORT).show()
            }
            else
            {
                CallApiAccount(
                    login_fragment_edt_username.text.toString(),
                    login_fragment_edt_password.text.toString()
                )
            }
        }
        login_fragment_btn_register?.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_logoutFragment)
        }
    }

    private fun CallApiAccount(username: String, password: String) {

        presenter.getAccount()

        account!!.accountDtoList.forEach {
            if (it.username.equals(username))
            {
                if (it.password.equals(password))
                {
                    val intent = Intent(context,MainActivity::class.java)
                    intent.putExtra("username",username)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(context,"Mật khẩu không đúng!",Toast.LENGTH_SHORT).show()
                }
                return
            }

        }

        Toast.makeText(context,"Tài khoản không tồn tại",Toast.LENGTH_LONG).show()

    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun UpdateViewData(account: account) {
        this.account = account
    }
}