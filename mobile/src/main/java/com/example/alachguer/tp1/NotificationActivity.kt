package com.example.alachguer.tp1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner



class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val arraySpinner = arrayOf("à l'heure","1 min", "15 min", "30min", "1 heure", "1 jour")
        val s = findViewById(R.id.choiceButton) as Spinner
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        s.adapter = adapter

        //Return on previous activity
        val CancelButton : Button?
        CancelButton = findViewById(R.id.annuler) as Button
        CancelButton.setOnClickListener{
            finish()
        }
    }
}
