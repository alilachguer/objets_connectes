package com.example.alachguer.tp1
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.content.Intent
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.widget.ImageButton
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread
import android.app.AlarmManager
import android.support.v4.content.ContextCompat.getSystemService
import android.os.SystemClock
import android.app.PendingIntent
import com.example.alachguer.tp1.NotificationPublisher




//implement the interface OnNavigationItemSelectedListener in your activity class
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val btmNavigation: BottomNavigationView? = null
    lateinit var todoDBHelper: TodoDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //loading the default fragment
        loadFragment(CalendarFragment())

        //Make the button clickable
        val notificationButton: ImageButton?
        notificationButton = findViewById(R.id.notification_button) as ImageButton
        notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        //getting bottom navigation view and attaching the listener
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(this)

        todoDBHelper = TodoDbHelper(this)
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

    fun createNotification(name: String, type: String, delay : Long) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        val mManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val CHANNEL_ID = "my_channel_01"
        val importance =  NotificationManager.IMPORTANCE_HIGH
        val myChannel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,importance)
        mManager.createNotificationChannel(myChannel)
        val builder = Notification.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_remove_circle_outline_black_24dp)
                .setContentTitle(type)
                .setContentText(name)
//                    .setStyle(NotificationCompat.BigTextStyle()
//                            .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
        }

        val notification =  builder.build()
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        this.startForegroundService(notificationIntent)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)

    }
}

