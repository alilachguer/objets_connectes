package com.example.alachguer.tp1
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_add_task.*

class AddTaskFragment : Fragment(){

    lateinit var editText: EditText
    lateinit var datePicker: DatePicker
    lateinit var titleInput: EditText
    lateinit var descriptionInput: EditText

    companion object {
        val TITLE_EXTRA: String = "title_value"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_task, null)
        editText = view.findViewById(R.id.titre)
        datePicker = view.findViewById(R.id.date)

        editText.setOnClickListener{
            var li: LayoutInflater = LayoutInflater.from(context)
            var promptsView: View = li.inflate(R.layout.activity_task, null)
            var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setView(promptsView)

            var titleInput: EditText = promptsView.findViewById(R.id.desc_title);
            var descriptionInput: EditText = promptsView.findViewById(R.id.desc_description);

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


        return view
    }
}