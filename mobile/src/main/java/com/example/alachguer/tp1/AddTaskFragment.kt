package com.example.alachguer.tp1
import android.content.Intent
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker
import android.widget.EditText

class AddTaskFragment : Fragment(){

    lateinit var editText: EditText
    lateinit var datePicker: DatePicker

    companion object {
        val TITLE_EXTRA: String = "title_value"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_task, null)
        editText = view.findViewById(R.id.titre)
        datePicker = view.findViewById(R.id.date)

        editText.setOnClickListener{
            val descriptionActivity = Intent(context, TaskActivity::class.java)
            descriptionActivity.apply {
                putExtra(TITLE_EXTRA, editText.text)
            }
            startActivity(descriptionActivity)
        }


        return view
    }
}