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
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.alachguer.tp1.NotificationPublisher




//implement the interface OnNavigationItemSelectedListener in your activity class
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val btmNavigation: BottomNavigationView? = null
    lateinit var todoDBHelper: TodoDbHelper
    var myTTS : TextToSpeech? = null
    var mySpeechRecognize : SpeechRecognizer ? = null
    lateinit var lunchSpeechRecognizer : ImageButton


    fun initializeTextToSpeech(){
        myTTS = TextToSpeech(this, TextToSpeech.OnInitListener(
                @Override
                fun(i : Int){
                    if (myTTS != null) {
                        if (myTTS!!.engines.size ==0 ){
                            Toast.makeText(this,"Option non disponible sur votre téléphone",
                                    Toast.LENGTH_LONG).show()
                        }
                        else{
                            myTTS!!.setLanguage(Locale.FRENCH)
                            speak("Bonjour, je suis pret")
                        }
                    }
                }
        ) )


    }

    fun speak(message: String) {
        if(Build.VERSION.SDK_INT >= 21){
            myTTS!!.speak(message,TextToSpeech.QUEUE_FLUSH,null,null)
        }
        else{
            myTTS!!.speak(message,TextToSpeech.QUEUE_FLUSH,null)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lunchSpeechRecognizer = findViewById(R.id.SpeechButton)
        //loading the default fragment
        loadFragment(CalendarFragment())



        //getting bottom navigation view and attaching the listener
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(this)

        todoDBHelper = TodoDbHelper(this)

        //Start Voice recognision
        initializeTextToSpeech()
        initializeSpeechRecognizer()

        lunchSpeechRecognizer.setOnClickListener{
            var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1)
            mySpeechRecognize!!.startListening(intent)
        }


    }

    fun initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){



            mySpeechRecognize = SpeechRecognizer.createSpeechRecognizer(this)
            mySpeechRecognize!!.setRecognitionListener(object : RecognitionListener {
                override fun onBufferReceived(buffer: ByteArray) {
                }

                override fun onError(error: Int) {
                }

                override fun onEvent(eventType: Int, params: Bundle) {
                }

                override fun onPartialResults(partialResults: Bundle) {
                }

                override fun onReadyForSpeech(params: Bundle) {
                }

                override fun onResults(bundle: Bundle) {
                    var result : Array<String> = bundle.getStringArray(SpeechRecognizer.RESULTS_RECOGNITION)
                    processResult(result.get(0))


                }

                override fun onRmsChanged(rmsdB: Float) {
                }

                override fun onBeginningOfSpeech() {
                }

                override fun onEndOfSpeech() {
                }
            })

        }
    }

    private fun processResult(command: String) {
        val command = command.toLowerCase()

        if(command.indexOf("Oui") != -1) {
            speak("Je vous affiche la liste des tâches")
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CalendarFragment()).commit()

        }

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
        val NotifIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val CHANNEL_ID = "my_channel_01"
        val importance =  NotificationManager.IMPORTANCE_HIGH
        val myChannel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,importance)
        mManager.createNotificationChannel(myChannel)

        var builder : Notification.Builder
        when(type){
            "Sport" -> {
                builder = Notification.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_running)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(NotifIntent)
                        .setAutoCancel(true)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                }
            }
            "Anniversaire" -> {
                builder = Notification.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_cake)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                }
            }
            "Travail" -> {
                builder = Notification.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_portfolio)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                }
            }
            "Rendez-vous" -> {
                builder = Notification.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_group)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                }
            }

            else -> {
                builder = Notification.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_remove_circle_outline_black_24dp)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                }
            }
        }



        val notification =  builder.build()
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        this.startForegroundService(notificationIntent)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }

    override fun onPause(){
        super.onPause()
        myTTS!!.shutdown()
    }

}

