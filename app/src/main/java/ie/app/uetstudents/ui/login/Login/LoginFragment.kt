package ie.app.uetstudents.ui.login.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ie.app.uetstudents.MainActivity
import ie.app.uetstudents.R
import ie.app.uetstudents.data.request.RegisterFirebaseTokenRequest
import ie.app.uetstudents.service.FirebaseService.Companion.KEY_DATA_FCM
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Account.Get.dangnhap.dangnhap_account
import ie.app.uetstudents.ui.Entity.Account.Get.dangnhap.xacminhdangnhap
import ie.app.uetstudents.ui.Entity.Question.get.QuestionX
import ie.app.uetstudents.ui.login.SigninActivity
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {


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

        val token = PreferenceUtils.getString(KEY_DATA_FCM, "")
        if(token != "") {
            val request = RegisterFirebaseTokenRequest(token, "1")
            val call = ApiClient.getClient.registerFirebaseToken(request)
            call.enqueue(object : Callback<ie.app.uetstudents.data.response.Response>{
                override fun onResponse(
                    call: Call<ie.app.uetstudents.data.response.Response>,
                    response: Response<ie.app.uetstudents.data.response.Response>
                ) {
                    var id_user = 1
                    val intent = Intent(activity,MainActivity::class.java)
                    intent.putExtra("id_user",id_user)
                    intent.putExtra("username",username)
                    startActivity(intent)
                }

                override fun onFailure(
                    call: Call<ie.app.uetstudents.data.response.Response>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })
        }



    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

}