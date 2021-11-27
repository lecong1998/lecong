package ie.app.uetstudents.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        back_notification.setOnClickListener {
            onBackPressed()
        }
    }
}