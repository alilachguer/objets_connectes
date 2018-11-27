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
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    lateinit var listView: ListView
    private var listTodos = ArrayList<TodoModel>()
    lateinit var todoDbHelper: TodoDbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, null)
        val filtreByName = view.findViewById(R.id.nameFilter) as SearchView
        val typeFiltre = view.findViewById(R.id.typeFilter) as Spinner

        // intiation de la base de donnee
        todoDbHelper = TodoDbHelper(view.context)

//        todoDbHelper.insertTodo(TodoModel("Java", "JavaSampleApproach",
//                "Java technology", "Sport", 0, 221, 0))
//
//        todoDbHelper.insertTodo(TodoModel("Todo", "Rendez-vous", "sdsd",
//                "desc", 0, 221, 0))
//        todoDbHelper.insertTodo(TodoModel("Test", "Anniversaire", "sdssdsdsdsd",
//                "desc dfds", 0, 221, 0))

        listView = view.findViewById<ListView>(R.id.listTodos)

        // ajouter toutes les lignes de la bdd a la liste pour les lire
        todoDbHelper.readAllTodos().forEach {
            listTodos.add(it)
        }

        val customAdapter = CustomAdapter(view.context, listTodos)
        //customAdapter.mList = listTodos
        listView.adapter = customAdapter

        var searchText: String = ""

        // evenement de lorsque la valeur du spinner est changee
        typeFiltre.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // appel de filter avec deux parametres (text + valeur du type selectionne)
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                customAdapter.filter(searchText, typeFiltre.selectedItem.toString())
            }
        }
        )

        // filtre par texte lorsqu'un user ecrit dans la barre de recherche
        // cette fonction est appelee chaque fois que le user ajoute ou enleve une lettre
        filtreByName.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
            	// met la valeur du texte dans la variable searchtext
                searchText = newText.toString()
                // appele la fonction filter sur le texte dans la barre de recherche
                customAdapter.filter(searchText, typeFiltre.selectedItem.toString())
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(context, "Item clicked: " + customAdapter.mList.get(id.toInt()).title, Toast.LENGTH_LONG).show()
        }
        return view;
        // class adapteur permet de personnaliser l'affichade des donnees grace a un layout
    }
}