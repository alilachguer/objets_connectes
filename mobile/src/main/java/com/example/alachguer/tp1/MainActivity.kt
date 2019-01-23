package com.example.alachguer.tp1
import android.Manifest
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
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v4.app.RemoteInput
import android.widget.Toast
import com.example.alachguer.tp1.NotificationPublisher




//implement the interface OnNavigationItemSelectedListener in your activity class
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val btmNavigation: BottomNavigationView? = null
    lateinit var todoDBHelper: TodoDbHelper
    var myTTS : TextToSpeech? = null
    var mySpeechRecognize : SpeechRecognizer ? = null
    lateinit var lunchSpeechRecognizer : ImageButton
    lateinit var todoDbHelper: TodoDbHelper

    fun stringToWords(s : String) = s.trim().splitToSequence(' ')
            .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
            .toList()


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
                            speak("Bonjour, je suis prête à suivre vos instructions")
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


        lunchSpeechRecognizer = this.findViewById(R.id.SpeechButton)
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
            speak("J'écoute vos instructions")
            var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1)
            val permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            101)
                }
            }
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
                    val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
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
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        var titre : String = ""
        var bundle : Bundle = Bundle()

        //Supprimer tout
        //Ajouter une tache avec un titre
        //Liste de ce que tu peux faire
        //Quitter l'application
        val command = command.toLowerCase()


        if(command.indexOf("supprime")!=-1){
            speak("Je supprime toutes vos taches")
            todoDbHelper = TodoDbHelper(this)
            todoDBHelper.deleteAllTodos()
            navigation.setSelectedItemId(R.id.navigation_notifications);
            loadFragment(SearchFragment())
            return
        }

        if(command.indexOf("affiche") != -1 || command.indexOf("liste") != -1
                || command.indexOf("toute") != -1) {
            speak("Je vous affiche la liste des tâches")
            navigation.setSelectedItemId(R.id.navigation_notifications);
            loadFragment(SearchFragment())
            return
        }

        if(command.indexOf("ajoute")!=-1 || command.indexOf("ajout")!=-1 ||
                command.indexOf("ajoutez")!=-1  || command.indexOf("ajouter")!=-1 ){



            if(command.indexOf("titre")==-1){

                titre  = command.substringAfter("titre")
                speak("Le titre de votre tâche est "+titre)
                var myFrag : Fragment = AddTaskFragment()
                bundle.putString("Titre", titre)
                myFrag.setArguments(bundle)
                navigation.setSelectedItemId(R.id.navigation_home);
                loadFragment(myFrag)
                return
            }
            else{
                if(command.indexOf("description")!=-1){
                    var description =  command.substringAfter("description")
                    var commandArr = stringToWords(command)
                    command.replaceAfter(commandArr.get(commandArr.indexOf("et")),"")
                    bundle.putString("Description",description)
                }
                titre =command.substringAfter("tâche")
                speak("Le titre de votre tâche est "+titre)
                var myFrag : Fragment = AddTaskFragment()
                bundle.putString("Titre", titre)
                myFrag.setArguments(bundle)
                navigation.setSelectedItemId(R.id.navigation_home);
                loadFragment(myFrag)
                return
            }
        }

        if(command.indexOf("faire")!=-1){
            speak("Je peux : Supprimer toutes vos tâches , Quitter l'application, Ajouter une tâche avec ou sans titre et afficher la liste des tâches")
            return
        }



        if(command.indexOf("arrête")!=-1){
            speak("Merci de votre utilisation, aurevoir")
            finish()
        }

        speak("Je n'ai pas compris, Pour connaitre la liste des instructions possibles dites : Que sais-tu faire ?")
        return

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



        var builder : NotificationCompat.Builder
        val notificationId = Random().nextInt()

        //Intent for DelayAction
        val delayIntent = Intent(this,NotificationDelay::class.java)

        //Intent for Notification
        val NotifIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        //Construct notification depending on type
        when(type){
            "Sport" -> {
                builder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_running)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(NotifIntent)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_running, "Commencez votre exercice", null)
                        .setVibrate(longArrayOf(500,500))
            }
            "Anniversaire" -> {
                builder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_cake)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(longArrayOf(500,500))

            }
            "Travail" -> {
                builder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_portfolio)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(longArrayOf(500,500))
            }
            "Rendez-vous" -> {
                builder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_group)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(longArrayOf(500,500))
            }
            else -> {
                builder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_remove_circle_outline_black_24dp)
                        .setContentTitle(type)
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(longArrayOf(500,500))
            }
        }

        //Je put l'ID de la notif pour le recup plus tard pour le delais
        delayIntent.putExtra(NotificationDelay.NOTIFICATION_ID,notificationId)
        val delayPendingIntent = PendingIntent.getBroadcast(this,0,delayIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        //Second version de la notif
        val remoteInput = RemoteInput.Builder("DELAY")
                .setLabel("Repoussez dans ")
                .setChoices(resources.getStringArray(R.array.delay_choices))
                .build()

        val replyAction = NotificationCompat.Action.Builder(
                R.drawable.ic_notifications_white_24dp, "Repoussez la notification", delayPendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build()


        //Je créer l'action pour delais la notif
//        val action = NotificationCompat.Action.Builder(
//                R.drawable.ic_notifications_white_24dp,"Repoussez la notification",delayPendingIntent
//        ).build()


        builder.addAction(replyAction)
        val notification =  builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL

        val notificationIntent = Intent(this, NotificationPublisher::class.java)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)


        //Ici pour schedule ma notif
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }

    override fun onPause(){
        super.onPause()
        myTTS!!.shutdown()
    }

}
