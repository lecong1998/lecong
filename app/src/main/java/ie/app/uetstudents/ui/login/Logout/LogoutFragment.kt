package ie.app.uetstudents.ui.login.Logout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Account.AccountDto
import ie.app.uetstudents.ui.Entity.Account.account
import ie.app.uetstudents.ui.login.LogContract
import kotlinx.android.synthetic.main.fragment_logout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class LogoutFragment : Fragment(), LogContract.View {

    private lateinit var presenter: LogContract.Presenter
    private lateinit var account: account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            register_fragment_btn_register.setOnClickListener {


                if (register_fragment_edt_username.text.toString().isEmpty()
                    ||register_fragment_edt_password.text.toString().isEmpty()
                    ||register_fragment_edt_name.text.toString().isEmpty()
                    ||register_fragment_edt_email.text.toString().isEmpty())
                {
                    Toast.makeText(context,"Thông tin chưa đầy đủ",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    callApi(register_fragment_edt_username.text.toString(),
                        register_fragment_edt_password.text.toString(),
                        register_fragment_edt_name.text.toString(),
                        register_fragment_edt_email.text.toString())

                }

            }


        register_fragment_btn_cancel?.setOnClickListener {
            it.findNavController().navigate(R.id.action_logoutFragment_to_loginFragment)
        }
    }

    private fun callApi(username: String, password: String, fullname: String, email: String) {

           // val list = ArrayList<AccountDto>()
            //list.add(AccountDto("anh",1,"111","111",1,"lecng"))
            //account = account(list,"ok",true)
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            presenter.getAccount()
            account!!.accountDtoList.forEach {
                if (it.username.equals(register_fragment_edt_username.text.toString()))
                {
                    Toast.makeText(context,"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show()
                    return
                }
            }

            val call : Call<account> =
                ApiClient.getClient.CallsetAccount(
                    AccountDto("",
                        null,
                        password,
                        null,
                        null,
                        username
                    )
                )

            call.enqueue(object : Callback<account>{
                override fun onResponse(call: Call<account>, response: Response<account>) {
                    if(response.isSuccessful){
                        Log.e("Api","Call thành công")
                    }

                }

                override fun onFailure(call: Call<account>, t: Throwable) {
                    Log.e("Api","Call thất bại")
                }
            })

            this.findNavController().navigate(R.id.action_logoutFragment_to_loginFragment)
            Toast.makeText(context,"Đăng ký tài khoản thành công",Toast.LENGTH_SHORT).show()



    }

    override fun UpdateViewData(account: account) {
        this.account = account
    }

}