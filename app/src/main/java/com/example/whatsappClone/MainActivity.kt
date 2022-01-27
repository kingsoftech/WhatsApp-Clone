package com.example.whatsappClone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.viewpager2.widget.ViewPager2
import com.example.whatsappClone.adapters.FragmentsAdapter
import com.example.whatsappClone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fragmentsAdapter: FragmentsAdapter
    private lateinit var tabLayout:TabLayout
    private lateinit var viewpager2:ViewPager2
    var titles: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titles.add("Chats")
        titles.add("Status")
        titles.add("Calls")
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        setContentView(activityMainBinding?.root)

       tabLayoutView()
    }

    private fun tabLayoutView() {
        fragmentsAdapter = FragmentsAdapter(supportFragmentManager, lifecycle)
        viewpager2 = activityMainBinding!!.viewPager
        tabLayout = activityMainBinding!!.tabLayout
        viewpager2.adapter = fragmentsAdapter
        TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
            when(position){
                0->{
                    tab.text = titles[0]
                }
                1->{
                    tab.text = titles[1]
                }
                2->{
                    tab.text = titles[2]
                }

            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.setting -> {
                startActivity(Intent(this@MainActivity,ActivitySettings::class.java))
            }
            R.id.log_out ->{
                mAuth.signOut()
                startActivity(Intent(this@MainActivity,SignInActivity::class.java))
                finish()
            }
            R.id.group_chat->{
                startActivity(Intent(this@MainActivity,GroupChatActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}