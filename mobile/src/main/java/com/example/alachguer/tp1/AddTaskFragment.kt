package com.example.alachguer.tp1
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle;
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import android.widget.EditText
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*



class AddTaskFragment : Fragment()
{

    lateinit var editText: EditText
    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker
    lateinit var titleInput: EditText
    lateinit var typeInput: Spinner
    lateinit var descriptionInput: EditText
    lateinit var select_date_time: Button
    lateinit var add_task: Button
    lateinit var activeNotif: Switch
    lateinit var timeTask: Spinner
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    lateinit var todoDbHelper: TodoDbHelper

    companion object {
        val TITLE_EXTRA: String = "title_value"
    }

    private fun isEmpty(etText: EditText): Boolean {
        return (etText.text.toString().trim { it <= ' ' }.length > 0)
    }


    @SuppressLint("WrongViewCast")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        val view = inflater.inflate(R.layout.fragment_add_task, null)
        typeInput = view.findViewById(R.id.typeTask)
        editText = view.findViewById(R.id.titre)
        select_date_time = view.findViewById(R.id.select_date_time)
        add_task = view.findViewById(R.id.add_task_button)
        activeNotif = view.findViewById(R.id.actifNotif)
        timeTask = view.findViewById(R.id.TimeTask)



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
            if (!isEmpty(editText) || month==0 || year==0 || day.toString().equals("0")){
                Toast.makeText(view.context,"Entrez un titre et une description",Toast.LENGTH_SHORT).show()
            }
            else{
                var datee: String = day.toString()+"/"+month+"/"+year
                var todo: TodoModel = TodoModel(
                        titleInput.text.toString(),
                        descriptionInput.text.toString(),
                        datee,
                        typeInput.getSelectedItem().toString(),
                        hour,
                        minute,
                        //0 : notification off, changer a partir des parametres, prend que 1 ou 0
                        0)

                todoDbHelper = TodoDbHelper(view.context)
                todoDbHelper.insertTodo(todo)
                Toast.makeText(view.context,"tâche: " + todo.title,Toast.LENGTH_SHORT).show()
                editText.getText().clear();
                typeInput.setSelection(0);


                if(activeNotif.isChecked){

                    val time = timeTask.selectedItem
                    var timeInMillis = 0
                    when(time){
                        "0" -> timeInMillis=0
                        "5 min" -> timeInMillis = 300000
                        "15 min" -> timeInMillis = 900000
                        "30 min" -> timeInMillis = 900000*2
                        "60 min" -> timeInMillis = 900000*4
                        else -> timeInMillis = 0
                    }

                    var Calendar = Calendar.getInstance()
                    Calendar.set(year,month,day,hour,minute,0)
                    ((activity as MainActivity)).createNotification(todo.title,todo.type,Calendar.timeInMillis+timeInMillis)
                }

            }
        }
        return view
    }

}


