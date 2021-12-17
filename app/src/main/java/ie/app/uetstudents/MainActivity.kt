package ie.app.uetstudents

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import ie.app.uetstudents.data.response.Response
import ie.app.uetstudents.databinding.ActivityMainBinding
import ie.app.uetstudents.service.FirebaseService
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.notifications.notification_service
import ie.app.uetstudents.ui.timkiem.*
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity() {

    var mBroadcastAction = "STRING_BROADCAST_ACTION"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var  view : View

    private  var username : String = "16020859"
    private var id_user : Int? = null

    private var textItemCount : TextView? = null

    var unreadCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = intent.getStringExtra("username")
        id_user = intent.getIntExtra("id_user",0)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding?.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_notifications,
                R.id.nav_document,
                R.id.nav_exam,
                R.id.nav_news,
                R.id.nav_forum,
                R.id.nav_uettalk,
                R.id.nav_login
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val intent = Intent(this, notification_service::class.java)
        startService(intent)

        Handler(Looper.getMainLooper()).postDelayed({
            getUnreadNotification(PreferenceUtils.getUser().id)
        }, 500)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val menuItem = menu.findItem(R.id.action_notification)
        val actionView = menuItem.actionView
        textItemCount = actionView.findViewById<View>(R.id.unread_notification) as TextView

        setupBadge(unreadCount)

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
        return true
    }

    private fun setupBadge(count : Int) {

        if (textItemCount != null) {
            textItemCount?.isVisible = count != 0
            textItemCount?.text = "$count"
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        if(event == FirebaseService.UPDATE_NOTIFICATION) {
            getUnreadNotification(PreferenceUtils.getUser().id)
        }
    }

    private fun getUnreadNotification(userId : Int) {
        unreadCount = 0
        setupBadge(unreadCount)
        val callComment = ApiClient.getClient.getUnreadComment(userId)
        callComment.enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                unreadCount += response.body()?.resultQuantity ?: 0
                setupBadge(unreadCount)
            }

            override fun onFailure(
                call: Call<Response>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })

        val callQuestion = ApiClient.getClient.getUnreadQuestion(userId)
        callQuestion.enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                unreadCount += response.body()?.resultQuantity ?: 0
                setupBadge(unreadCount)
            }

            override fun onFailure(
                call: Call<Response>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

   @SuppressLint("ResourceAsColor")
   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {

                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_search)
            }
            R.id.action_profile -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_profile)
            }
            R.id.action_notification -> {

                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_notification)

            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onSupportNavigateUp(): Boolean {
        val bundle = Bundle()
        bundle.putInt("id_user", id_user!!)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //navController.navigate(R.id.mobile_navigation,bundle)
        return navController.navigateUp(appBarConfiguration)|| super.onSupportNavigateUp()
    }

  /*  override fun onStart() {
        super.onStart()

       // val intentFilter = IntentFilter(mBroadcastAction)
        //registerReceiver(mbroadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(mbroadcastReceiver)
    }

 /*   val mbroadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action?.equals(MainActivity().mBroadcastAction)!!)
            {

                val jsonString : String = intent.getStringExtra("broadcast")
                val type = object : TypeToken<List<NotificationCommentDto?>?>() {}.type
                val gson : Gson = Gson()
                val notifi : List<NotificationCommentDto> = gson.fromJson(jsonString,type)

            }
        }
    }*/

*/
}
