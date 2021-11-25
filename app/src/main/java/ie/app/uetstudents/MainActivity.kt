package ie.app.uetstudents

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.databinding.ActivityMainBinding
import ie.app.uetstudents.databinding.ActivityMainBinding.inflate
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Search.PersonDto
import ie.app.uetstudents.ui.Entity.Search.QuestionDto
import ie.app.uetstudents.ui.Entity.Search.search_person
import ie.app.uetstudents.ui.Entity.Search.search_question
import ie.app.uetstudents.ui.notifications.NotificationsActivity
import ie.app.uetstudents.ui.profile.ProfileActivity
import ie.app.uetstudents.ui.timkiem.*
import kotlinx.android.synthetic.main.searchdialog_fullscreen.*
import kotlinx.android.synthetic.main.searchdialog_fullscreen.view.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var  view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

   @SuppressLint("ResourceAsColor")
   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(binding.root.context, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.action_profile -> {
                val intent = Intent(binding.root.context, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.action_notification -> {
                val intent = Intent(binding.root.context,NotificationsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}