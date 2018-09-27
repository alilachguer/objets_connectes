package com.example.alachguer.tp1
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_search.listTodos


class SearchFragment : Fragment(){

    lateinit var listView: ListView
    private var listNotes = ArrayList<TodoModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_search, null)

        listNotes.add(TodoModel(1, "JavaSampleApproach",
                "Java technology", "date sdsd", 221, 54, 65))
        listNotes.add(TodoModel(1, "JavaSampleApproach",
                "Java technology", "date sdsd", 221, 54, 65))
        listNotes.add(TodoModel(1, "JavaSampleApproach",
                "Java technology", "date sdsd", 221, 54, 65))


    }
}