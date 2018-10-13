package com.example.alachguer.tp1
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle;
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*
import kotlinx.android.synthetic.main.fragment_add_task.*

class AddTaskFragment : Fragment(){

    lateinit var editText: EditText
    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker
    lateinit var titleInput: EditText
    lateinit var descriptionInput: EditText
    lateinit var select_date_time: Button
    lateinit var add_task: Button
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    companion object {
        val TITLE_EXTRA: String = "title_value"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_task, null)
        editText = view.findViewById(R.id.titre)
        select_date_time = view.findViewById(R.id.select_date_time)
        add_task = view.findViewById(R.id.add_task_button)

        editText.setOnClickListener{
            var li: LayoutInflater = LayoutInflater.from(context)
            var promptsView: View = li.inflate(R.layout.activity_task, null)
            var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setView(promptsView)

            titleInput = promptsView.findViewById(R.id.desc_title);
            descriptionInput = promptsView.findViewById(R.id.desc_description);


            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK"){
                        dialog, whichbutton ->
                            titre.setText(titleInput.text)
                    }
                    .setNegativeButton("Cancel"){
                        dialog, whichbutton -> dialog.cancel()
                    }

            var alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        select_date_time.setOnClickListener{
            var li: LayoutInflater = LayoutInflater.from(context)
            var promptsView: View = li.inflate(R.layout.date_time, null)
            var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setView(promptsView)

            datePicker = promptsView.findViewById(R.id.date_picker)
            timePicker = promptsView.findViewById(R.id.time_picker)

            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK"){
                        dialog, whichbutton ->
                        day = datePicker.dayOfMonth
                        month = datePicker.month
                        year = datePicker.year
                        hour = timePicker.hour
                        minute = timePicker.minute
                        Toast.makeText(context, day.toString() +"/"+month+"/"+year+" , "+hour+":"+minute, Toast.LENGTH_SHORT).show()

                    }
                    .setNegativeButton("Cancel"){
                        dialog, whichbutton -> dialog.cancel()
                    }

            var alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        add_task.setOnClickListener{
            var todo: TodoModel = TodoModel(
                    titleInput.text.toString(),
                    descriptionInput.text.toString(),
                    day.toString()+"/"+month+"/"+year,
                    hour,
                    minute,
                    //0 : notification off, changer a partir des parametres, prend que 1 ou 0
                    0)
        }


        return view
    }
}