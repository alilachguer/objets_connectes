package com.example.alachguer.tp1
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.R.id.button1
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.alachguer.tp1.R.id.navigation


//implement the interface OnNavigationItemSelectedListener in your activity class
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //loading the default fragment
        loadFragment(CalendarFragment())

        //Make the button clickable
        val notificationButton : ImageButton ?
        notificationButton = findViewById(R.id.notification_button) as ImageButton
                notificationButton.setOnClickListener{
                    val intent  = Intent(this, NotificationActivity:: class.java)
                    startActivity(intent)
                }

        //getting bottom navigation view and attaching the listener
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_home -> fragment = AddTaskFragment()

            R.id.navigation_dashboard -> fragment = CalendarFragment()

            R.id.navigation_notifications -> fragment = SearchFragment()

        }

        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            return true
        }
        return false
    }
}