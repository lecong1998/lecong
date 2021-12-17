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
import ie.app.uetstudents.data.request.LoginRequest
import ie.app.uetstudents.data.request.RegisterFirebaseTokenRequest
import ie.app.uetstudents.data.response.login.LoginResponse
import ie.app.uetstudents.service.FirebaseService.Companion.KEY_DATA_FCM
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.utils.Constants.KEY_PREFERENCE_USER
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
       // PreferenceUtils.clearAll()
       // PreferenceUtils.remove(KEY_PREFERENCE_USER);
        val user = PreferenceUtils.getUser()

        if(user != null) {
            val intent = Intent(activity,MainActivity::class.java)
            intent.putExtra("id_user",user.id)
            intent.putExtra("username", user.username)
            startActivity(intent)
        }




        super.onViewCreated(view, savedInstanceState)

        login_fragment_btn_login?.setOnClickListener {
            if (login_fragment_edt_username.text.toString().isEmpty()||
                login_fragment_edt_password.text.toString().isEmpty()
            ) {
                Toast.makeText(context,"Thông tin chưa đầy đủ",Toast.LENGTH_SHORT).show()
            } else {
                CallApiAccount(
                    login_fragment_edt_username.text.toString().trim(),
                    login_fragment_edt_password.text.toString()
                )
            }
        }
        login_fragment_btn_register?.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_logoutFragment)
        }
    }

    private fun CallApiAccount(username: String, password: String) {

        val request = LoginRequest(
            username, password
        )

        val callLogin = ApiClient.getClient.callSigninAccount(request)
//        val callLogin = ApiClient.getClient.callSigninAccount2(username, password, "on")

        callLogin.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()?.success == true) {
                    PreferenceUtils.saveObject(KEY_PREFERENCE_USER, response.body()?.accountDto)
                    val idUser = response.body()?.accountDto?.id

                    val token = PreferenceUtils.getString(KEY_DATA_FCM, "")
                    if(token != "") {
                        val request = RegisterFirebaseTokenRequest(token, PreferenceUtils.getUser().id.toString())
                        val callFirebase = ApiClient.getClient.registerFirebaseToken(request)
                        callFirebase.enqueue(object : Callback<ie.app.uetstudents.data.response.Response>{
                            override fun onResponse(
                                call: Call<ie.app.uetstudents.data.response.Response>,
                                response: Response<ie.app.uetstudents.data.response.Response>
                            ) {
                                val intent = Intent(activity,MainActivity::class.java)
                                intent.putExtra("id_user",idUser)
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
                } else {
                    Toast.makeText(context,response.body()?.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context,"Có lỗi xảy ra, vui lòng thử lại sau.",Toast.LENGTH_SHORT).show()
            }

        })





    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

}
