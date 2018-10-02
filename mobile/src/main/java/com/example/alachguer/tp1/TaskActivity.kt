package com.example.alachguer.tp1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        toolbar = findViewById(R.id.task_toolbar)
        toolbar.setTitle(R.string.title_task_description)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        editTextTitle = findViewById(R.id.desc_title)
        editTextDescription = findViewById(R.id.desc_description)

        /*
        val intent = getIntent()
        val bundle = intent.extras
        val name: String = bundle.get(AddTaskFragment.TITLE_EXTRA) as String
        editTextTitle.setText(name)
        */

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
