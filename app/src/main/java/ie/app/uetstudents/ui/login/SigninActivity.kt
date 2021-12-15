package ie.app.uetstudents.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.app.uetstudents.R
import ie.app.uetstudents.utils.Constants.KEY_PREFERENCE_USER
import ie.app.uetstudents.utils.PreferenceUtils

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }
}