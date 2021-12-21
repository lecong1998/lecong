package ie.app.uetstudents.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ie.app.uetstudents.R
import ie.app.uetstudents.utils.Constants.KEY_PREFERENCE_USER
import ie.app.uetstudents.utils.PreferenceUtils

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("dLog", "SigninActivity onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }
}