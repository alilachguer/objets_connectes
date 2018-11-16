package com.example.alachguer.tp1
import android.content.Context
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    lateinit var listView: ListView
    private var listTodos = ArrayList<TodoModel>()
    lateinit var todoDbHelper: TodoDbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val filtreByName = R.id.nameFilter as EditText
        val view = inflater.inflate(R.layout.fragment_search, null)
        listTodos.add(TodoModel("Java", "JavaSampleApproach",
                "Java technology", "Sport", 0, 221, 0))
        listTodos.add(TodoModel("Todo", "Rendez-vous", "sdsd",
                "desc", 0, 221, 0))
        listTodos.add(TodoModel("Test", "Anniversaire", "sdssdsdsdsd",
                "desc dfds", 0, 221, 0))

        listView = view.findViewById<ListView>(R.id.listTodos)

        // intiation de la base de donnee
        todoDbHelper = TodoDbHelper(view.context)
        // insertion a la base de donnees

        /*
        todoDbHelper.insertTodo(listTodos.get(0))
        todoDbHelper.insertTodo(listTodos.get(1))
        todoDbHelper.insertTodo(listTodos.get(2))
        */

        // ajouter toutes les lignes de la bdd a la liste pour les lire
        todoDbHelper.readAllTodos().forEach {
            listTodos.add(it)
        }

        val customAdapter = CustomAdapter(view.context)
        customAdapter.mList = listTodos
        listView.adapter = customAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(context, "Item clicked: " + customAdapter.mList.get(id.toInt()).title, Toast.LENGTH_LONG).show()
        }
        return view;
        // class adapteur permet de personnaliser l'affichade des donnees grace a un layout
    }
}